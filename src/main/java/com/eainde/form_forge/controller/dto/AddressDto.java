package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.JsonFormField;
import lombok.Data;

/**
 * A simple DTO representing an address, to be used as a nested object for grouping.
 */
@Data
public class AddressDto {

    @JsonFormField(label = "Street Address", required = true)
    private String street;

    @JsonFormField(label = "City")
    private String city;

    @JsonFormField(label = "Postal Code")
    private String postalCode;
}
