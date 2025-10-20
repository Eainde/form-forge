package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UiControlType {
    CONTROL("Control"),
    TEXT_AREA("Control"), // Special case, handled by options
    DATE_PICKER("Control"), // Special case, handled by format
    VERTICAL_LAYOUT("VerticalLayout"),
    HORIZONTAL_LAYOUT("HorizontalLayout");

    private final String typeValue;

    UiControlType(String typeValue) {
        this.typeValue = typeValue;
    }

    @JsonValue
    public String getTypeValue() {
        return typeValue;
    }
}
