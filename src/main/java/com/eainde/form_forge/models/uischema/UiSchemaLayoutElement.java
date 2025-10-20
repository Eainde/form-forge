package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A base class for all UI schema elements (e.g., Control, Group).
 * This allows for polymorphism when serializing the 'elements' array.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UiSchemaElement.class, name = "Control"),
        @JsonSubTypes.Type(value = UiSchemaGroupElement.class, name = "Group"),
        @JsonSubTypes.Type(value = UiSchemaHorizontalLayoutElement.class, name = "HorizontalLayout"),
        @JsonSubTypes.Type(value = UiSchemaVerticalLayoutElement.class, name = "VerticalLayout")
})
public abstract class UiSchemaLayoutElement {
    protected String type;

    /**
     * This getter is ignored for JSON serialization to prevent duplication.
     * The @JsonTypeInfo annotation is solely responsible for writing the 'type' field.
     * @return The element type (e.g., "Control", "Group").
     */
    @JsonIgnore
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
