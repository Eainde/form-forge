# JSON Forms Generator Library

This library provides a powerful, annotation-based mechanism for generating JSON schemas and UI schemas compatible with the [JSON Forms](https://jsonforms.io/) library.

It is designed to be used within a Java (and Spring Boot) environment, allowing developers to define form structures directly on their DTOs or entities while retaining the flexibility to inject dynamic metadata (e.g., from a database) at runtime.

## Core Features

- **Annotation-Driven:** Define your form's structure, layout, and validation using simple annotations on your Java classes.
- **Dynamic Metadata:** Easily override or supplement form properties like labels, descriptions, validation rules, and dropdown options with data fetched from a database or any other external source.
- **Type-Safe Models:** Uses POJOs to build the schema structures, preventing common errors and making the library easier to extend.
- **Spring Boot Friendly:** Designed as a Spring `@Component` for easy injection and use in your services.

## How to Use

### 1. Annotate Your Data Class

Add annotations to the class and fields you want to appear in the form.

```java
// Example: UserProfile.java
import com.yourcompany.jsonform.library.annotations.*;
import com.yourcompany.jsonform.library.models.uischema.UiControlType;

@JsonForm(
    title = "User Profile",
    description = "Form to collect user profile information."
)
public class UserProfile {

    @JsonFormField(label = "Full Name", required = true, minLength = 3)
    private String name;

    @JsonFormField(label = "Email Address", format = "email")
    private String email;

    @JsonFormField(label = "Bio", controlType = UiControlType.TEXT_AREA)
    private String bio;
    
    // This field's options will be provided dynamically
    @JsonFormField(label = "Country")
    private String country;
}
```

### 2. Generate the Form JSON

Inject the `JsonFormGenerator` service and call the `generate` method.

- **`Class<?> targetClass`**: The annotated class (`UserProfile.class`).
- **`Map<String, Map<String, Object>> dynamicMetadata`**: A map containing dynamic values. The outer key is the field name (e.g., "country"), and the inner map contains the properties to set (e.g., "enum", "enumNames").

```java
// Example: In a Spring Controller or Service

@Autowired
private JsonFormGenerator jsonFormGenerator;

public JsonFormResponse createProfileForm() {
    // 1. Fetch dynamic data from your database/service
    Map<String, Map<String, Object>> dynamicData = new HashMap<>();

    // For the 'country' field, provide dropdown options
    Map<String, Object> countryMetadata = new HashMap<>();
    countryMetadata.put("enum", List.of("US", "CA", "UK"));
    countryMetadata.put("enumNames", List.of("United States", "Canada", "United Kingdom"));
    dynamicData.put("country", countryMetadata);

    // 2. Generate the JSON Form structure
    return jsonFormGenerator.generate(UserProfile.class, dynamicData);
}
```

### 3. Send to Frontend

The returned `JsonFormResponse` object can be directly serialized to JSON and sent to your frontend application where the JSON Forms renderer will use it.

## Library Components

- **`annotations`**: Contains all custom annotations (`@JsonForm`, `@JsonFormField`).
- **`models`**: Contains POJOs representing the `schema` and `uischema` structures.
- **`service`**: The core `JsonFormGenerator` service that performs the generation logic.
