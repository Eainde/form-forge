package com.eainde.form_forge.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a source for a JSON Form.
 * This annotation is placed at the class level to provide overall form details.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsonForm {
    /**
     * The title of the form, often used in the schema's title property.
     */
    String title() default "";

    /**
     * A description for the form, often used in the schema's description property.
     */
    String description() default "";

    /**
     * Defines a custom UI layout for the fields in this form.
     * If not specified, fields will be rendered in a default vertical layout.
     */
    Layout layout() default @Layout({});
}
