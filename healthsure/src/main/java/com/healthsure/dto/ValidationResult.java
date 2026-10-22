/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.healthsure.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class ValidationResult {
    private boolean valid;
    private List<String> errors;
    private List<String> warnings;

    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public static ValidationResult success() {
        return ValidationResult.builder().valid(true).build();
    }

    public static ValidationResult failure(String error) {
        ValidationResult result = new ValidationResult();
        result.setValid(false);
        result.getErrors().add(error);
        return result;
    }

    @Generated
    private static List<String> $default$errors() {
        return new ArrayList<String>();
    }

    @Generated
    private static List<String> $default$warnings() {
        return new ArrayList<String>();
    }

    @Generated
    public static ValidationResultBuilder builder() {
        return new ValidationResultBuilder();
    }

    @Generated
    public boolean isValid() {
        return this.valid;
    }

    @Generated
    public List<String> getErrors() {
        return this.errors;
    }

    @Generated
    public List<String> getWarnings() {
        return this.warnings;
    }

    @Generated
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Generated
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Generated
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    @Generated
    public ValidationResult() {
        this.errors = ValidationResult.$default$errors();
        this.warnings = ValidationResult.$default$warnings();
    }

    @Generated
    public ValidationResult(boolean valid, List<String> errors, List<String> warnings) {
        this.valid = valid;
        this.errors = errors;
        this.warnings = warnings;
    }

    @Generated
    public static class ValidationResultBuilder {
        @Generated
        private boolean valid;
        @Generated
        private boolean errors$set;
        @Generated
        private List<String> errors$value;
        @Generated
        private boolean warnings$set;
        @Generated
        private List<String> warnings$value;

        @Generated
        ValidationResultBuilder() {
        }

        @Generated
        public ValidationResultBuilder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        @Generated
        public ValidationResultBuilder errors(List<String> errors) {
            this.errors$value = errors;
            this.errors$set = true;
            return this;
        }

        @Generated
        public ValidationResultBuilder warnings(List<String> warnings) {
            this.warnings$value = warnings;
            this.warnings$set = true;
            return this;
        }

        @Generated
        public ValidationResult build() {
            List<String> errors$value = this.errors$value;
            if (!this.errors$set) {
                errors$value = ValidationResult.$default$errors();
            }
            List<String> warnings$value = this.warnings$value;
            if (!this.warnings$set) {
                warnings$value = ValidationResult.$default$warnings();
            }
            return new ValidationResult(this.valid, errors$value, warnings$value);
        }

        @Generated
        public String toString() {
            return "ValidationResult.ValidationResultBuilder(valid=" + this.valid + ", errors$value=" + String.valueOf(this.errors$value) + ", warnings$value=" + String.valueOf(this.warnings$value) + ")";
        }
    }
}
