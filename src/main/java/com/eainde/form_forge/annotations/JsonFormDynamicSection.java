package com.eainde.form_forge.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a placeholder field to be replaced by a dynamically generated section of a form.
 * This is used for scenarios where form fields need to be created based on a list of data
 * known only at runtime (e.g., a list of employees, products, etc.).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonFormDynamicSection {

    /**
     * A reference to a DTO class that acts as a template for the form fields to be
     * generated for EACH item in the dynamic data list.
     */
    Class<?> itemDto();

    /**
     * The name of the key in the dynamic data map that provides the unique identifier
     * for each item. This is used to generate unique property names in the JSON schema.
     * Example: "employeeId"
     */
    String propertyKeyField();

    /**
     * The name of the key in the dynamic data map that provides the human-readable
     * label for each generated UI control.
     * Example: "employeeName"
     */
    String labelField();

    /**
     * An optional prefix to be added to the generated property keys in the JSON schema
     * to prevent potential naming conflicts with other fields.
     */
    String propertyKeyPrefix() default "";
}

