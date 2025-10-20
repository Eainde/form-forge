package com.eainde.form_forge.models.uischema;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 'Group' layout element in the UI schema.
 */
public class UiSchemaGroupElement extends UiSchemaLayoutElement {
    private String label;
    private List<UiSchemaLayoutElement> elements = new ArrayList<>();

    public UiSchemaGroupElement() {
        super.setType("Group");
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<UiSchemaLayoutElement> getElements() {
        return elements;
    }

    public void setElements(List<UiSchemaLayoutElement> elements) {
        this.elements = elements;
    }
}
