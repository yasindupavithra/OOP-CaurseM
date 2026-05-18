package com.course.system.model;

public class Student extends User {
    private String degreeProgram;
    private String fullName;
    private double gpa;
    private int yearOfStudy;
//attributes of the class
    public Student() {
        super();
        setUserType("STUDENT");
    }

    public Student(String id, String username, String password, String email, String degreeProgram, String fullName, double gpa, int yearOfStudy) {
        super(id, username, password, email, "STUDENT");
        this.degreeProgram = degreeProgram;
        this.fullName = fullName;
        this.gpa = gpa;
        this.yearOfStudy = yearOfStudy;
    }

    public String getDegreeProgram() { return degreeProgram; }
    public void setDegreeProgram(String degreeProgram) { this.degreeProgram = degreeProgram; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    @Override
    public String getRoleDescription() {
        return "Student: Enrolled in " + degreeProgram + " (Year " + yearOfStudy + ")";
    }

    @Override
    public String toString() {
        return super.toString() + "|" + degreeProgram + "|" + fullName + "|" + gpa + "|" + yearOfStudy;
    }
}
