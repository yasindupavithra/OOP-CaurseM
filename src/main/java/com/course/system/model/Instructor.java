package com.course.system.model;

import java.io.Serializable;

public abstract class Instructor implements Serializable {
    private String id;
    private String name;
    private String email;
    private String department;
    private String specialization;

    public Instructor() {}

    public Instructor(String id, String name, String email, String department, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.specialization = specialization;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public abstract String getEmploymentType();
    public abstract double calculateSalary();

    @Override
    public String toString() {
        return id + "|" + name + "|" + email + "|" + department + "|" + specialization + "|" + getEmploymentType();
    }
}
