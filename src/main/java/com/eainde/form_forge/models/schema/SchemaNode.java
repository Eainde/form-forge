package com.eainde.form_forge.models.schema;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A marker interface to allow polymorphism in the JSON schema's 'properties' map.
 * Both SchemaProperty (for simple fields) and JsonSchema (for nested objects) will implement this.
 * The JsonTypeInfo annotation helps Jackson serialize the objects correctly based on their type.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public interface SchemaNode {
}
