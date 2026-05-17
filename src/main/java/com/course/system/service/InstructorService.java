package com.course.system.service;

import com.course.system.model.Instructor;
import com.course.system.model.PermanentInstructor;
import com.course.system.model.VisitingInstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    private static final String FILE_NAME = "instructors.txt";

    @Autowired
    private FileService fileService;

    public void addInstructor(Instructor instructor) throws IOException {
        List<Instructor> existing = getAllInstructors();
        for (Instructor ins : existing) {
            if (ins.getEmail().equalsIgnoreCase(instructor.getEmail())) {
                throw new IllegalArgumentException("Instructor with this email already exists!");
            }
        }
        fileService.appendToFile(FILE_NAME, instructor.toString());
    }

    public List<Instructor> getAllInstructors() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Instructor> instructors = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];
                String dept = parts[3];
                String spec = parts[4];
                String type = parts[5];

                if (type.equals("PERMANENT")) {
                    double monthlySalary = parts.length > 6 ? Double.parseDouble(parts[6]) : 50000.0;
                    double allowance = parts.length > 7 ? Double.parseDouble(parts[7]) : 10000.0;
                    instructors.add(new PermanentInstructor(id, name, email, dept, spec, monthlySalary, allowance));
                } else {
                    double hourlyRate = parts.length > 6 ? Double.parseDouble(parts[6]) : 2000.0;
                    double hoursTaught = parts.length > 7 ? Double.parseDouble(parts[7]) : 40.0;
                    instructors.add(new VisitingInstructor(id, name, email, dept, spec, hourlyRate, hoursTaught));
                }
            }
        }
        return instructors;
    }

    public Optional<Instructor> getInstructorById(String id) throws IOException {
        return getAllInstructors().stream().filter(ins -> ins.getId().equals(id)).findFirst();
    }

    public void updateInstructor(Instructor updated) throws IOException {
        List<Instructor> list = getAllInstructors();
        List<String> updatedLines = new ArrayList<>();
        for (Instructor ins : list) {
            if (ins.getId().equals(updated.getId())) {
                updatedLines.add(updated.toString());
            } else {
                updatedLines.add(ins.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    public void deleteInstructor(String id) throws IOException {
        List<Instructor> list = getAllInstructors();
        List<String> updatedLines = new ArrayList<>();
        for (Instructor ins : list) {
            if (!ins.getId().equals(id)) {
                updatedLines.add(ins.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
