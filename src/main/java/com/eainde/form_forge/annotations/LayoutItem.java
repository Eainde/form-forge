package com.eainde.form_forge.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents a single layout container (e.g., a HorizontalLayout) within a larger layout.
 * It specifies the type of layout and the fields that should be included within it.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutItem {
    /**
     * The type of layout to create (HORIZONTAL or VERTICAL).
     */
    LayoutType type() default LayoutType.VERTICAL;

    /**
     * An array of field names (strings) from the DTO that should be
     * placed inside this layout element.
     */
    String[] fields();
}
