package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.JsonFormField;
import lombok.Data;

@Data
public class ContactInfoTemplateDto {

    @JsonFormField(label = "Email Address", format = "email")
    private String email;

    @JsonFormField(label = "Phone Number")
    private String phoneNumber;
}
