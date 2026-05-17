package com.course.system.model;

public class VisitingInstructor extends Instructor {
    private double hourlyRate;
    private double hoursTaught;

    public VisitingInstructor() { super(); }

    public VisitingInstructor(String id, String name, String email, String department, String specialization, double hourlyRate, double hoursTaught) {
        super(id, name, email, department, specialization);
        this.hourlyRate = hourlyRate;
        this.hoursTaught = hoursTaught;
    }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getHoursTaught() { return hoursTaught; }
    public void setHoursTaught(double hoursTaught) { this.hoursTaught = hoursTaught; }

    @Override
    public String getEmploymentType() { return "VISITING"; }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursTaught;
    }

    @Override
    public String toString() {
        return super.toString() + "|" + hourlyRate + "|" + hoursTaught;
    }
}
