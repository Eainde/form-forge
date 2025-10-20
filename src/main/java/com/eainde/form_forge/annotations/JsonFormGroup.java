package com.eainde.form_forge.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field (which should be a nested object) as a UI Group in JSON Forms.
 * This will generate a 'Group' layout element containing the fields of the nested object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonFormGroup {
    /**
     * @return The mandatory label to be displayed for the group.
     */
    String label();

    /**
     * Defines a custom UI layout for the fields within this group.
     * If not specified, fields will be rendered in a default vertical layout.
     */
    Layout layout() default @Layout({});
}
