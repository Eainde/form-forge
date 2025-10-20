package com.eainde.form_forge.controller.dto;

import com.eainde.form_forge.annotations.*;
import com.eainde.form_forge.models.uischema.RuleEffect;
import com.eainde.form_forge.models.uischema.UiControlType;
import lombok.Data;
import java.util.Date;

/**
 * An example DTO showing how to use the annotations for fields, rules, and dynamic data.
 */
@Data
@JsonForm(
        title = "User Profile",
        description = "Form to collect user profile information."
)
public class UserProfileDto {

    /**
     * This annotation creates a group for personal details and uses the new @Layout
     * annotation to specify that 'firstName' and 'lastName' from the PersonalDetailsDto
     * should be rendered side-by-side in a horizontal layout.
     */
    @JsonFormGroup(
            label = "Personal Details",
            layout = @Layout(
                    @LayoutItem(type = LayoutType.HORIZONTAL, fields = {"firstName", "lastName", "contactInfoDto"})
            )
    )
    private PersonalDetailsDto personalDetails;

    @JsonFormField(label = "Email Address", format = "email", description = "Must be a valid email.")
    private String email;


    @JsonFormField(label = "Bio", controlType = UiControlType.TEXT_AREA, maxLength = 500)
    private String bio;

    @JsonFormField(label = "Date of Birth", format = "date")
    private Date dateOfBirth;

    @JsonFormField(label = "Is this a business account?")
    private boolean isBusinessAccount;

    // --- RULE EXAMPLE ---
    // This field will only be SHOWN if the 'isBusinessAccount' field is set to 'true'.
    // The library automatically detects this rule annotation.
    /*@JsonFormRule(
            effect = RuleEffect.SHOW,
            conditionField = "isBusinessAccount",
            expectedValue = "true"
    )*/
    @JsonFormField(label = "Company Name")
    private String companyName;

    @JsonFormField(label = "Is Active")
    private boolean active;

    // --- DYNAMIC DATA EXAMPLE ---
    // This field's label and dropdown options will be provided dynamically from the controller.
    @JsonFormField(label = "Country") // A static label can be provided as a fallback.
    private String country;
}
