package com.georgeradu.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class LoginRequest {
    @NotBlank(message = "Email cannot be empty")
    @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "LoginRequest{" + "email='" + email + '\'' + ", password='" + password + '\'' + '}';
    }
}
