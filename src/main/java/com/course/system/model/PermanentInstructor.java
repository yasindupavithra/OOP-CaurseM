package com.course.system.model;

// OOP ANNOTATIONS for PermanentInstructor.java:
// - Inheritance: extends 'Instructor'.
// - Polymorphism: overrides 'calculateSalary()' to compute monthlySalary + allowance.
// - Encapsulation: keeps 'monthlySalary' and 'allowance' private with getters/setters.

public class PermanentInstructor extends Instructor {
    private double monthlySalary;
    private double allowance;

    public PermanentInstructor() { super(); }

    public PermanentInstructor(String id, String name, String email, String department, String specialization, double monthlySalary, double allowance) {
        super(id, name, email, department, specialization);
        this.monthlySalary = monthlySalary;
        this.allowance = allowance;
    }
// getter and setter methods
    public double getMonthlySalary() { return monthlySalary; }
    public void setMonthlySalary(double monthlySalary) { this.monthlySalary = monthlySalary; }

    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }
//override functions
    @Override
    public String getEmploymentType() { return "PERMANENT"; }

    @Override
    public double calculateSalary() {
        return monthlySalary + allowance;
    }

    @Override
    public String toString() {
        return super.toString() + "|" + monthlySalary + "|" + allowance;
    }
}
