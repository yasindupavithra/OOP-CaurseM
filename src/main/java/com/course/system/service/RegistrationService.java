package com.course.system.service;

import com.course.system.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private static final String FILE_NAME = "registrations.txt";

    @Autowired
    private FileService fileService;

    public void addRegistration(Registration registration) throws IOException {
        // Validation: duplicate enrollment prevention
        List<Registration> existing = getAllRegistrations();
        for (Registration r : existing) {
            if (r.getStudentId().equals(registration.getStudentId()) && 
                r.getCourseId().equals(registration.getCourseId()) &&
                !r.getStatus().equals("DROPPED")) {
                throw new IllegalArgumentException("Student is already enrolled in this course!");
            }
        }
        fileService.appendToFile(FILE_NAME, registration.toString());
    }

    public List<Registration> getAllRegistrations() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Registration> registrations = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                Registration r = new Registration();
                r.setId(parts[0]);
                r.setStudentId(parts[1]);
                r.setCourseId(parts[2]);
                r.setRegistrationDate(LocalDate.parse(parts[3]));
                r.setRegistrationFee(Double.parseDouble(parts[4]));
                r.setStatus(parts[5]);
                registrations.add(r);
            }
        }
        return registrations;
    }

    public Optional<Registration> getRegistrationById(String id) throws IOException {
        return getAllRegistrations().stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public void updateRegistration(Registration updated) throws IOException {
        List<Registration> list = getAllRegistrations();
        List<String> updatedLines = new ArrayList<>();
        for (Registration r : list) {
            if (r.getId().equals(updated.getId())) {
                updatedLines.add(updated.toString());
            } else {
                updatedLines.add(r.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    public void deleteRegistration(String id) throws IOException {
        List<Registration> list = getAllRegistrations();
        List<String> updatedLines = new ArrayList<>();
        for (Registration r : list) {
            if (!r.getId().equals(id)) {
                updatedLines.add(r.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
