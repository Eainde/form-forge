package com.eainde.form_forge.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field representing a nested DTO whose child fields should be rendered
 * within a HorizontalLayout in the JSON Form. This is a UI-only construct
 * and does not create a nested object in the data schema.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonFormHorizontalLayout {
}
