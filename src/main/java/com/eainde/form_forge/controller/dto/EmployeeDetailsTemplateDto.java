package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.*;
import lombok.Data;

@Data
@JsonForm(
        layout = @Layout({
                // Item 1: A horizontal layout for the name fields
                @LayoutItem(type = LayoutType.HORIZONTAL, fields = {"firstName", "lastName"}),

                // Item 2: A reference to the nested group field
                @LayoutItem(type = LayoutType.HORIZONTAL, fields = {"contactInfo"})
        })
)
public class EmployeeDetailsTemplateDto {

    @JsonFormField(label = "First Name", required = true)
    private String firstName;

    @JsonFormField(label = "Last Name", required = true)
    private String lastName;

    // This annotation tells the generator to create a nested group
    // for the ContactInfoTemplateDto.
    @JsonFormGroup(label = "Contact Info")
    private ContactInfoTemplateDto contactInfo;
}
