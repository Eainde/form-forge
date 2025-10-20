package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.*;
import lombok.Data;

/**
 * A DTO to hold personal details. This will be used inside a group
 * with a custom horizontal layout.
 */
@Data
public class PersonalDetailsDto {

    @JsonFormField(label = "First Name", required = true)
    private String firstName;

    @JsonFormField(label = "Last Name", required = true)
    private String lastName;

    @JsonFormGroup(
            label = "Contact Info",
            layout = @Layout(
                    @LayoutItem(type = LayoutType.HORIZONTAL, fields = {"phoneNumber"})
            )
    )
    private ContactInfoDto contactInfoDto;
}