package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Represents a Rule object in the UI Schema.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {
    private RuleEffect effect;
    private Condition condition;
}
