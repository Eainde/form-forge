package com.eainde.form_forge.controller;

import com.eainde.form_forge.controller.dto.UserProfileDto;
import com.eainde.form_forge.models.JsonFormResponse;
import com.eainde.form_forge.service.JsonFormGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private JsonFormGenerator jsonFormGenerator;

    @GetMapping("/user-profile")
    public JsonFormResponse getUserProfileForm() {
        // 1. Fetch dynamic metadata from a database or another service.
        Map<String, Map<String, Object>> dynamicData = getDynamicDataForUserProfile();

        // 2. Generate the form.
        return jsonFormGenerator.generate(UserProfileDto.class, dynamicData);
    }

    /**
     * Mocks a call to a service that fetches dynamic form data from a database.
     * This method prepares the data in the format required by the JsonFormGenerator.
     *
     * @return A map where the key is the field name and the value is a map of its dynamic properties.
     */
    private Map<String, Map<String, Object>> getDynamicDataForUserProfile() {
        Map<String, Map<String, Object>> allDynamicData = new HashMap<>();

        Map<String, Object> countryMetadata = new HashMap<>();
        countryMetadata.put("enum", List.of("US", "CA", "UK", "DE"));
        countryMetadata.put("enumNames", List.of("United States", "Canada", "United Kingdom", "Germany"));
        countryMetadata.put("label", "Country of Residence");
        allDynamicData.put("country", countryMetadata);

        // 2. Dynamic Rule for the 'bio' field
        // This rule will make the 'bio' field appear only when 'Is Active' is checked.
        Map<String, Object> bioProperties = new HashMap<>();
        Map<String, Object> ruleData = new HashMap<>();
        ruleData.put("effect", "SHOW");
        ruleData.put("conditionField", "active");
        ruleData.put("expectedValue", true);

        bioProperties.put("rule", ruleData);
        allDynamicData.put("bio", bioProperties);

        return allDynamicData;
    }
}


