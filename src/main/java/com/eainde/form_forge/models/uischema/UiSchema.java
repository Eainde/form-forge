package com.eainde.form_forge.models.uischema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main UI Schema structure.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UiSchema {
    private UiControlType type = UiControlType.VERTICAL_LAYOUT;
    private List<UiSchemaLayoutElement> elements = new ArrayList<>();
}
