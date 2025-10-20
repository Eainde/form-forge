package com.eainde.form_forge.models.uischema;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 'HorizontalLayout' element in the UI schema. It contains a list
 * of nested elements to be rendered horizontally.
 */
public class UiSchemaHorizontalLayoutElement extends UiSchemaLayoutElement {
    private List<UiSchemaLayoutElement> elements = new ArrayList<>();

    public UiSchemaHorizontalLayoutElement() {
        this.type = "HorizontalLayout";
    }

    public List<UiSchemaLayoutElement> getElements() {
        return elements;
    }

    public void setElements(List<UiSchemaLayoutElement> elements) {
        this.elements = elements;
    }
}
