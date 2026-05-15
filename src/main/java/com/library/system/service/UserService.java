package com.library.system.service;

import com.library.system.model.AdminUser;
import com.library.system.model.RegularUser;
import com.library.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String FILE_NAME = "users.txt";

    @Autowired
    private FileService fileService;

    // Create
    public void registerUser(User user) throws IOException {
        fileService.appendToFile(FILE_NAME, user.toString());
    }

    // Read All
    public List<User> getAllUsers() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                if (parts[4].equals("ADMIN")) {
                    users.add(new AdminUser(parts[0], parts[1], parts[2], parts[3]));
                } else {
                    users.add(new RegularUser(parts[0], parts[1], parts[2], parts[3], parts.length > 5 ? parts[5] : "Basic"));
                }
            }
        }
        return users;
    }

    // Read One
    public Optional<User> findByUsername(String username) throws IOException {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    // Update
    public void updateUser(User updatedUser) throws IOException {
        List<User> users = getAllUsers();
        List<String> updatedLines = new ArrayList<>();
        for (User u : users) {
            if (u.getId().equals(updatedUser.getId())) {
                updatedLines.add(updatedUser.toString());
            } else {
                updatedLines.add(u.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    // Delete
    public void deleteUser(String id) throws IOException {
        List<User> users = getAllUsers();
        List<String> updatedLines = new ArrayList<>();
        for (User u : users) {
            if (!u.getId().equals(id)) {
                updatedLines.add(u.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
