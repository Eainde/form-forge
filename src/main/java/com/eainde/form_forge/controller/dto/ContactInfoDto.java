package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.*;
import lombok.Data;

/**
 * An intermediate DTO to demonstrate nested grouping.
 * It contains a basic field and a nested AddressDto group.
 */
@Data
public class ContactInfoDto {

    @JsonFormField(label = "Phone Number")
    private String phoneNumber;

    /**
     * This annotation creates the inner group ("Mailing Address")
     * inside the parent "Contact Information" group.
     */
    @JsonFormGroup(label = "Mailing Address",
            layout = @Layout(
                    @LayoutItem(type = LayoutType.HORIZONTAL, fields = {"street", "city", "postalCode"})
            ))
    private AddressDto address;
}
