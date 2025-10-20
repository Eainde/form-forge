package com.eainde.form_forge.models;

import com.eainde.form_forge.models.schema.JsonSchema;
import com.eainde.form_forge.models.uischema.UiSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The final response object containing both the JSON Schema and UI Schema.
 * This object can be directly serialized to JSON to be consumed by the frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonFormResponse {
    private JsonSchema schema;
    private UiSchema uischema;
}
