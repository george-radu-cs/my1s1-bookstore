package com.georgeradu.bookstore.dto;


import com.georgeradu.bookstore.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class RegisterRequest {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters")
    private String lastName;
    @NotBlank(message = "Email cannot be empty")
    @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    @Password
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        RegisterRequest that = (RegisterRequest) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) &&
               Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return "RegisterRequest{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" +
               email + '\'' + ", password='" + password + '\'' + '}';
    }
}
