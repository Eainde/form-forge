package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.JsonFormField;
import lombok.Data;

/**
 * This DTO acts as a TEMPLATE for the dynamic section.
 * It defines that for each employee, we want to generate one boolean field.
 */
@Data
public class TrainingStatusDto {

    // The label here ("Completed") is for the checkbox itself, but it will
    // be overridden by the dynamic label (the employee's name) in the generator.
    @JsonFormField(label = "Completed", required = true)
    private boolean completed;
}
