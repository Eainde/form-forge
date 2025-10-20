package com.eainde.form_forge.annotations;

import com.eainde.form_forge.models.uischema.RuleEffect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a rule for a UI schema element, allowing for conditional
 * showing, hiding, enabling, or disabling of a form field.
 * This should be placed on a field that also has @JsonFormField.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonFormRule {

    /**
     * The effect to apply when the condition is met (e.g., SHOW, HIDE).
     */
    RuleEffect effect();

    /**
     * The name of the field in the same class whose value will be checked.
     */
    String conditionField();

    /**
     * The expected value of the conditionField for the rule to be active.
     * The value will be parsed based on the type of the conditionField (e.g., "true" for boolean, "123" for integer).
     */
    String expectedValue();
}
