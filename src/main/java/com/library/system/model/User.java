package com.library.system.model;

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

    // Encapsulation: Public getters and setters
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

    // Polymorphism: Abstract method to be implemented by subclasses
    public abstract String getPermissions();

    @Override
    public String toString() {
        return id + "|" + username + "|" + password + "|" + email + "|" + userType;
    }
}
