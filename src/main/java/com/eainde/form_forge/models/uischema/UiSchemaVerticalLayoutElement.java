package com.eainde.form_forge.models.uischema;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 'VerticalLayout' element in the UI schema. It contains a list
 * of nested elements to be rendered vertically.
 */
public class UiSchemaVerticalLayoutElement extends UiSchemaLayoutElement {
    private List<UiSchemaLayoutElement> elements = new ArrayList<>();

    public UiSchemaVerticalLayoutElement() {
        this.type = "VerticalLayout";
    }

    public List<UiSchemaLayoutElement> getElements() {
        return elements;
    }

    public void setElements(List<UiSchemaLayoutElement> elements) {
        this.elements = elements;
    }
}
