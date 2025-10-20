package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Represents the Condition part of a Rule.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Condition {
    private String type = "LEAF";
    private String scope;
    private ConditionSchema schema;
}
