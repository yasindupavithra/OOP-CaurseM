package com.library.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Value("${file.storage.path}")
    private String storagePath;

    // Generic Write Method
    public void writeToFile(String filename, List<String> lines) throws IOException {
        Path path = Paths.get(storagePath + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // Generic Append Method
    public void appendToFile(String filename, String line) throws IOException {
        Path path = Paths.get(storagePath + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, (line + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // Generic Read Method
    public List<String> readFromFile(String filename) throws IOException {
        Path path = Paths.get(storagePath + filename);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path);
    }
}
