package com.mms.models;

/**
 * Base abstract class representing a user in the system.
 * Defines common attributes and polymorphic behavior.
 */
public abstract class User {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private UserRole role;

    public enum UserRole {
        ADMIN, CLINICIAN, PATIENT
    }

    public User(String id, String name, String phone, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Polymorphic method to be overridden by subclasses.
     * Each user type displays its own menu.
     */
    public abstract void showMenu();

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
