package com.course.system.model;
//abstract used here
public abstract class Course {
    private String id;
    private String title;
    private String instructor;
    private int credits;
    private String courseCode;
    private boolean openForRegistration;

    public Course() {}

    public Course(String id, String title, String instructor, int credits, String courseCode, boolean openForRegistration) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
        this.credits = credits;
        this.courseCode = courseCode;
        this.openForRegistration = openForRegistration;
    }
//getters and setters are used here
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public boolean isOpenForRegistration() { return openForRegistration; }
    public void setOpenForRegistration(boolean openForRegistration) { this.openForRegistration = openForRegistration; }

    public abstract String getCourseType();

    @Override
    public String toString() {
        return id + "|" + title + "|" + instructor + "|" + credits + "|" + courseCode + "|" + openForRegistration + "|" + getCourseType();
    }
}
