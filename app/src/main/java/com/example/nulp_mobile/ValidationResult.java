package com.example.nulp_mobile;

public enum ValidationResult {

    FINE("Fine"),
    THIS_FIELD_CANNOT_BE_EMPTY("This field cannot be empty"),
    PASSWORD_SHOUD_CONTAINT_AT_LEAST_8_CHARACTERS("Password should be at least 8 characters"),
    EMAIL_IS_NOT_VALID("Email is not valid"),
    PHONE_NUMBER_IS_NOT_VALID("Phone number is not valid");

    private final String message;

    ValidationResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
