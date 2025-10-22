package com.eainde.form_forge.service;

import com.eainde.form_forge.annotations.*;
import com.eainde.form_forge.models.JsonFormResponse;
import com.eainde.form_forge.models.schema.JsonSchema;
import com.eainde.form_forge.models.schema.SchemaNode;
import com.eainde.form_forge.models.schema.SchemaProperty;
import com.eainde.form_forge.models.uischema.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The core service that generates JSON Form schema and UI schema from annotated Java classes.
 * This class uses reflection to scan DTOs for annotations and build a JSON structure
 * that is compatible with the JSON Forms library.
 */
@Component
public class JsonFormGenerator {

    /**
     * The main entry point for generating a JSON Form response. It orchestrates the
     * entire process of schema and UI schema generation.
     *
     * @param targetClass     The DTO class annotated with @JsonForm. This class defines the
     * structure of the form.
     * @param dynamicMetadata A map for providing dynamic data (e.g., for dropdowns) at runtime.
     * The outer key is the field name, and the inner map contains
     * properties like "enum" and "enumNames".
     * @return A {@link JsonFormResponse} containing the generated schema and uischema.
     * @throws IllegalArgumentException if the target class is not annotated with @JsonForm.
     */
    public JsonFormResponse generate(Class<?> targetClass, Map<String, Map<String, Object>> dynamicMetadata) {
        if (dynamicMetadata == null) {
            dynamicMetadata = new HashMap<>();
        }
        JsonForm classAnnotation = targetClass.getAnnotation(JsonForm.class);
        if (classAnnotation == null) {
            throw new IllegalArgumentException("The target class must be annotated with @JsonForm.");
        }

        JsonSchema schema = new JsonSchema();
        schema.setType("object");
        schema.setTitle(classAnnotation.title());
        schema.setDescription(classAnnotation.description());

        UiSchema uischema = new UiSchema();
        List<String> requiredFields = new ArrayList<>();

        // Pass the root DTO class (targetClass) for rule parsing context.
        processClassFields(targetClass, classAnnotation, uischema.getElements(), requiredFields, "#/properties/", schema, dynamicMetadata, targetClass, null, null);

        if (!requiredFields.isEmpty()) {
            schema.setRequired(requiredFields);
        }

        applyDynamicMetadata(schema, uischema, dynamicMetadata, targetClass);
        return new JsonFormResponse(schema, uischema);
    }

    /**
     * The main dispatcher for processing fields within a class. It checks if a custom declarative
     * layout is defined via the @Layout annotation and delegates to the appropriate processing method.
     *
     * @param targetClass          The class whose fields are being processed.
     * @param groupAnnotation      The annotation of the group being processed, if this is a nested call. Null for the root class.
     * @param parentSchema         The JSON schema of the parent element to which properties will be added.
     * @param parentUiElements     The list of UI elements of the parent to which new controls or layouts will be added.
     * @param parentRequiredFields The list of required fields for the parent schema.
     * @param currentScope         The current JSON scope path (e.g., "#/properties/").
     */
    private void processClassFields(Class<?> targetClass, JsonForm classAnnotation, List<UiSchemaLayoutElement> parentUiElements, List<String> parentRequiredFields, String currentScope, JsonSchema parentSchema, Map<String, Map<String, Object>> dynamicMetadata, Class<?> rootDtoClass, Map<String, Object> fieldRules, String dynamicPropertyPrefix) {
        Layout layout = (classAnnotation != null) ? classAnnotation.layout() : null;

        if (layout != null && layout.value().length > 0) {
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : targetClass.getDeclaredFields()) {
                fieldMap.put(field.getName(), field);
            }
            for (LayoutItem item : layout.value()) {
                List<UiSchemaLayoutElement> currentContainer = parentUiElements;
                if (item.type() == LayoutType.HORIZONTAL) {
                    UiSchemaHorizontalLayoutElement hLayout = new UiSchemaHorizontalLayoutElement();
                    parentUiElements.add(hLayout);
                    currentContainer = hLayout.getElements();
                }
                for (String fieldName : item.fields()) {
                    Field field = fieldMap.get(fieldName);
                    if (field != null) {
                        processSingleField(field, parentSchema, currentContainer, parentRequiredFields, currentScope, dynamicMetadata, rootDtoClass, fieldRules, dynamicPropertyPrefix);
                    }
                }
            }
        } else {
            processWithDefaultLayout(targetClass, parentSchema, parentUiElements, parentRequiredFields, currentScope, dynamicMetadata, rootDtoClass, fieldRules, dynamicPropertyPrefix);
        }
    }

    /**
     * Processes fields based on a declarative @Layout annotation. This allows for creating
     * complex UIs with nested horizontal and vertical layouts.
     *
     * @param layout               The @Layout annotation instance defining the UI structure.
     * @param targetClass          The class whose fields are being processed.
     * @param parentSchema         The JSON schema of the parent element.
     * @param parentUiElements     The list of UI elements of the parent.
     * @param parentRequiredFields The list of required fields for the parent schema.
     * @param currentScope         The current JSON scope path.
     */
    /*private void processWithCustomLayout(Layout layout, Class<?> targetClass, JsonSchema parentSchema, List<UiSchemaLayoutElement> parentUiElements, List<String> parentRequiredFields, String currentScope, Map<String, Map<String, Object>> dynamicMetadata) {
        // Create a mutable map of all declared fields in the class for easy lookup and to track which fields have been processed.
        Map<String, Field> fieldMap = new HashMap<>(Arrays.stream(targetClass.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, field -> field)));

        // Iterate through each layout item defined in the @Layout annotation.
        for (LayoutItem item : layout.value()) {
            // Create the appropriate layout container (Horizontal or Vertical).
            List<UiSchemaLayoutElement> layoutElements = new ArrayList<>();
            if (item.type() == LayoutType.HORIZONTAL) {
                UiSchemaHorizontalLayoutElement uiLayout = new UiSchemaHorizontalLayoutElement();
                uiLayout.setElements(layoutElements);
                parentUiElements.add(uiLayout);
            } else {
                UiSchemaVerticalLayoutElement uiLayout = new UiSchemaVerticalLayoutElement();
                uiLayout.setElements(layoutElements);
                parentUiElements.add(uiLayout);
            }

            // Process each field specified in the layout item's 'fields' array and add it to the new layout container.
            for (String fieldName : item.fields()) {
                Field field = fieldMap.get(fieldName);
                if (field != null) {
                    processSingleField(field, parentSchema, layoutElements, parentRequiredFields, currentScope,dynamicMetadata);
                    // Remove the field from the map so it isn't processed again as a "remaining" field.
                    fieldMap.remove(fieldName);
                }
            }
        }

        // After processing all custom layouts, process any remaining fields that were not part of any layout item.
        // These will be added vertically at the end of the form/group.
        for (Field remainingField : fieldMap.values()) {
            processSingleField(remainingField, parentSchema, parentUiElements, parentRequiredFields, currentScope,dynamicMetadata);
        }
    }*/

    /**
     * Processes fields in their default declaration order, creating a simple vertical list of controls.
     * This is the fallback behavior when no custom @Layout is specified.
     *
     * @param targetClass          The class whose fields are being processed.
     * @param parentSchema         The JSON schema of the parent element.
     * @param parentUiElements     The list of UI elements of the parent.
     * @param parentRequiredFields The list of required fields for the parent schema.
     * @param currentScope         The current JSON scope path.
     */
    private void processWithDefaultLayout(Class<?> targetClass, JsonSchema parentSchema, List<UiSchemaLayoutElement> parentUiElements, List<String> parentRequiredFields, String currentScope, Map<String, Map<String, Object>> dynamicMetadata, Class<?> rootDtoClass, Map<String, Object> fieldRules, String dynamicPropertyPrefix) {
        for (Field field : targetClass.getDeclaredFields()) {
            processSingleField(field, parentSchema, parentUiElements, parentRequiredFields, currentScope, dynamicMetadata, rootDtoClass, fieldRules, dynamicPropertyPrefix);
        }
    }

    /**
     * Processes a single field, determining if it's a regular form field (@JsonFormField) or a
     * nested group of fields (@JsonFormGroup) and calls the appropriate handler.
     *
     * @param field                The field to process.
     * @param parentSchema         The JSON schema of the parent element.
     * @param parentUiElements     The list of UI elements where the new element for this field should be added.
     * @param parentRequiredFields The list of required fields for the parent schema.
     * @param currentScope         The current JSON scope path.
     */
    private void processSingleField(Field field, JsonSchema parentSchema, List<UiSchemaLayoutElement> parentUiElements, List<String> parentRequiredFields, String currentScope, Map<String, Map<String, Object>> dynamicMetadata, Class<?> rootDtoClass, Map<String, Object> fieldRules, String dynamicPropertyPrefix) {
        JsonFormField fieldAnnotation = field.getAnnotation(JsonFormField.class);
        JsonFormGroup groupAnnotation = field.getAnnotation(JsonFormGroup.class);
        JsonFormDynamicSection dynamicSectionAnnotation = field.getAnnotation(JsonFormDynamicSection.class);

        if (groupAnnotation != null) {
            String fieldName = field.getName();
            Class<?> nestedDtoClass = field.getType();
            JsonSchema groupSchema = new JsonSchema();
            groupSchema.setType("object");
            groupSchema.setTitle(groupAnnotation.label());
            List<String> groupRequiredFields = new ArrayList<>();
            parentSchema.getProperties().put(fieldName, groupSchema);
            UiSchemaGroupElement uiGroup = new UiSchemaGroupElement();
            uiGroup.setLabel(groupAnnotation.label());
            parentUiElements.add(uiGroup);
            JsonForm nestedClassAnnotation = nestedDtoClass.getAnnotation(JsonForm.class);
            // Pass the rootDtoClass, fieldRules, and prefix down the recursion.
            processClassFields(nestedDtoClass, nestedClassAnnotation, uiGroup.getElements(), groupRequiredFields, currentScope + fieldName + "/properties/", groupSchema, dynamicMetadata, rootDtoClass, fieldRules, dynamicPropertyPrefix);
            if (!groupRequiredFields.isEmpty()) {
                groupSchema.setRequired(groupRequiredFields);
            }
        } else if (fieldAnnotation != null) {
            String fieldName = field.getName();
            SchemaProperty schemaProperty = createSchemaProperty(fieldAnnotation, field.getType());
            parentSchema.getProperties().put(fieldName, schemaProperty);
            if (fieldAnnotation.required()) {
                parentRequiredFields.add(fieldName);
            }
            UiSchemaElement uiElement = createUiElement(fieldAnnotation, field, currentScope + fieldName, rootDtoClass);
            parentUiElements.add(uiElement);

            // --- NEW LOGIC FOR DYNAMIC RULES ---
            // Check if a rule was passed down for this specific field.
            if (fieldRules != null && fieldRules.containsKey(field.getName())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> ruleData = (Map<String, Object>) fieldRules.get(field.getName());
                Rule rule = buildDynamicRule(ruleData, currentScope, dynamicPropertyPrefix, rootDtoClass);
                if (rule != null) {
                    uiElement.setRule(rule);
                }
            }

        } else if (dynamicSectionAnnotation != null) {
            processDynamicSection(field, parentSchema, parentUiElements, parentRequiredFields, currentScope, dynamicMetadata, rootDtoClass);
        }
    }

    /**
     * Handles the generation of a dynamic form section based on the @JsonFormDynamicSection annotation.
     */
    private void processDynamicSection(Field field, JsonSchema parentSchema, List<UiSchemaLayoutElement> parentUiElements, List<String> parentRequiredFields, String currentScope, Map<String, Map<String, Object>> dynamicMetadata, Class<?> rootDtoClass) {
        JsonFormDynamicSection dynamicSectionAnnotation = field.getAnnotation(JsonFormDynamicSection.class);
        String placeholderFieldName = field.getName();
        Map<String, Object> sectionData = dynamicMetadata.get(placeholderFieldName);
        if (sectionData == null || !(sectionData.get("data") instanceof List)) return;

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) sectionData.get("data");
        Class<?> itemDto = dynamicSectionAnnotation.itemDto();

        for (Map<String, Object> itemData : items) {
            String key = String.valueOf(itemData.get(dynamicSectionAnnotation.propertyKeyField()));
            String label = String.valueOf(itemData.get(dynamicSectionAnnotation.labelField()));
            String dynamicItemKey = dynamicSectionAnnotation.propertyKeyPrefix() + key;

            // Check for field-specific rules within this item's data.
            @SuppressWarnings("unchecked")
            Map<String, Object> fieldRules = (Map<String, Object>) itemData.get("fieldRules");

            JsonSchema itemSchema = new JsonSchema();
            itemSchema.setType("object");
            itemSchema.setTitle(label);
            parentSchema.getProperties().put(dynamicItemKey, itemSchema);

            UiSchemaGroupElement itemGroup = new UiSchemaGroupElement();
            itemGroup.setLabel(label);
            parentUiElements.add(itemGroup);

            JsonForm itemDtoAnnotation = itemDto.getAnnotation(JsonForm.class);
            String newScope = currentScope + dynamicItemKey + "/properties/";
            List<String> itemRequiredFields = new ArrayList<>();

            // Pass the item-specific rules and prefix down the recursive call.
            processClassFields(itemDto, itemDtoAnnotation, itemGroup.getElements(), itemRequiredFields, newScope, itemSchema, dynamicMetadata, rootDtoClass, fieldRules, dynamicItemKey + "_");

            if (!itemRequiredFields.isEmpty()) {
                itemSchema.setRequired(itemRequiredFields);
            }
        }
    }

    /**
     * Creates a {@link SchemaProperty} object based on a field's @JsonFormField annotation and Java type.
     *
     * @param annotation The @JsonFormField annotation on the field.
     * @param fieldType  The Java {@link Class} type of the field.
     * @return A configured {@link SchemaProperty} instance.
     */
    private SchemaProperty createSchemaProperty(JsonFormField annotation, Class<?> fieldType) {
        SchemaProperty schemaProperty = new SchemaProperty();
        schemaProperty.setType(mapJavaTypeToJsonType(fieldType));
        schemaProperty.setTitle(annotation.label());
        schemaProperty.setDescription(annotation.description().isBlank() ? null : annotation.description());
        schemaProperty.setFormat(annotation.format().isBlank() ? null : annotation.format());
        if (annotation.minLength() > -1) schemaProperty.setMinLength(annotation.minLength());
        if (annotation.maxLength() > -1) schemaProperty.setMaxLength(annotation.maxLength());
        return schemaProperty;
    }

    /**
     * Creates a {@link UiSchemaElement} (a "Control") based on a field's annotations.
     *
     * @param fieldAnnotation The @JsonFormField annotation on the field.
     * @param field           The {@link Field} object itself.
     * @param scope           The JSON scope path for this control (e.g., "#/properties/firstName").
     * @return A configured {@link UiSchemaElement} instance.
     */
    private UiSchemaElement createUiElement(JsonFormField fieldAnnotation, Field field, String scope, Class<?> rootDtoClass) {
        UiSchemaElement uiElement = new UiSchemaElement();
        uiElement.setScope(scope);
        uiElement.setLabel(fieldAnnotation.label());

        if (fieldAnnotation.controlType() == UiControlType.TEXT_AREA) {
            uiElement.setOptions(Map.of("multi", true));
        }
        uiElement.setOptionsFromString(fieldAnnotation.options());

        JsonFormRule ruleAnnotation = field.getAnnotation(JsonFormRule.class);
        if (ruleAnnotation != null) {
            Rule rule = new Rule();
            rule.setEffect(ruleAnnotation.effect());
            Condition condition = new Condition();

            // Fix: Check if the condition field is on the root or nested.
            // This logic is simplified; a real implementation would need to know the 'level' of the DTO.
            // For now, assume condition fields are on the root DTO for static rules.
            String conditionScope = "#/properties/" + ruleAnnotation.conditionField();
            condition.setScope(conditionScope);

            Object parsedValue = parseExpectedValue(ruleAnnotation.expectedValue(), rootDtoClass, ruleAnnotation.conditionField());
            condition.setSchema(new ConditionSchema(parsedValue));
            rule.setCondition(condition);
            uiElement.setRule(rule);
        }
        return uiElement;
    }

    /**
     * Applies dynamic metadata to the already generated schema and UI schema. This is used
     * to inject runtime values, such as enum lists from a database.
     *
     * @param schema          The generated {@link JsonSchema}.
     * @param uiSchema        The generated {@link UiSchema}.
     * @param dynamicMetadata The map of dynamic data to apply.
     */
    private void applyDynamicMetadata(JsonSchema schema, UiSchema uiSchema, Map<String, Map<String, Object>> dynamicMetadata, Class<?> rootDtoClass) {
        dynamicMetadata.forEach((fieldName, properties) -> {
            SchemaNode schemaNode = schema.getProperties().get(fieldName);
            if (schemaNode == null || !(schemaNode instanceof SchemaProperty)) {
                return;
            }
            SchemaProperty schemaProperty = (SchemaProperty) schemaNode;
            properties.forEach((key, value) -> {
                switch (key) {
                    case "enum":
                        if (value instanceof List) {
                            schemaProperty.setEnumValues((List<String>) value);
                        }
                        break;
                    case "label":
                        findUiElement(uiSchema.getElements(), "#/properties/" + fieldName).ifPresent(el -> el.setLabel(String.valueOf(value)));
                        schemaProperty.setTitle(String.valueOf(value));
                        break;
                    case "rule":
                        if (value instanceof Map) {
                            Map<String, Object> ruleData = (Map<String, Object>) value;
                            findUiElement(uiSchema.getElements(), "#/properties/" + fieldName).ifPresent(uiElement -> {
                                Rule rule = buildDynamicRule(ruleData, "#/properties/", null, rootDtoClass);
                                if (rule != null) {
                                    uiElement.setRule(rule);
                                }
                            });
                        }
                        break;
                }
            });
        });
    }

    private Rule buildDynamicRule(Map<String, Object> ruleData, String currentScope, String dynamicPropertyPrefix, Class<?> rootDtoClass) {
        String conditionField = (String) ruleData.get("conditionField");
        Object expectedValue = ruleData.get("expectedValue");
        Object effectObj = ruleData.get("effect");

        if (conditionField == null || expectedValue == null || effectObj == null) {
            return null;
        }

        Rule rule = new Rule();
        if (effectObj instanceof RuleEffect) {
            rule.setEffect((RuleEffect) effectObj);
        } else if (effectObj instanceof String) {
            rule.setEffect(RuleEffect.valueOf(((String) effectObj).toUpperCase()));
        }

        Condition condition = new Condition();
        // Construct the fully dynamic scope for the condition.
        // currentScope = #/properties/emp_101/properties/
        // dynamicPropertyPrefix = emp_101_
        // conditionField = lastName
        // We need to find the base scope: #/properties/
        String baseScope = currentScope.substring(0, currentScope.indexOf("/properties/") + "/properties/".length());

        // This assumes the condition field is within the same dynamic item.
        // A more complex implementation could check if the field is "global" (on root) or "local" (in the item).
        // For now, assume it's local to the item.
        String dynamicConditionScope = baseScope + dynamicPropertyPrefix + conditionField;
        condition.setScope(dynamicConditionScope);

        Object parsedValue = parseExpectedValue(String.valueOf(expectedValue), rootDtoClass, conditionField);
        condition.setSchema(new ConditionSchema(parsedValue));
        rule.setCondition(condition);
        return rule;
    }

    /**
     * Recursively searches the UI schema's element tree to find a "Control" element
     * with a specific scope.
     *
     * @param elements    The list of UI elements to search through.
     * @param scopeToFind The scope to match (e.g., "#/properties/firstName").
     * @return An {@link Optional} containing the found {@link UiSchemaElement}, or empty if not found.
     */
    private Optional<UiSchemaElement> findUiElement(List<UiSchemaLayoutElement> elements, String scopeToFind) {
        for (UiSchemaLayoutElement element : elements) {
            // Base case: The element is a Control. Check if its scope matches.
            if (element instanceof UiSchemaElement) {
                UiSchemaElement control = (UiSchemaElement) element;
                if (scopeToFind.equals(control.getScope())) {
                    return Optional.of(control);
                }
            }
            // Recursive step: The element is a layout container. Search its children.
            else if (element instanceof UiSchemaGroupElement) {
                Optional<UiSchemaElement> foundInGroup = findUiElement(((UiSchemaGroupElement) element).getElements(), scopeToFind);
                if (foundInGroup.isPresent()) return foundInGroup;
            } else if (element instanceof UiSchemaHorizontalLayoutElement) {
                Optional<UiSchemaElement> foundInLayout = findUiElement(((UiSchemaHorizontalLayoutElement) element).getElements(), scopeToFind);
                if (foundInLayout.isPresent()) return foundInLayout;
            } else if (element instanceof UiSchemaVerticalLayoutElement) {
                Optional<UiSchemaElement> foundInLayout = findUiElement(((UiSchemaVerticalLayoutElement) element).getElements(), scopeToFind);
                if (foundInLayout.isPresent()) return foundInLayout;
            }
        }
        return Optional.empty();
    }

    /**
     * Maps a Java {@link Class} type to its corresponding JSON schema type string.
     *
     * @param javaType The Java class.
     * @return The JSON schema type as a String (e.g., "string", "integer").
     */
    private String mapJavaTypeToJsonType(Class<?> javaType) {
        if (javaType == String.class) return "string";
        if (javaType == Integer.class || javaType == int.class || javaType == Long.class || javaType == long.class) return "integer";
        if (javaType == Double.class || javaType == double.class || javaType == Float.class || javaType == float.class) return "number";
        if (javaType == Boolean.class || javaType == boolean.class) return "boolean";
        if (javaType.isEnum()) return "string"; // Enums are represented as strings in JSON.
        return "object"; // Default for complex types.
    }

    /**
     * Parses the string 'expectedValue' from a @JsonFormRule annotation into its correct
     * primitive type (boolean, integer, etc.) by inspecting the type of the condition field.
     *
     * @param value        The string value from the annotation.
     * @param dtoClass     The class containing the field.
     * @param fieldName    The name of the field the rule depends on.
     * @return The parsed value as an {@link Object} (e.g., a Boolean, Integer, or String).
     */
    private Object parseExpectedValue(String value, Class<?> dtoClass, String fieldName) {
        try {
            // Use reflection to find the type of the field the condition is based on.
            Field conditionField = dtoClass.getDeclaredField(fieldName);
            Class<?> fieldType = conditionField.getType();

            // Parse the string value into the correct type.
            if (fieldType == boolean.class || fieldType == Boolean.class) return Boolean.parseBoolean(value);
            if (fieldType == int.class || fieldType == Integer.class) return Integer.parseInt(value);
            if (fieldType == long.class || fieldType == Long.class) return Long.parseLong(value);
            if (fieldType == double.class || fieldType == Double.class) return Double.parseDouble(value);
            // If it's not a recognized primitive, assume it's a string comparison.
            return value;
        } catch (NoSuchFieldException | SecurityException | NumberFormatException e) {
            // Log an error and default to a string comparison if reflection fails.
            System.err.println("Could not parse rule value for field '" + fieldName + "'. Defaulting to String type. Error: " + e.getMessage());
            return value;
        }
    }
}

