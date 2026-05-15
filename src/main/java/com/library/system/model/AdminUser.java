package com.library.system.model;

public class AdminUser extends User {
    
    public AdminUser() {
        super();
        setUserType("ADMIN");
    }

    public AdminUser(String id, String username, String password, String email) {
        super(id, username, password, email, "ADMIN");
    }

    @Override
    public String getPermissions() {
        return "ALL_ACCESS: Manage Books, Users, and Transactions";
    }
}
