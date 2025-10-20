package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import java.util.Map;

/**
 * Represents a single element in the UI Schema's 'elements' array.
 * This is typically a "Control" element.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UiSchemaElement extends UiSchemaLayoutElement{
    private String scope;
    private String label;
    private Map<String, Object> options;
    private Rule rule; // <-- ADDED THIS LINE

    /**
     * Helper method to parse a JSON string into the options map.
     * @param optionsJson JSON string for options.
     */
    public void setOptionsFromString(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.options = mapper.readValue(optionsJson, Map.class);
        } catch (JsonProcessingException e) {
            // Log this error in a real application
            System.err.println("Failed to parse UI options JSON: " + optionsJson);
        }
    }
}


