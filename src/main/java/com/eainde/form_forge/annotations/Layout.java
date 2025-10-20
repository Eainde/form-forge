package com.eainde.form_forge.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A container for one or more @LayoutItem annotations. This is used to define a
 * custom UI layout for the fields within a DTO.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    /**
     * An array of LayoutItem definitions that make up the custom layout.
     */
    LayoutItem[] value();
}
