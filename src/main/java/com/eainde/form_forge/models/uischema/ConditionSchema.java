package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the 'schema' part of a Condition, which typically contains a 'const' value.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConditionSchema {
    @JsonProperty("const")
    private Object constantValue;
}
