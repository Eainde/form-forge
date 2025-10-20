package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the possible effects of a UI Schema rule.
 */
public enum RuleEffect {
    SHOW("SHOW"),
    HIDE("HIDE"),
    ENABLE("ENABLE"),
    DISABLE("DISABLE");

    private final String value;

    RuleEffect(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
