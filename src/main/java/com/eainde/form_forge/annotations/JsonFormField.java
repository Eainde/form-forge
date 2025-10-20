package com.eainde.form_forge.annotations;


import com.eainde.form_forge.models.uischema.UiControlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field within a class to be included in the JSON Form.
 * This annotation contains metadata for both the JSON Schema and the UI Schema.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonFormField {

    // --- Common Properties ---
    /**
     * The display label for the form field in the UI.
     */
    String label() default "";

    /**
     * A description or helper text for the field.
     */
    String description() default "";

    /**
     * Whether this field is mandatory. Maps to the 'required' array in JSON Schema.
     */
    boolean required() default false;

    // --- UI Schema Properties ---
    /**
     * The type of UI control to render for this field (e.g., text input, text area, date picker).
     * Defaults to a standard text input.
     */
    UiControlType controlType() default UiControlType.CONTROL;

    /**
     * A string to define custom options for the control element in the UI Schema.
     * Example: '{"multi": true}' for a multi-line text area.
     * Note: This is advanced usage. For simple cases, use dedicated properties.
     */
    String options() default "";


    // --- JSON Schema Validation Properties ---
    /**
     * The format of the data (e.g., "email", "date-time", "uri").
     */
    String format() default "";

    /**
     * Minimum length for a string field.
     */
    int minLength() default -1;

    /**
     * Maximum length for a string field.
     */
    int maxLength() default -1;

    /**
     * A regex pattern for string validation.
     */
    String pattern() default "";
}

