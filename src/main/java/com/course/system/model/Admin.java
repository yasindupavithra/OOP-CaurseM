package com.course.system.model;

public class Admin extends User {
    
    public Admin() {
        super();
        setUserType("ADMIN");
    }

    public Admin(String id, String username, String password, String email) {
        super(id, username, password, email, "ADMIN");
    }
//override done
    @Override
    public String getRoleDescription() {
        return "Administrator: Full access to manage students, courses, and instructors.";
    }
}
