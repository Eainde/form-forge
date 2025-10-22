package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.JsonForm;
import com.eainde.form_forge.annotations.JsonFormDynamicSection;
import com.eainde.form_forge.annotations.JsonFormField;
import lombok.Data;
import java.util.Map;

/**
 * This DTO demonstrates how to create a form with a dynamic section.
 */
@Data
@JsonForm(title = "Employee Training Completion")
public class EmployeeTrainingFormDto {

    @JsonFormField(label = "Course Name", required = true)
    private String courseName;

    /**
     * This annotation marks the 'employees' field as a placeholder for a dynamic
     * section. The generator will replace this with a list of fields based on
     * the data provided at runtime.
     */
    @JsonFormDynamicSection(
            itemDto = TrainingStatusDto.class,
            propertyKeyField = "employeeId",
            labelField = "employeeName",
            propertyKeyPrefix = "training_"
    )
    private Map<String, TrainingStatusDto> employees;
}
