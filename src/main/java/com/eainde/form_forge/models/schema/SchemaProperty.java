package com.eainde.form_forge.models.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Represents a simple property within a JSON Schema (e.g., string, number, boolean).
 * Implements SchemaNode to be part of the polymorphic properties map.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaProperty implements SchemaNode {
    private String type;
    private String title;
    private String description;
    private String format;
    private Integer minLength;
    private Integer maxLength;
    @JsonProperty("enum")
    private List<String> enumValues;
}

