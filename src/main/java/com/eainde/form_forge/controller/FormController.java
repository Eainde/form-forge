package com.eainde.form_forge.controller;

import com.eainde.form_forge.controller.dto.EmployeeFormDto;
import com.eainde.form_forge.controller.dto.EmployeeTrainingFormDto;
import com.eainde.form_forge.controller.dto.UserProfileDto;
import com.eainde.form_forge.models.JsonFormResponse;
import com.eainde.form_forge.service.JsonFormGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping("/employee-training")
    public JsonFormResponse getEmployeeTrainingForm() {
        // 1. In a real application, you would fetch this from a database.
        List<Map<String, Object>> employeeList = List.of(
                Map.of("employeeId", "emp_123", "employeeName", "Alice Smith"),
                Map.of("employeeId", "emp_456", "employeeName", "Bob Johnson"),
                Map.of("employeeId", "emp_789", "employeeName", "Charlie Brown")
        );

        // 2. Prepare the dynamic data for the generator.
        Map<String, Map<String, Object>> dynamicMetadata = new HashMap<>();
        // The key "employees" must match the placeholder field name in EmployeeTrainingFormDto.
        dynamicMetadata.put("employees", Map.of("data", employeeList));

        // 3. Generate the form.
        return jsonFormGenerator.generate(EmployeeTrainingFormDto.class, dynamicMetadata);
    }

    /**
     * New endpoint to demonstrate a dynamic section with nested groups.
     */
    @GetMapping("/employee-details")
    public JsonFormResponse getEmployeeDetailsForm() {
        // 1. Fetch employee data.
        List<Map<String, Object>> employeeList = List.of(
                Map.of("employeeId", "101", "employeeName", "Alice Smith"),
                Map.of("employeeId", "102", "employeeName", "Bob Johnson")
        );

        // 2. Prepare the dynamic data.
        Map<String, Map<String, Object>> dynamicMetadata = new HashMap<>();
        // The key "employees" matches the placeholder field name in EmployeeFormDto.
        dynamicMetadata.put("employees", Map.of("data", employeeList));

        // 3. Generate the form.
        return jsonFormGenerator.generate(EmployeeFormDto.class, dynamicMetadata);
    }

    /**
     * New endpoint to demonstrate a dynamic section with nested groups.
     */
    @GetMapping("/employee-details-with-rules")
    public JsonFormResponse getEmployeeDetailsFormWithRules() {
        // 1. Fetch employee data.
        /*List<Map<String, Object>> employeeList = List.of(
                Map.of("employeeId", "101", "employeeName", "Alice Smith"),
                Map.of("employeeId", "102", "employeeName", "Bob Johnson")
        );*/

        // 2. Prepare the dynamic data.
        Map<String, Map<String, Object>> dynamicMetadata = new HashMap<>();
        // In your FormController...

        List<Map<String, Object>> employeeList = new ArrayList<>();

// Employee 1: No special rules
        Map<String, Object> alice = new HashMap<>();
        alice.put("employeeId", "101");
        alice.put("employeeName", "Alice Smith");
        employeeList.add(alice);

// Employee 2: Has a special rule on one of their fields
        Map<String, Object> bob = new HashMap<>();
        bob.put("employeeId", "102");
        bob.put("employeeName", "Bob Johnson");

// --- Define the rule for Bob ---
        Map<String, Object> bobsRules = new HashMap<>();
        Map<String, Object> emailRule = new HashMap<>();
        emailRule.put("effect", "SHOW");
// The conditionField is also relative to the template DTO
        emailRule.put("conditionField", "lastName");
        emailRule.put("expectedValue", "Johnson");

// Attach the rule to the "email" field specifically for Bob
        bobsRules.put("email", emailRule);

// Add the rules map to Bob's data
        bob.put("fieldRules", bobsRules);
        employeeList.add(bob);

// ... prepare dynamicMetadata as before ...
        dynamicMetadata.put("employees", Map.of("data", employeeList));

        // 3. Generate the form.
        return jsonFormGenerator.generate(EmployeeFormDto.class, dynamicMetadata);
    }
}


