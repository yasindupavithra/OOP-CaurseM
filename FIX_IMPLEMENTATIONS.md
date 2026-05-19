# CRITICAL FIXES - CODE IMPLEMENTATION GUIDE

## FIX #1: ADD PASSWORD HASHING (2 hours)

### Step 1: Add BCrypt Dependency to pom.xml

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

### Step 2: Create PasswordService

Create `src/main/java/com/course/system/service/PasswordService.java`:

```java
package com.course.system.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * Hash a plaintext password using BCrypt
     */
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }
    
    /**
     * Verify plaintext password against hash
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
```

### Step 3: Update UserService to Hash Passwords

```java
@Service
public class UserService {
    
    @Autowired
    private PasswordService passwordService;
    
    @Autowired
    private FileService fileService;
    
    private static final String FILE_NAME = "users.txt";
    
    // Modify registerUser to hash password
    public void registerUser(User user) throws IOException {
        // Validation for duplicate username or email
        List<User> existingUsers = getAllUsers();
        for (User u : existingUsers) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists!");
            }
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists!");
            }
        }
        
        // HASH PASSWORD BEFORE SAVING
        String hashedPassword = passwordService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        fileService.appendToFile(FILE_NAME, user.toString());
    }
    
    // Add authentication method
    public Optional<User> authenticate(String username, String plainPassword) throws IOException {
        Optional<User> user = getAllUsers().stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username))
            .findFirst();
        
        if (user.isPresent()) {
            boolean passwordValid = passwordService.verifyPassword(
                plainPassword, 
                user.get().getPassword()
            );
            return passwordValid ? user : Optional.empty();
        }
        return Optional.empty();
    }
}
```

### Step 4: Update StudentController (Example)

```java
@PostMapping("/add")
public String addStudent(@RequestParam String username, 
                         @RequestParam String password,
                         @RequestParam String email, 
                         @RequestParam String fullName,
                         @RequestParam String degreeProgram, 
                         @RequestParam double gpa,
                         @RequestParam int yearOfStudy, 
                         Model model) {
    
    // Validation checks (same as before)
    if (username.trim().isEmpty() || password.trim().isEmpty() || 
        email.trim().isEmpty() || fullName.trim().isEmpty()) {
        model.addAttribute("error", "All fields are required!");
        return "students/add";
    }
    
    // ... other validations ...
    
    String id = "std_" + UUID.randomUUID().toString().substring(0, 5);
    Student student = new Student(id, username, password, email, 
                                  degreeProgram, fullName, gpa, yearOfStudy);
    
    try {
        // Password hashing happens inside registerUser now
        userService.registerUser(student);
        return "redirect:/students";
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "students/add";
    } catch (IOException e) {
        model.addAttribute("error", "Failed to save student to file database!");
        return "students/add";
    }
}
```

### ✅ Testing Password Hashing
```java
// Test: Hash and verify
String plainPassword = "myPassword123";
String hash = passwordService.hashPassword(plainPassword);
// hash: $2a$10$abcdefghijklmnopqrstuvwxyz...

boolean matches = passwordService.verifyPassword(plainPassword, hash);
// matches: true

boolean wrongPassword = passwordService.verifyPassword("wrongPassword", hash);
// wrongPassword: false
```

---

## FIX #2: ADD RACE CONDITION LOCKS (1.5 hours)

### Update FileService with Locks

```java
package com.course.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class FileService {

    @Value("${file.storage.path}")
    private String storagePath;
    
    // Thread-safe lock for file operations
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void writeToFile(String filename, List<String> lines) throws IOException {
        lock.writeLock().lock();  // Exclusive write lock
        try {
            Path path = Paths.get(storagePath + filename);
            Files.createDirectories(path.getParent());
            
            // Write to temp file first
            Path tempFile = Files.createTempFile(path.getParent(), ".tmp", "");
            Files.write(tempFile, lines, StandardOpenOption.CREATE, 
                       StandardOpenOption.TRUNCATE_EXISTING);
            
            // Atomic move
            Files.move(tempFile, path, 
                      StandardCopyOption.ATOMIC_MOVE,
                      StandardCopyOption.REPLACE_EXISTING);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void appendToFile(String filename, String line) throws IOException {
        lock.writeLock().lock();  // Exclusive write lock
        try {
            Path path = Paths.get(storagePath + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, (line + System.lineSeparator()).getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<String> readFromFile(String filename) throws IOException {
        lock.readLock().lock();  // Shared read lock
        try {
            Path path = Paths.get(storagePath + filename);
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(path);
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

### ✅ Testing Race Conditions
```java
@Test
public void testConcurrentWrites() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    
    for (int i = 0; i < 100; i++) {
        final int index = i;
        executor.execute(() -> {
            try {
                fileService.appendToFile("test.txt", "Line " + index);
            } catch (IOException e) {
                fail("Exception during concurrent write: " + e);
            }
        });
    }
    
    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
    
    List<String> lines = fileService.readFromFile("test.txt");
    assertEquals(100, lines.size());  // All 100 lines should be present
}
```

---

## FIX #3: ADD REFERENTIAL INTEGRITY (1 hour)

### Update RegistrationService

```java
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
    
    @Autowired
    private PaymentService paymentService;

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
        return getAllRegistrations().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst();
    }

    /**
     * NEW: Check if registration has associated payments
     */
    public boolean hasAssociatedPayments(String registrationId) throws IOException {
        return paymentService.getAllPayments().stream()
            .anyMatch(p -> p.getRegistrationId().equals(registrationId));
    }

    /**
     * NEW: Prevent deletion if payments exist
     */
    public void deleteRegistration(String id) throws IOException {
        // Check if payment exists
        if (hasAssociatedPayments(id)) {
            throw new IllegalArgumentException(
                "Cannot delete registration with existing payments. " +
                "Please cancel payments first or contact administrator."
            );
        }
        
        List<Registration> list = getAllRegistrations();
        List<String> updatedLines = new ArrayList<>();
        for (Registration r : list) {
            if (!r.getId().equals(id)) {
                updatedLines.add(r.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
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
}
```

### Update RegistrationController

```java
@PostMapping("/delete")
public String deleteRegistration(@RequestParam String id, Model model) throws IOException {
    try {
        registrationService.deleteRegistration(id);
        return "redirect:/registrations";
    } catch (IllegalArgumentException e) {
        // Show error: cannot delete due to payments
        model.addAttribute("error", e.getMessage());
        registrationService.getRegistrationById(id)
            .ifPresent(r -> model.addAttribute("registration", r));
        return "registrations/delete";
    }
}
```

---

## FIX #4: FIX DELIMITER CORRUPTION (1 hour)

### Create StringEscapeUtil

```java
package com.course.system.util;

public class StringEscapeUtil {
    
    private static final String DELIMITER = "|";
    private static final String ESCAPE_CHAR = "\\";
    
    /**
     * Escape special characters for safe file storage
     */
    public static String escape(String input) {
        if (input == null) return "";
        return input
            .replace(ESCAPE_CHAR, ESCAPE_CHAR + ESCAPE_CHAR)  // Escape backslash first
            .replace(DELIMITER, ESCAPE_CHAR + DELIMITER)      // Escape pipe
            .replace("\n", "\\n")                               // Escape newline
            .replace("\r", "\\r");                              // Escape carriage return
    }
    
    /**
     * Unescape special characters after reading from file
     */
    public static String unescape(String input) {
        if (input == null) return "";
        return input
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace(ESCAPE_CHAR + DELIMITER, DELIMITER)
            .replace(ESCAPE_CHAR + ESCAPE_CHAR, ESCAPE_CHAR);
    }
}
```

### Update User.toString()

```java
@Override
public String toString() {
    return StringEscapeUtil.escape(id) + "|" + 
           StringEscapeUtil.escape(username) + "|" + 
           StringEscapeUtil.escape(password) + "|" + 
           StringEscapeUtil.escape(email) + "|" + 
           StringEscapeUtil.escape(userType);
}
```

### Update UserService parsing

```java
public List<User> getAllUsers() throws IOException {
    List<String> lines = fileService.readFromFile(FILE_NAME);
    List<User> users = new ArrayList<>();
    for (String line : lines) {
        // Better parsing that respects escapes
        String[] parts = parseLine(line);  // Use new parsing method
        if (parts.length >= 5) {
            String id = StringEscapeUtil.unescape(parts[0]);
            String username = StringEscapeUtil.unescape(parts[1]);
            String password = StringEscapeUtil.unescape(parts[2]);
            String email = StringEscapeUtil.unescape(parts[3]);
            String type = StringEscapeUtil.unescape(parts[4]);

            if (type.equals("ADMIN")) {
                users.add(new Admin(id, username, password, email));
            } else {
                String degreeProgram = parts.length > 5 ? 
                    StringEscapeUtil.unescape(parts[5]) : "General";
                String fullName = parts.length > 6 ? 
                    StringEscapeUtil.unescape(parts[6]) : username;
                double gpa = parts.length > 7 ? Double.parseDouble(parts[7]) : 0.0;
                int yearOfStudy = parts.length > 8 ? Integer.parseInt(parts[8]) : 1;
                users.add(new Student(id, username, password, email, 
                                    degreeProgram, fullName, gpa, yearOfStudy));
            }
        }
    }
    return users;
}

/**
 * Parse CSV line respecting escaped delimiters
 */
private String[] parseLine(String line) {
    List<String> result = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    
    for (int i = 0; i < line.length(); i++) {
        char ch = line.charAt(i);
        
        if (ch == '\\' && i + 1 < line.length()) {
            // Escaped character
            char nextChar = line.charAt(i + 1);
            current.append(nextChar);
            i++;  // Skip next character
        } else if (ch == '|') {
            // Unescaped delimiter
            result.add(current.toString());
            current = new StringBuilder();
        } else {
            current.append(ch);
        }
    }
    
    result.add(current.toString());  // Add last field
    return result.toArray(new String[0]);
}
```

---

## FIX #5: ADD INPUT VALIDATION SERVICE (1 hour)

### Create ValidationService

```java
package com.course.system.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    
    private static final String EMAIL_REGEX = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    private static final String USERNAME_REGEX = 
        "^[A-Za-z0-9_-]{3,20}$";
    
    private static final String CARD_NUMBER_REGEX = 
        "^\\d{16}$";
    
    /**
     * Validate email format
     */
    public void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException("Invalid email format");
        }
        if (email.length() > 100) {
            throw new ValidationException("Email too long (max 100 characters)");
        }
    }
    
    /**
     * Validate username
     */
    public void validateUsername(String username) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username is required");
        }
        if (!username.matches(USERNAME_REGEX)) {
            throw new ValidationException(
                "Username must be 3-20 characters (alphanumeric, dash, underscore only)"
            );
        }
    }
    
    /**
     * Validate password
     */
    public void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }
        if (password.length() > 50) {
            throw new ValidationException("Password too long (max 50 characters)");
        }
    }
    
    /**
     * Validate GPA
     */
    public void validateGPA(double gpa) throws ValidationException {
        if (gpa < 0.0 || gpa > 4.0) {
            throw new ValidationException("GPA must be between 0.0 and 4.0");
        }
    }
    
    /**
     * Validate year of study
     */
    public void validateYearOfStudy(int year) throws ValidationException {
        if (year < 1 || year > 6) {
            throw new ValidationException("Year of study must be between 1 and 6");
        }
    }
    
    /**
     * Validate credits
     */
    public void validateCredits(int credits) throws ValidationException {
        if (credits <= 0 || credits > 6) {
            throw new ValidationException("Credits must be between 1 and 6");
        }
    }
    
    /**
     * Validate payment amount
     */
    public void validatePaymentAmount(double amount) throws ValidationException {
        if (amount <= 0) {
            throw new ValidationException("Payment amount must be greater than zero");
        }
        if (amount > 1000000) {
            throw new ValidationException("Payment amount exceeds maximum allowed");
        }
    }
    
    /**
     * Validate card number
     */
    public void validateCardNumber(String cardNumber) throws ValidationException {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new ValidationException("Card number is required");
        }
        if (!cardNumber.matches(CARD_NUMBER_REGEX)) {
            throw new ValidationException("Card number must be exactly 16 digits");
        }
    }
}
```

### Create ValidationException

```java
package com.course.system.exception;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
```

### Use in Controllers

```java
@Autowired
private ValidationService validationService;

@PostMapping("/add")
public String addStudent(@RequestParam String username, 
                         @RequestParam String password,
                         @RequestParam String email, 
                         @RequestParam String fullName,
                         @RequestParam String degreeProgram, 
                         @RequestParam double gpa,
                         @RequestParam int yearOfStudy, 
                         Model model) {
    
    try {
        // Centralized validation
        validationService.validateUsername(username);
        validationService.validatePassword(password);
        validationService.validateEmail(email);
        validationService.validateGPA(gpa);
        validationService.validateYearOfStudy(yearOfStudy);
        
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Full name is required");
        }
        
        String id = "std_" + UUID.randomUUID().toString().substring(0, 5);
        Student student = new Student(id, username, password, email, 
                                      degreeProgram, fullName, gpa, yearOfStudy);
        
        userService.registerUser(student);
        return "redirect:/students";
        
    } catch (ValidationException e) {
        model.addAttribute("error", e.getMessage());
        return "students/add";
    } catch (IOException e) {
        model.addAttribute("error", "Failed to save student");
        return "students/add";
    }
}
```

---

## TESTING ALL FIXES

### Unit Test Example

```java
package com.course.system;

import com.course.system.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SecurityAndIntegrityTests {
    
    @Autowired
    private PasswordService passwordService;
    
    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Test
    public void testPasswordHashing() {
        String plain = "myPassword123";
        String hash = passwordService.hashPassword(plain);
        
        assertNotEquals(plain, hash);  // Never plaintext
        assertTrue(passwordService.verifyPassword(plain, hash));  // Correct password
        assertFalse(passwordService.verifyPassword("wrongPass", hash));  // Wrong password
    }
    
    @Test
    public void testValidationService() {
        assertThrows(ValidationException.class, () -> {
            validationService.validateEmail("invalid-email");
        });
        
        assertThrows(ValidationException.class, () -> {
            validationService.validateGPA(5.0);  // Out of range
        });
        
        assertDoesNotThrow(() -> {
            validationService.validateGPA(3.5);  // Valid
        });
    }
    
    @Test
    public void testRegistrationDelete() throws IOException {
        // Should throw error if payment exists
        assertThrows(IllegalArgumentException.class, () -> {
            registrationService.deleteRegistration("reg_with_payment");
        });
    }
}
```

---

## IMPLEMENTATION ORDER

### Day 1 (Critical Fixes - 3-4 hours):
1. ✅ Fix delimiter corruption (copy-paste StringEscapeUtil)
2. ✅ Add password hashing (copy-paste PasswordService)
3. ✅ Add race condition locks (update FileService)

### Day 2 (Data Integrity - 2-3 hours):
4. ✅ Add referential integrity (update RegistrationService)
5. ✅ Create ValidationService (copy-paste entire service)

### Day 3 (Documentation - 2-3 hours):
6. ✅ Create README.md with setup instructions
7. ✅ Add DATA_FORMAT.md documenting file schema
8. ✅ Add architecture diagram

**Total Time: 7-10 hours for all critical + documentation fixes**
**Expected Mark Gain: +15-20 marks (70 → 85-90)**

