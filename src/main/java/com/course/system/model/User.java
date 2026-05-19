package com.course.system.model;

// OOP ANNOTATIONS for User.java:
// - Abstraction: 'User' is abstract and provides common user attributes and contract 'getRoleDescription()'.
// - Inheritance: 'Student' and 'Admin' extend User.
// - Encapsulation: id, username, password, email, userType are private with getters/setters to control access.
// - Information Hiding: toString() is used for file serialization but reveals sensitive fields (password) — consider hashing and hiding sensitive data.

import java.io.Serializable;

public abstract class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String userType;

    public User() {}

    public User(String id, String username, String password, String email, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public abstract String getRoleDescription();

    @Override
    public String toString() {
        return id + "|" + username + "|" + password + "|" + email + "|" + userType;
    }
}
