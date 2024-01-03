package com.georgeradu.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialCharacter;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireLowercase = constraintAnnotation.requireLowercase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialCharacter = constraintAnnotation.requireSpecialCharacter();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        if (value.length() < minLength) {
            return false;
        }

        if (value.length() > maxLength) {
            return false;
        }

        if (requireUppercase && !containsUppercase(value)) {
            return false;
        }

        if (requireLowercase && !containsLowercase(value)) {
            return false;
        }

        if (requireDigit && !containsDigit(value)) {
            return false;
        }

        if (requireSpecialCharacter && !containsSpecialCharacter(value)) {
            return false;
        }

        return true;
    }

    private boolean containsUppercase(String value) {
        return !value.equals(value.toLowerCase());
    }

    private boolean containsLowercase(String value) {
        return !value.equals(value.toUpperCase());
    }

    private boolean containsDigit(String value) {
        return value.matches(".*\\d.*");
    }

    private boolean containsSpecialCharacter(String value) {
        String specialCharacters = "[!@#$%^&*(),.?\":{}|<>]";
        return value.matches(".*" + specialCharacters + ".*");
    }
}