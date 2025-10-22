package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.JsonForm;
import com.eainde.form_forge.annotations.JsonFormDynamicSection;
import com.eainde.form_forge.annotations.JsonFormField;
import lombok.Data;
import java.util.Map;

/**
 * This is the main form DTO that your controller will use. It contains the
 * @JsonFormDynamicSection annotation which points to the EmployeeDetailsTemplateDto
 * as the template for each dynamically generated item.
 */
@Data
@JsonForm(title = "Employee Details Form")
public class EmployeeFormDto {

    @JsonFormField(label = "Department Name", required = true)
    private String departmentName;

    /**
     * This annotation points to the EmployeeDetailsTemplateDto. The generator will
     * now use the structure defined in that class (including its nested
     * ContactInfoTemplateDto group) to build the UI for each employee.
     */
    @JsonFormDynamicSection(
            itemDto = EmployeeDetailsTemplateDto.class,
            propertyKeyField = "employeeId",
            labelField = "employeeName",
            propertyKeyPrefix = "emp_"
    )
    private Map<String, EmployeeDetailsTemplateDto> employees;
}
