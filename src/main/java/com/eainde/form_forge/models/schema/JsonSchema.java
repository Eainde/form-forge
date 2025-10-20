package com.eainde.form_forge.models.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the main JSON Schema structure.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonSchema implements SchemaNode{
    private String type = "object";
    private String title;
    private String description;
    private Map<String, SchemaNode> properties = new HashMap<>();
    private List<String> required;
}

