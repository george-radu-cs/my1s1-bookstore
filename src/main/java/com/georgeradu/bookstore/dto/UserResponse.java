package com.georgeradu.bookstore.dto;

import com.georgeradu.bookstore.model.User;
import com.georgeradu.bookstore.model.UserRole;

import java.util.Objects;

public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    public UserResponse() {
    }

    public UserResponse(
            Long id, String firstName, String lastName, String email, UserRole role
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public UserResponse(User response) {
        this.id = response.getId();
        this.firstName = response.getFirstName();
        this.lastName = response.getLastName();
        this.email = response.getEmail();
        this.role = response.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, role);
    }

    @Override
    public String toString() {
        return "UserResponse{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' + ", role=" + role + '}';
    }
}
