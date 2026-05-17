package com.course.system.service;

import com.course.system.model.Course;
import com.course.system.model.OnlineCourse;
import com.course.system.model.OnsiteCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private static final String FILE_NAME = "courses.txt";

    @Autowired
    private FileService fileService;

    public void addCourse(Course course) throws IOException {
        // Validation: No duplicates for course code
        List<Course> existing = getAllCourses();
        for (Course c : existing) {
            if (c.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                throw new IllegalArgumentException("Course with this course code already exists!");
            }
        }
        fileService.appendToFile(FILE_NAME, course.toString());
    }

    public List<Course> getAllCourses() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Course> courses = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                String id = parts[0];
                String title = parts[1];
                String instructor = parts[2];
                int credits = Integer.parseInt(parts[3]);
                String code = parts[4];
                boolean open = Boolean.parseBoolean(parts[5]);
                String type = parts[6];

                if (type.equals("ONLINE")) {
                    courses.add(new OnlineCourse(id, title, instructor, credits, code, open, parts[7], parts.length > 8 ? parts[8] : ""));
                } else {
                    courses.add(new OnsiteCourse(id, title, instructor, credits, code, open, parts[7], parts.length > 8 ? parts[8] : ""));
                }
            }
        }
        return courses;
    }

    public Optional<Course> getCourseById(String id) throws IOException {
        return getAllCourses().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public void updateCourse(Course updatedCourse) throws IOException {
        List<Course> courses = getAllCourses();
        List<String> updatedLines = new ArrayList<>();
        for (Course c : courses) {
            if (c.getId().equals(updatedCourse.getId())) {
                updatedLines.add(updatedCourse.toString());
            } else {
                updatedLines.add(c.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    public void deleteCourse(String id) throws IOException {
        List<Course> courses = getAllCourses();
        List<String> updatedLines = new ArrayList<>();
        for (Course c : courses) {
            if (!c.getId().equals(id)) {
                updatedLines.add(c.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
