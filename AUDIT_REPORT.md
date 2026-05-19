# COMPREHENSIVE SYSTEM AUDIT REPORT
**Course Registration System - SE1020 OOP Assignment**  
**Audit Date:** May 20, 2026  
**Auditor:** Senior Java/OOP Lecturer & Assignment Marker  

---

## EXECUTIVE SUMMARY

**Total Marks: 72/100**

Your system demonstrates **solid OOP fundamentals** with proper inheritance, polymorphism, and file handling. However, there are **critical security issues, data integrity problems, and design flaws** that prevent a higher mark.

**Viability: MODERATE** - Project can score 72-78 with fixes  
**Viva Safety: RISKY** - Be prepared for tough questions on design choices  

---

## 1. CRUD FUNCTIONALITY ANALYSIS
**Mark: 24/30** (-6 for critical issues)

### What Works ✅
- **Create**: All entities (Student, Course, Registration, Payment, Instructor, User) have proper add functionality
- **Read**: Search/filter on all modules works correctly
- **Update**: Full record updates implemented
- **Delete**: Cascade concerns handled partially (registrations exist independently of students)

### CRITICAL BUGS & WEAKNESSES ❌

#### 1.1 **MAJOR BUG: No Validation on Registration Delete**
```java
// PaymentController & RegistrationController
// PROBLEM: Can delete registrations with associated payments
// If registration is deleted, payments become orphaned
registrationService.deleteRegistration(id);  // ← No FK check
```
**Impact**: Data integrity violation - orphaned payments  
**Fix**: Before deleting registration, verify no payments exist:
```java
public boolean canDeleteRegistration(String registrationId) {
    List<Payment> payments = paymentService.getAllPayments();
    return payments.stream().noneMatch(p -> p.getRegistrationId().equals(registrationId));
}
```

#### 1.2 **Duplicate Registration Bug**
```java
// RegistrationService.java - Line 21-23
for (Registration r : existing) {
    if (r.getStudentId().equals(registration.getStudentId()) && 
        r.getCourseId().equals(registration.getCourseId()) &&
        !r.getStatus().equals("DROPPED")) {  // ← WEAK LOGIC
```
**Problem**: Status check is weak. If a student drops a course and re-enrolls:
- The check prevents re-enrollment after DROP
- But doesn't prevent duplicate ENROLLED entries if status changes

**Better Logic**:
```java
long activeCount = existing.stream()
    .filter(r -> r.getStudentId().equals(registration.getStudentId()) && 
                 r.getCourseId().equals(registration.getCourseId()) &&
                 (r.getStatus().equals("ENROLLED") || r.getStatus().equals("PENDING")))
    .count();
if (activeCount > 0) throw new IllegalArgumentException("Already enrolled");
```

#### 1.3 **Payment Validation Missing**
```java
// PaymentController.java - Line 75
if (amount <= 0) {  // ← Only basic check
    model.addAttribute("error", "Payment amount must be greater than zero!");
```
**Missing Validations**:
- Payment amount should match registration fee (fraud prevention)
- No verification that payment.registrationId exists
- No prevention of duplicate payments for same registration
- No maximum payment limit check

**Add These**:
```java
Registration reg = registrationService.getRegistrationById(registrationId)
    .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

// Prevent overpayment
if (amount > reg.getRegistrationFee() * 1.1) {  // 10% tolerance
    throw new IllegalArgumentException("Payment exceeds expected amount");
}

// Prevent duplicate payments
List<Payment> paid = paymentService.getAllPayments()
    .stream()
    .filter(p -> p.getRegistrationId().equals(registrationId) && p.getStatus().equals("PAID"))
    .collect(Collectors.toList());
if (!paid.isEmpty()) {
    throw new IllegalArgumentException("This registration already has a paid payment");
}
```

#### 1.4 **Course Code Uniqueness Not Enforced on Update**
```java
// CourseService.java - updateCourse() method
// PROBLEM: When updating, doesn't check if new code conflicts with existing
public void updateCourse(Course updatedCourse) throws IOException {
    List<Course> courses = getAllCourses();
    List<String> updatedLines = new ArrayList<>();
    for (Course c : courses) {
        if (c.getId().equals(updatedCourse.getId())) {
            updatedLines.add(updatedCourse.toString());  // ← Can violate uniqueness
        } else {
            updatedLines.add(c.toString());
        }
    }
    fileService.writeToFile(FILE_NAME, updatedLines);
}
```

**Fix**: Validate on update too:
```java
public void updateCourse(Course updatedCourse) throws IOException {
    List<Course> courses = getAllCourses();
    for (Course c : courses) {
        if (!c.getId().equals(updatedCourse.getId()) && 
            c.getCourseCode().equalsIgnoreCase(updatedCourse.getCourseCode())) {
            throw new IllegalArgumentException("Course code already in use!");
        }
    }
    // ... rest of update logic
}
```

#### 1.5 **Weak Email Validation**
```java
// StudentController.java - Line 61
if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
```
**Problem**: Regex allows invalid emails like `user@.` or `user@@domain.com`

**Better Regex**:
```java
if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
```

#### 1.6 **Year of Study Validation Issue**
```java
if (yearOfStudy < 1 || yearOfStudy > 5) {
```
**Problem**: Why 5? What if someone takes 6 years? Should be configurable or at least documented.

### Edge Cases Not Handled ⚠️

| Edge Case | Current Behavior | Expected Behavior |
|-----------|------------------|-------------------|
| Edit user email to duplicate | No check on update | Should validate like create |
| Register student to closed course | Allows if course exists | Should check `openForRegistration` flag |
| Payment status lifecycle | Can change freely | Should have state machine (PENDING→PAID→CLEARED) |
| Delete student with registrations | Deletes student, leaves orphaned registrations | Should either cascade or prevent deletion |
| Import with special chars in name | May corrupt file parsing | Should sanitize or escape pipe characters |

### CRUD Mark Breakdown
- Create: 5/5 (good validation)
- Read: 5/5 (search works well)
- Update: 6/8 (missing validations, uniqueness checks)
- Delete: 5/8 (no cascade/FK checks)
- Data Integrity: 3/4 (orphaned data risk)
- **Subtotal: 24/30**

---

## 2. OOP CONCEPTS ANALYSIS
**Mark: 15/20** (-5 for design issues)

### ✅ Well-Implemented OOP Concepts

#### 2.1 **Inheritance** ⭐ Good
```
User (Abstract)
├── Student (extends User)
└── Admin (extends User)

Course (Abstract)
├── OnlineCourse (extends Course)
└── OnsiteCourse (extends Course)

Payment (Abstract)
├── CardPayment (extends Payment)
└── BankTransferPayment (extends Payment)

Instructor (Abstract - should not be abstract for base!)
├── PermanentInstructor (extends Instructor)
└── VisitingInstructor (extends Instructor)
```

**Positive**: Clear hierarchy, good separation of concerns  
**Negative**: See section 2.3

#### 2.2 **Polymorphism** ⭐ Good
```java
// Abstract methods overridden correctly
public abstract String getRoleDescription();  // User
public abstract String getCourseType();  // Course
public abstract String getPaymentMethod();  // Payment
public abstract double calculateSalary();  // Instructor

// Payment polymorphism used correctly in UI
if (payment instanceof CardPayment) { ... }
```

**Issue**: Type checking with instanceof instead of using polymorphism:
```java
// BAD (in PaymentService & CourseService)
if (method.equals("CARD")) {  // String comparison instead of polymorphism
    // ...
} else {  // Assumes BANK_TRANSFER
```
**Should use**: `payment.getPaymentMethod()` polymorphically

#### 2.3 **Encapsulation** ⭐⭐ MEDIUM - Has Issues
```java
// ✅ Good: Private fields with getters/setters
public class Student extends User {
    private String degreeProgram;
    private String fullName;
    private double gpa;
    // ... getters/setters
}

// ❌ BAD: Plain file storage, no security
// Passwords stored in PLAINTEXT in text file!
user.toString() returns: "std1|yasindu|yasindu123|yasindu@student.com|STUDENT|..."
```

**CRITICAL SECURITY ISSUE**: Passwords are visible in plaintext!  
Should hash passwords (use BCrypt):
```java
@Service
public class PasswordService {
    public String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }
    public boolean verify(String plain, String hash) {
        return BCrypt.checkpw(plain, hash);
    }
}
```

#### 2.4 **Abstraction** ⭐⭐ MEDIUM - Partial

**Good**:
- Abstract base classes defined
- `getRoleDescription()` is a good abstraction

**Problems**:
- Service layer doesn't abstract the file persistence layer
- Could use Repository pattern:
```java
// Better design:
public interface UserRepository {
    void save(User user) throws IOException;
    User findById(String id) throws IOException;
    List<User> findAll() throws IOException;
}

public class FileUserRepository implements UserRepository {
    // Actual file operations
}
```

#### 2.5 **Information Hiding** ⭐ WEAK

**Issues**:
1. **Passwords exposed** (as noted above)
2. **toString() reveals everything** - No sanitization
3. **Model classes too exposed** - Controllers directly manipulate
4. **No DTO/VO pattern** - Data transfers between layers uncontrolled

```java
// BAD: Direct model exposure
@PostMapping("/add")
public String addStudent(@RequestParam String username, 
                         @RequestParam String password, ...) {
    // Parameters directly mapped to model
    Student student = new Student(id, username, password, ...);
}

// BETTER: Use DTOs
@PostMapping("/add")
public String addStudent(@Valid StudentCreateRequest request) {
    // Request object validates input
    Student student = studentMapper.toDomain(request);
}
```

### ❌ Design Flaws

#### 2.6 **Tight Coupling**
```java
// CourseController depends directly on CourseService
@Autowired
private CourseService courseService;

// CourseService depends directly on FileService
@Autowired
private FileService fileService;

// No dependency injection interface, hardcoded implementation
```

**Better Design**:
```java
public interface IFileService {
    void write(String file, List<String> lines) throws IOException;
    List<String> read(String file) throws IOException;
}

// Then inject IFileService, not FileService
```

#### 2.7 **God Classes / Single Responsibility Violation**
```java
// Student class does too much:
// - Authentication data (username, password)
// - Academic data (degreeProgram, GPA)
// - Personal data (fullName, email)

// Should split into:
// - StudentProfile (academic)
// - AuthCredential (security)
// - ContactInfo (personal)
```

#### 2.8 **DRY Violation - Duplicate Code**

Same validation logic repeated in ALL controllers:
```java
// StudentController
if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { ... }

// PaymentController (has own validation)
if (amount <= 0) { ... }

// CourseController
if (credits <= 0 || credits > 6) { ... }
```

**Should Extract**:
```java
@Component
public class ValidationService {
    public void validateEmail(String email) throws ValidationException { ... }
    public void validateGPA(double gpa) throws ValidationException { ... }
    public void validateCredits(int credits) throws ValidationException { ... }
}
```

#### 2.9 **Course Model Design Issue**

```java
public abstract class Course {
    // ...
    public abstract String getCourseType();  // ← Why abstract?
    
    @Override
    public String toString() {
        return id + "|" + title + "|" + instructor + "|" 
            + credits + "|" + courseCode + "|" 
            + openForRegistration + "|" + getCourseType();  // ← Dynamic data in toString!
    }
}

// OnlineCourse extends Course
public class OnlineCourse extends Course {
    public String getCourseType() { return "ONLINE"; }  // ← Could be constant
}
```

**Problem**: `getCourseType()` should be a constant property, not a method  
**Fix**:
```java
public class OnlineCourse extends Course {
    private static final String COURSE_TYPE = "ONLINE";
    
    public String getCourseType() { return COURSE_TYPE; }
}
```

#### 2.10 **No Exception Hierarchy**

```java
// Uses generic exceptions everywhere
throw new IllegalArgumentException("Username already exists!");
throw new IOException("File error");

// No custom exceptions for domain logic
```

**Better**:
```java
public class UserAlreadyExistsException extends DomainException { }
public class CourseCodeDuplicateException extends DomainException { }
public class FileStorageException extends TechnicalException { }
```

### OOP Mark Breakdown
- Inheritance: 3/4 (good, but Instructor shouldn't be abstract)
- Polymorphism: 3/4 (overused instanceof)
- Encapsulation: 2/4 (plaintext passwords!)
- Abstraction: 3/4 (incomplete)
- Information Hiding: 2/4 (exposed data)
- Design Patterns: 0/4 (no patterns used)
- **Subtotal: 15/20**

---

## 3. FILE HANDLING ANALYSIS
**Mark: 7/10** (-3 for serious issues)

### File Handling Implementation

```java
@Service
public class FileService {
    @Value("${file.storage.path}")
    private String storagePath;

    public void writeToFile(String filename, List<String> lines) throws IOException {
        Path path = Paths.get(storagePath + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, lines, StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void appendToFile(String filename, String line) throws IOException {
        Path path = Paths.get(storagePath + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, (line + System.lineSeparator()).getBytes(), 
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<String> readFromFile(String filename) throws IOException {
        Path path = Paths.get(storagePath + filename);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path);
    }
}
```

### ✅ Strengths
- Uses modern `java.nio.file` APIs (not deprecated FileReader/Writer)
- Proper path handling with `Paths`
- Creates directories automatically
- Files properly closed by `Files.write()` and `Files.readAllLines()`
- Good resource management

### ❌ CRITICAL ISSUES

#### 3.1 **RACE CONDITION - No File Locking**
```
Thread 1: Read all users
Thread 2: Add new user
Thread 1: Updates file (overwrites Thread 2's data)

Result: Data loss!
```

**Current flow**:
```java
// UserService.updateUser()
List<User> users = getAllUsers();  // ← Read from file
// ... modify list ...
fileService.writeToFile(FILE_NAME, updatedLines);  // ← Write to file
// ← ANOTHER THREAD COULD MODIFY FILE HERE
```

**Fix**: Add file locking or use database
```java
private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

public void updateUser(User user) throws IOException {
    lock.writeLock().lock();
    try {
        List<User> users = getAllUsers();
        // ... update ...
        fileService.writeToFile(FILE_NAME, updatedLines);
    } finally {
        lock.writeLock().unlock();
    }
}
```

#### 3.2 **DELIMITER CORRUPTION**
```
Data: "Yasindu Pavithra" (name contains pipe? Or commas?)
toString(): id + "|" + name + "|" + ...
// ← If name contains "|", parsing breaks!
```

**Example failure**:
```
// If student name is "Silva | Kavisha"
std3|Silva | Kavisha|kavisha@student.com|STUDENT|...
         ↑ Extra pipe character breaks parsing!

// Split by "|" produces wrong parts
```

**Fix**: Escape special characters
```java
public String toString() {
    String safeName = fullName.replace("|", "\\|");  // Escape pipes
    return id + "|" + safeName + "|" + ...;
}

// On read:
String[] parts = line.split("\\|(?=(?:[^\\\\]|\\\\.)*$)");  // Regex that respects escapes
```

Or use **CSV format** (Apache Commons CSV):
```java
CSVFormat.DEFAULT
    .withHeader("id", "name", "email", ...)
    .print(writer)
    .printRecord(id, name, email, ...);
```

#### 3.3 **CONCURRENT MODIFICATION - Update Loop**
```java
// PaymentService, RegistrationService, CourseService all do this:
for (Payment p : list) {
    if (p.getId().equals(updated.getId())) {
        updatedLines.add(updated.toString());  // ← Reconstructs entire file
    } else {
        updatedLines.add(p.toString());
    }
}
fileService.writeToFile(FILE_NAME, updatedLines);  // ← TRUNCATE_EXISTING
```

**Problem**: If 1000 records and 1 updates:
- Rewrites ALL 1000 records
- Risk of data corruption if process crashes mid-write
- No atomic operation

**Fix**: Implement write-to-temp-then-rename pattern
```java
Path tempFile = Files.createTempFile(storagePath, ".tmp");
Files.write(tempFile, updatedLines);
Files.move(tempFile, path, StandardCopyOption.ATOMIC_MOVE, 
           StandardCopyOption.REPLACE_EXISTING);
```

#### 3.4 **NO BACKUP ON WRITE**
```java
// If write fails, original file is lost
Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
```

**Should backup first**:
```java
if (Files.exists(path)) {
    Files.copy(path, Paths.get(path + ".backup"), 
               StandardCopyOption.REPLACE_EXISTING);
}
Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
```

#### 3.5 **DATA TYPE PARSING ERRORS NOT CAUGHT**
```java
// RegistrationService
try {
    r.setRegistrationDate(LocalDate.parse(parts[3]));
    r.setRegistrationFee(Double.parseDouble(parts[4]));
} catch (Exception e) {
    // ← Silently skips bad records!
    // No logging, no error tracking
}
```

**Fix**:
```java
try {
    r.setRegistrationDate(LocalDate.parse(parts[3]));
} catch (DateTimeParseException e) {
    logger.error("Invalid date format: {}", parts[3]);
    throw new DataIntegrityException("Corrupted registration date", e);
}
```

#### 3.6 **DUPLICATE DATA POSSIBLE**
```
// In users.txt
admin1|admin|admin123|admin@edureg.com|ADMIN
std1|yasindu|yasindu123|yasindu@student.com|STUDENT|...
admin1|admin2|newpassword|admin2@edureg.com|ADMIN  ← DUPLICATE ID!
```

**No deduplication logic** in file reading.

#### 3.7 **NO ARCHIVAL/RETENTION POLICY**
- Files grow infinitely (especially payments.txt)
- No deletion/archival of old records
- Performance degrades over time

### File Handling Mark Breakdown
- FileWriter/Reader Usage: 4/4 (modern APIs)
- Proper File Closing: 4/4 (NIO handles this)
- Exception Handling: 1/2 (missing error details)
- Data Consistency: -2/4 (race conditions, no locking)
- Update/Delete Logic: 2/3 (full rewrite, no atomicity)
- Corruption Prevention: -1/3 (delimiter issue)
- **Subtotal: 7/10**

---

## 4. USER INTERFACE ANALYSIS
**Mark: 8/10**

### ✅ Strengths
- **Modern Design**: Glassmorphism navbar, gradient backgrounds
- **Responsive Cards**: Grid system (grid-3, grid-4) responsive
- **Color Scheme**: Consistent CSS variables (--primary, --danger, --success)
- **Navigation**: Clear, sticky navbar with active states
- **Tables**: Proper table structure with badges for GPA, status

### ❌ Issues

#### 4.1 **Mobile Responsiveness - Incomplete**
```css
/* Missing media queries */
@media (max-width: 768px) {
    .grid-3 { grid-template-columns: 1fr; }
    .container { padding: 1rem; }
}
```

Currently, on mobile:
- Navigation links stack badly
- Tables don't wrap on mobile
- Cards remain full width (might be okay)

**Missing**: responsive font sizes, mobile-first approach

#### 4.2 **Accessibility Issues**
```html
<!-- Missing alt text -->
<span>👤</span>  <!-- ← Should be: -->
<span role="img" aria-label="User icon">👤</span>

<!-- Missing form labels -->
<input type="text" name="search" placeholder="...">
<!-- ← Should have: -->
<label for="search-input">Search</label>
<input id="search-input" type="text" name="search">

<!-- Color-only distinction (GPA badges) -->
<span class="badge badge-success">GPA: 3.5</span>  <!-- Only color, no text difference -->
```

#### 4.3 **Form Validation - Client-Side Missing**
```html
<!-- Student add form - no client validation -->
<input type="text" name="username" />
<input type="password" name="password" />
<input type="email" name="email" />
<input type="number" name="gpa" />
<!-- ← Should have HTML5 validation: -->
<input type="email" name="email" required />
<input type="number" name="gpa" min="0" max="4" step="0.01" required />
<input type="text" name="username" required minlength="3" maxlength="50" />
```

**Plus JavaScript validation** before form submit (UX improvement)

#### 4.4 **Error Messages - Not User Friendly**
```java
model.addAttribute("error", "Username already exists!");
model.addAttribute("error", "Failed to save student to file database!");  // ← Too technical
```

Should be:
```
"Student profile could not be saved. Please try again or contact support."
```

#### 4.5 **No Success Feedback**
```java
return "redirect:/students";  // ← User doesn't know what succeeded
```

Should add toast/flash message:
```java
redirectAttributes.addFlashAttribute("success", "Student profile created successfully!");
return "redirect:/students";
```

#### 4.6 **UI Inconsistencies**
- Button sizing varies (no consistent padding)
- Search form alignment varies per page
- Table headers sometimes not aligned with data

#### 4.7 **Loading States Missing**
- No disabled state for buttons during submission
- No loading indicator for slow file operations
- No timeout/retry UI

#### 4.8 **Dashboard Stats - Limited Information**
```html
<div style="font-size: 2.25rem;">GPA: ${student.gpa}</div>
<!-- Shows number only, no sparkline or trend -->
```

Could show:
- GPA distribution chart
- Course completion rate
- Payment status
- Registration trends

### UI Mark Breakdown
- Design Quality: 4/4 (modern, professional)
- Responsiveness: 2/3 (missing media queries)
- Navigation: 3/3 (clear, sticky)
- Form Validation: 1/3 (server-side only)
- User Feedback: 0/3 (no success messages)
- Accessibility: 1/3 (missing labels, aria)
- Layout Consistency: 2/3 (minor inconsistencies)
- **Subtotal: 8/10**

---

## 5. SECURITY & CODE QUALITY ANALYSIS
**Mark: 5/10** (-5 for security issues)

### ❌ CRITICAL SECURITY ISSUES

#### 5.1 **PLAINTEXT PASSWORD STORAGE** 🔴 CRITICAL
```java
// Data file contains:
admin1|admin|admin123|admin@edureg.com|ADMIN
                ↑ PASSWORD IN PLAINTEXT!
```

**Risk**: Anyone with file access sees all passwords  
**Regulatory Impact**: GDPR violation, no security compliance  

**MUST FIX**:
```java
// 1. Add Spring Security & BCrypt
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// 2. Hash passwords on save
String hashedPassword = passwordEncoder.encode(password);

// 3. Update file format
admin1|admin|$2a$10$abcdefg...|admin@edureg.com|ADMIN  // ← Hashed
```

#### 5.2 **NO INPUT SANITIZATION** 🔴 CRITICAL
```java
// Direct string injection into file
fileService.appendToFile(FILE_NAME, registration.toString());
// What if toString() contains newlines? Or special chars?
```

**Attack**:
```
Create student with name: "Test\|fake|email"
Result: File line becomes corrupted, next read fails
```

**Fix**:
```java
public String toFileFormat() {
    // Escape special characters
    String safeName = fullName.replace("|", "\\|").replace("\n", "\\n");
    return id + "|" + safeName + "|" + ...;
}
```

#### 5.3 **NO AUTHENTICATION/AUTHORIZATION** 🔴 CRITICAL
```java
@GetMapping("/courses")
public String listCourses(...) throws IOException {
    // ← Anyone can access, no login check
    List<Course> courses = courseService.getAllCourses();
}

@PostMapping("/delete")
public String deleteCourse(@RequestParam String id) throws IOException {
    // ← No role check, even admin could delete everything
    courseService.deleteCourse(id);
}
```

**Should Add Spring Security**:
```java
@GetMapping("/courses")
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public String listCourses(...) throws IOException {
    // Only authorized users
}

@PostMapping("/delete")
@PreAuthorize("hasRole('ADMIN')")
public String deleteCourse(...) {
    // Only admins
}
```

#### 5.4 **NO CSRF PROTECTION** 🟠 HIGH
```html
<!-- Forms missing CSRF token -->
<form action="/students/add" method="post">
    <input type="text" name="username" />
    <!-- ← No hidden CSRF token -->
    <button type="submit">Add</button>
</form>
```

**Fix** (Spring Security provides by default if enabled):
```html
<form action="/students/add" method="post">
    <input type="text" name="username" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Add</button>
</form>
```

#### 5.5 **SQL INJECTION RISK (If Migrated to DB)** 🟠 HIGH
```java
// Current file-based, but if migrated:
String query = "SELECT * FROM students WHERE name = '" + name + "'";
// ← Vulnerable!
```

**Use Parameterized Queries**:
```java
public Optional<User> findByUsername(String username) {
    return repo.findByUsernameIgnoreCase(username);  // ← Spring Data handles parameterization
}
```

#### 5.6 **HARDCODED VALUES** 🟡 MEDIUM
```java
// PaymentController
String id = "pay_" + UUID.randomUUID().toString().substring(0, 5);
// ← UUID generation repeated everywhere, not centralized

// Registration
double baseFee = 5000.0;  // ← Hardcoded, should be configuration
```

**Fix**:
```java
@Configuration
public class SystemConfig {
    @Value("${system.id-prefix.payment}")
    private String paymentIdPrefix = "pay_";
    
    @Value("${registration.base-fee}")
    private double baseFee = 5000.0;
}
```

#### 5.7 **NO LOGGING** 🟡 MEDIUM
```java
// No audit trail of who did what
public void deleteUser(String id) throws IOException {
    List<User> users = getAllUsers();
    List<String> updatedLines = new ArrayList<>();
    for (User u : users) {
        if (!u.getId().equals(id)) {
            updatedLines.add(u.toString());
        }
    }
    fileService.writeToFile(FILE_NAME, updatedLines);
    // ← No log of deletion
}
```

**Add Logging**:
```java
private static final Logger logger = LoggerFactory.getLogger(UserService.class);

public void deleteUser(String id) throws IOException {
    logger.info("User deletion requested for ID: {}", id);
    try {
        // ... deletion ...
        logger.info("User {} successfully deleted", id);
    } catch (Exception e) {
        logger.error("Failed to delete user {}: {}", id, e.getMessage());
        throw e;
    }
}
```

#### 5.8 **NULL POINTER RISKS** 🟡 MEDIUM
```java
public String listStudents(@RequestParam(required = false) String search, Model model) {
    students = students.stream()
            .filter(u -> ((Student) u).getFullName()  // ← What if fullName is null?
                         .toLowerCase()
                         .contains(search.toLowerCase()));
}
```

**Fix**:
```java
.filter(u -> Optional.ofNullable(((Student) u).getFullName())
             .orElse("")
             .toLowerCase()
             .contains(search.toLowerCase()))
```

#### 5.9 **HARDCODED FILE PATHS** 🟡 MEDIUM
```properties
file.storage.path=data/
```

**Risks**:
- Different environments need different paths
- Relative paths can break depending on where app runs

**Fix**:
```properties
# application.properties
file.storage.path=${APP_DATA_PATH:./data/}

# application-prod.properties
file.storage.path=/var/app/edureg/data/
```

#### 5.10 **NO RATE LIMITING** 🟡 MEDIUM
```java
@PostMapping("/add")
public String addStudent(...) {
    // Nothing prevents brute force registrations
    // Anyone could spam 1000 registrations rapidly
}
```

**Add Spring Rate Limiting**:
```java
@GetMapping("/add")
@RateLimit(permitsPerMinute = 10)
public String showAddForm() { ... }
```

### Code Quality Issues

#### 5.11 **No Exception Handling Strategy**
```java
try {
    userService.registerUser(student);
    return "redirect:/students";
} catch (IllegalArgumentException e) {
    model.addAttribute("error", e.getMessage());
    return "students/add";
} catch (IOException e) {
    model.addAttribute("error", "Failed to save student to file database!");
    return "students/add";
}
```

**Better**:
```java
try {
    userService.registerUser(student);
    return "redirect:/students";
} catch (DuplicateUserException e) {
    model.addAttribute("error", e.getUserFriendlyMessage());
    return "students/add";
} catch (FileStorageException e) {
    logger.error("Storage error while saving student", e);
    model.addAttribute("error", "System error. Please try again.");
    return "students/add";
}
```

#### 5.12 **Large Methods / Low Cohesion**
```java
@PostMapping("/add")
public String addPayment(@RequestParam String registrationId, 
                         @RequestParam double amount,
                         @RequestParam String method, 
                         @RequestParam(required = false) String cardNumber,
                         @RequestParam(required = false) String cardType,
                         @RequestParam(required = false) String bankName,
                         @RequestParam(required = false) String referenceNumber, 
                         Model model) throws IOException {
    // ← 40+ lines: validation, business logic, UI logic all mixed
}
```

**Should Extract**:
```java
@PostMapping("/add")
public String addPayment(@Valid PaymentRequest request, Model model) {
    try {
        paymentService.recordPayment(request);
        return "redirect:/payments";
    } catch (ValidationException e) {
        model.addAttribute("error", e.getMessage());
        return "payments/add";
    }
}
```

### Security & Code Quality Mark Breakdown
- Input Validation: 1/3 (only basic, no sanitization)
- Authentication: 0/3 (none)
- Authorization: 0/3 (none)
- Encryption: 0/2 (passwords plaintext)
- Exception Handling: 2/3 (generic try-catch)
- Null Safety: 1/3 (no null checks)
- Code Structure: 2/4 (methods too long)
- Logging: 0/2 (no audit trail)
- Configuration: 1/2 (hardcoded values)
- **Subtotal: 5/10**

---

## 6. VIVA READINESS
**Mark: 6/10** (-4 for risky areas)

### Likely Viva Questions & Model Answers

#### Q1: "Why did you use abstract classes for User and Course?"
**Weak Answer** ❌:
"Because they're base classes and we have subclasses."

**Strong Answer** ✅:
"Abstract classes define contracts for subclasses. User has the abstract method `getRoleDescription()` which each subtype (Student, Admin) must implement differently. This ensures every user type explicitly defines its role behavior. Similarly, Course requires subclasses to implement `getCourseType()` to distinguish between ONLINE and ONSITE, enforcing polymorphic behavior."

---

#### Q2: "How does your system handle concurrent access to files?"
**Weak Answer** ❌:
"It just reads and writes files."

**Risky Answer** ⚠️:
"If two users try to update at the same time, one might overwrite the other's data. We could use locks."

**Strong Answer** ✅:
"Currently, our system has a race condition vulnerability. If two threads read the file simultaneously, both modify it, and the second write overwrites the first. To fix this, I would implement a ReentrantReadWriteLock around file operations, allowing multiple readers but exclusive writer access. For production, I'd migrate to a database with ACID transactions."

---

#### Q3: "How are passwords secured in your system?"
**Weak Answer** ❌:
"They're stored in the file with other data."

**Dangerous Answer** 🔴:
"Passwords are stored in the text file, but only administrators have access to the data folder."

**Strong Answer** ✅:
"I acknowledge this is a critical security flaw. Passwords are currently stored in plaintext, which violates security best practices. To fix this, I would implement BCrypt password hashing when users are created or updated. The password would be hashed before storing in the file, and authentication would compare the user's input hash against the stored hash. Additionally, I should add Spring Security to manage authentication and authorization."

---

#### Q4: "Explain the difference between your use of inheritance and composition in this system."
**Good Question!** This tests understanding of design decisions.

**Answer**:
"I used inheritance for IS-A relationships:
- Student IS-A User
- OnlineCourse IS-A Course
- CardPayment IS-A Payment

This creates a substitutability relationship - anywhere the system expects a User, I can pass a Student. 

I didn't use composition, but could have in cases like:
- Registration could have-a Course instead of just storing courseId
- Payment could have-a Registration object

But with file persistence, storing references as IDs is appropriate. If I migrated to a database with foreign keys, composition (object references) would be better than IS-A where appropriate."

---

#### Q5: "How would you handle payment concurrency? What if two payments try to pay for the same registration?"
**Answer**:
"Currently, I only validate that duplicate ENROLLED registrations can't exist. But I don't prevent duplicate payments. 

If two payments are submitted simultaneously for the same registration:
1. Both would be written to payments.txt (current flaw)
2. Registration would show double-paid

Fixes:
- Add a concurrent payment lock: Only one payment per registration can process at a time
- Add a unique constraint: database would enforce this naturally
- Check existing paid payments before allowing new payment
- Add payment status machine: PENDING → PAID → CLEARED (only one PAID state allowed)"

---

#### Q6: "Why didn't you use a database instead of text files?"
**Answer**:
"The assignment required file handling in Java, so I chose TXT files with a delimiter-based format. This demonstrates file I/O concepts.

However, in production, a database would solve many issues:
- No race conditions (database handles locking)
- No parsing corruption (structured schema)
- Better query capability (filter, sort, aggregate)
- Referential integrity (foreign keys prevent orphaned data)
- Atomic transactions (all-or-nothing updates)

If I were to redesign, I'd use Spring Data JPA with MySQL/PostgreSQL."

---

#### Q7: "Your code repeats validation logic across controllers. How would you refactor this?"
**Answer**:
"Excellent observation. Validation is repeated in StudentController, CourseController, etc. I should extract validation into a separate service:

```java
@Component
public class ValidationService {
    public void validateEmail(String email) throws ValidationException { }
    public void validateGPA(double gpa) throws ValidationException { }
    public void validateCredits(int credits) throws ValidationException { }
}
```

This follows Single Responsibility Principle - validation logic is centralized, maintainable, and testable. Controllers become thinner and focused on routing logic."

---

#### Q8: "How do you ensure data consistency when deleting a registration?"
**Answer (RISKY)**:
"Currently, I delete the registration without checking if payments exist. This creates orphaned payments - a data integrity issue.

Proper solution:
- Before deleting, query payments for that registration
- If payments exist in PAID status, prevent deletion
- Or implement cascading: delete all payments when registration deleted (with audit trail)
- Ideally: soft-delete (mark as DELETED) instead of hard delete for audit"

---

#### Q9: "Explain your payment polymorphism. CardPayment vs BankTransferPayment - what's the benefit?"
**Answer**:
"Payment is abstract with methods:
- `getPaymentMethod()` - returns 'CARD' or 'BANK_TRANSFER'
- `getPaymentDetails()` - formatted output specific to payment type

CardPayment returns: 'Paid via VISA Card Ending in 1234'
BankTransferPayment returns: 'Bank Transfer: BOC (Ref: TRF123456)'

This is polymorphism in action - caller doesn't need to know payment type. In UI:
```java
payment.getPaymentDetails()  // Works for any payment subtype
```

Instead of:
```java
if (payment instanceof CardPayment) { // ← Bad pattern
    CardPayment cp = (CardPayment) payment;
    // ...
} else if (payment instanceof BankTransferPayment) {
    // ...
}
```"

---

#### Q10: "What's your biggest design mistake, and how would you fix it?"
**Honest Answer** ✅:
"My biggest mistake was storing passwords in plaintext. This is a critical security vulnerability. I should have implemented BCrypt password hashing from the start. Additionally, I should have designed the system with authentication/authorization (Spring Security) from day one rather than building it unsecured and planning to add security later.

Another design issue is tight coupling between controllers and services - I should have used dependency injection of interfaces rather than concrete implementations."

---

### Risky Areas in Viva

| Topic | Risk Level | Why |
|-------|-----------|-----|
| Security | 🔴 CRITICAL | Plaintext passwords, no auth, no CSRF |
| Concurrency | 🔴 CRITICAL | Race conditions in file handling |
| Design Patterns | 🟠 HIGH | No patterns used, tight coupling |
| Exception Handling | 🟠 HIGH | Generic catch-all, no custom exceptions |
| Testing | 🔴 CRITICAL | No unit tests visible in project |
| Data Integrity | 🔴 CRITICAL | Orphaned data, no referential integrity |
| File Corruption | 🟡 MEDIUM | Delimiter corruption risk |

---

## 7. DOCUMENTATION ANALYSIS
**Mark: 5/10**

### What Exists ✅
- `README.md` (incomplete)
- Basic comments in some classes (StudentController, PaymentController)
- Configuration in `application.properties`

### What's Missing ❌

#### 7.1 **No Architecture Documentation**
- No system architecture diagram
- No sequence diagrams
- No entity relationship model (especially important for file-based system)

**Should Create**:
```
README.md:
- System Overview
- Architecture Diagram
- File Schema (delimiter positions, data types)
- Class Diagram (inheritance hierarchy)
- Setup Instructions
- Configuration Guide
```

#### 7.2 **No File Format Documentation**
```
Current situation:
users.txt format = id|username|password|email|type|[student-specific-fields]
students.txt = Not separate, students in users.txt
courses.txt = id|title|instructor|credits|code|open|type|[specific-fields]

Where is this documented? NOWHERE!
```

**Should Create** `DATA_FORMAT.md`:
```markdown
# Data Storage Format

## users.txt
Format: id|username|password|email|type|[type-specific]

Example (ADMIN):
admin1|admin|admin123|admin@edureg.com|ADMIN

Example (STUDENT):
std1|yasindu|yasindu123|yasindu@student.com|STUDENT|B.Sc. CS|Yasindu Pavithra|3.85|3

Fields:
- id: String, format: "std_XXXXX"
- username: String, required, unique
- password: String, plaintext (SECURITY ISSUE - should be hashed)
- email: String, required, unique
- type: "ADMIN" or "STUDENT"
- For STUDENT:
  - degreeProgram: String
  - fullName: String
  - gpa: Double (0-4.0)
  - yearOfStudy: Int (1-5)

## courses.txt
Format: id|title|instructor|credits|code|open|type|[type-specific]

Example (ONLINE):
crs_00001|Java OOP Fundamentals|Dr. Silva|3|CS1020|true|ONLINE|Zoom|https://zoom.us/j/123

Example (ONSITE):
crs_00002|Database Design|Prof. Kumar|4|CS2020|true|ONSITE|Room A-101|Main Campus
```

#### 7.3 **No Deployment Documentation**
- No setup guide
- No environment configuration
- No running instructions
- Port information missing
- Data directory not explained

**Should Add** `SETUP.md`:
```markdown
# Setup & Deployment

## Prerequisites
- Java 17+
- Maven 3.8+
- Windows/Linux/Mac

## Local Development
1. Clone repository
2. Run: `mvn spring-boot:run`
3. Access: http://localhost:8080
4. Data files created in ./data/

## Configuration
- Server port: application.properties (default 8080)
- File storage path: application.properties (default ./data/)

## Production Deployment
1. Build: `mvn clean package`
2. Copy edureg.jar to server
3. Set environment variables: APP_DATA_PATH=/var/data/edureg
4. Run: `java -jar edureg.jar`
```

#### 7.4 **No API Documentation**
- No endpoint documentation
- No request/response examples
- No error code documentation

**Should Add** `API.md`:
```markdown
# API Endpoints

## Students

### List Students
GET /students?search=query

Response:
```
{
  "students": [
    {
      "id": "std_1",
      "fullName": "Yasindu Pavithra",
      "username": "yasindu",
      "email": "yasindu@student.com",
      "degreeProgram": "B.Sc. in Computer Science",
      "gpa": 3.85,
      "yearOfStudy": 3
    }
  ]
}
```

### Create Student
POST /students/add
```

#### 7.5 **No Class Diagram**
Students need to understand OOP structure:
```
┌─────────────────────┐
│      User (A)       │
├─────────────────────┤
│ - id: String        │
│ - username: String  │
│ - password: String  │
├─────────────────────┤
│ + getRoleDesc()     │
└─────────────────────┘
        △
       / \
      /   \
  ┌──┴──┐ ┌──┴────┐
  │     │ │       │
Student Admin  (others if any)
```

#### 7.6 **No Design Decision Documentation**
- Why use abstract classes?
- Why this file format?
- Why these validation rules?

**Should Document**:
```markdown
# Design Decisions

## 1. Abstract Classes for User & Course
**Decision**: Use abstract User and Course as base classes

**Rationale**: 
- Ensures all user types define their role
- Enforces `getRoleDescription()` implementation
- Provides common fields (id, email, etc.)
- Supports polymorphism

**Alternatives Considered**:
- Interface (less suitable - doesn't provide common fields)
- Concrete base class (loses compile-time enforcement)

## 2. File-Based Storage
**Decision**: Use delimiter-based TXT files instead of database

**Rationale**:
- Assignment requirement for file handling
- Demonstrates Java I/O concepts
- Suitable for small-scale system

**Drawbacks**:
- Race conditions
- No ACID transactions
- Parsing complexity

**Future**: Migrate to MySQL with Spring Data JPA
```

#### 7.7 **No Test Documentation**
- No test cases documented
- No test coverage report
- No manual testing checklist

#### 7.8 **README is Incomplete**
```markdown
# Course Registration System

(Currently just has header)

SHOULD INCLUDE:
- Project Overview
- Features List
- Technology Stack
- Installation Instructions
- Quick Start
- Project Structure
- Architecture Overview
- Contributing
- License
- Known Issues
```

### Documentation Mark Breakdown
- Class Documentation: 1/2 (only two files have comments)
- Architecture Documentation: 0/2 (none)
- API Documentation: 0/1 (none)
- Setup/Deployment: 0/2 (minimal info)
- File Format: 0/1 (undocumented)
- Design Decisions: 0/1 (undocumented)
- Code Quality (readability): 2/2 (variable names good)
- **Subtotal: 5/10**

---

## 8. GITHUB CONTRIBUTION ANALYSIS
**Note**: Cannot analyze without access to GitHub repository. Instructions:
- Check commit frequency (should be regular, not all at end)
- Verify meaningful commit messages (not "changes")
- Check for individual contributions (each team member should have commits)
- Look for pull requests, reviews, collaboration

---

## 9. FINAL EVALUATION SUMMARY

### Mark Distribution

| Component | Mark | Max | Score |
|-----------|------|-----|-------|
| CRUD Functionality | 24 | 30 | 80% |
| OOP Concepts | 15 | 20 | 75% |
| File Handling | 7 | 10 | 70% |
| User Interface | 8 | 10 | 80% |
| Security & Code Quality | 5 | 10 | 50% 🔴 |
| Viva Readiness | 6 | 10 | 60% ⚠️ |
| Documentation | 5 | 10 | 50% 🔴 |
| GitHub Contribution | - | 10 | TBD |
| **TOTAL** | **70** | **100** | **70%** |

### Estimated Final Mark (without GitHub)
**70-75 out of 100** depending on:
- GitHub history (could add 0-5 marks)
- Viva performance (could add 0-10 marks)

### Can This Project Score 90+?
**NO, not in current state. Possible only with major fixes:**

1. Fix security (plaintext passwords) - Critical
2. Add authentication/authorization - Critical
3. Fix race conditions - Critical
4. Add comprehensive documentation - 5-7 marks
5. Improve code quality/design patterns - 2-3 marks
6. Add unit tests - 3-5 marks

**With all fixes: Realistic range 82-88, unlikely to reach 90.**

---

## 10. PRIORITY FIX LIST

### 🔴 CRITICAL FIXES (Before Submission) - Must Do
These issues can result in mark deduction or viva failure.

1. **ADD PASSWORD HASHING** (Security Critical)
   - [ ] Implement BCrypt
   - [ ] Hash passwords on save
   - [ ] Update file format to store hashes
   - [ ] Update login validation
   - **Estimated time**: 2 hours
   - **Mark gain**: +3-4

2. **ADD AUTHENTICATION/AUTHORIZATION** (Security Critical)
   - [ ] Add Spring Security configuration
   - [ ] Create login page
   - [ ] Protect all endpoints with @PreAuthorize
   - [ ] Add role-based access control
   - **Estimated time**: 3 hours
   - **Mark gain**: +4-5

3. **FIX RACE CONDITION** (Data Integrity Critical)
   - [ ] Implement ReentrantReadWriteLock in FileService
   - [ ] Test with concurrent requests
   - [ ] Document thread-safety
   - **Estimated time**: 1.5 hours
   - **Mark gain**: +2-3

4. **ADD REFERENTIAL INTEGRITY** (Data Integrity Critical)
   - [ ] Prevent deleting registration with payments
   - [ ] Prevent deleting student with registrations
   - [ ] Add cascade options or prevent deletion
   - **Estimated time**: 1 hour
   - **Mark gain**: +2

5. **FIX DELIMITER CORRUPTION** (Data Integrity Critical)
   - [ ] Escape special characters in toString()
   - [ ] Use proper parsing with regex
   - [ ] Consider CSV format (Apache Commons CSV)
   - **Estimated time**: 1 hour
   - **Mark gain**: +1-2

### 🟠 HIGH PRIORITY FIXES (Should Do)
These improve security and code quality significantly.

6. **ADD INPUT SANITIZATION** (Security High)
   - [ ] Validate all user inputs
   - [ ] Escape special characters
   - [ ] Add maxLength constraints
   - **Estimated time**: 1 hour
   - **Mark gain**: +1-2

7. **ADD CSRF PROTECTION** (Security High)
   - [ ] Enable Spring Security CSRF
   - [ ] Add CSRF token to forms
   - [ ] Verify tokens on POST
   - **Estimated time**: 0.5 hours
   - **Mark gain**: +1

8. **EXTRACT VALIDATION SERVICE** (Code Quality High)
   - [ ] Create ValidationService
   - [ ] Move all validation logic there
   - [ ] Add custom exception classes
   - **Estimated time**: 1.5 hours
   - **Mark gain**: +1-2

9. **ADD COMPREHENSIVE DOCUMENTATION** (Documentation High)
   - [ ] Complete README.md
   - [ ] Add ARCHITECTURE.md with diagrams
   - [ ] Add DATA_FORMAT.md
   - [ ] Add API_DOCS.md
   - [ ] Add SETUP.md
   - **Estimated time**: 3 hours
   - **Mark gain**: +3-4

10. **ADD LOGGING & ERROR HANDLING** (Code Quality High)
    - [ ] Add SLF4J logging
    - [ ] Log all CRUD operations
    - [ ] Better error messages
    - [ ] Create custom exception hierarchy
    - **Estimated time**: 1.5 hours
    - **Mark gain**: +1-2

### 🟡 MEDIUM PRIORITY FIXES (Nice to Have)
These improve design but are less critical.

11. **ADD UNIT TESTS** (Code Quality Medium)
    - [ ] Create test classes for Services
    - [ ] Test validation logic
    - [ ] Test file operations
    - [ ] Aim for 70%+ coverage
    - **Estimated time**: 3-4 hours
    - **Mark gain**: +2-3

12. **IMPROVE UI/UX** (UI Medium)
    - [ ] Add client-side form validation
    - [ ] Add success flash messages
    - [ ] Improve mobile responsiveness
    - [ ] Add loading states
    - **Estimated time**: 2 hours
    - **Mark gain**: +1-2

13. **DESIGN PATTERN IMPROVEMENTS** (OOP Medium)
    - [ ] Implement Repository pattern
    - [ ] Use DTOs for data transfer
    - [ ] Add custom exception hierarchy
    - **Estimated time**: 2 hours
    - **Mark gain**: +1-2

14. **IMPROVE COURSE DESIGN** (OOP Medium)
    - [ ] Make Instructor concrete, not abstract
    - [ ] Add CourseType enum instead of String
    - [ ] Add PaymentType enum
    - **Estimated time**: 1 hour
    - **Mark gain**: +1

### ⚪ LOW PRIORITY FIXES (Optional)
These are polish and best practices.

15. **Migrate from File to Database** (Optional)
    - Solve all file/concurrency issues permanently
    - Use Spring Data JPA + MySQL
    - **Estimated time**: 5-6 hours
    - **Mark gain**: +5-8 (if you have time)

16. **Add Containerization** (Optional)
    - [ ] Create Docker setup
    - **Estimated time**: 1 hour
    - **Mark gain**: +0 (bonus only if asked)

---

## ESTIMATED TIMELINE FOR FIXES

### Quick Wins (1-2 hours) - Do these first
- Add CSRF Protection (0.5h)
- Fix Delimiter Corruption (1h)
- Improve Course Design (1h)
- **Time: ~2.5 hours**
- **Expected gain: +4 marks**

### Important Fixes (2-4 hours) - Do before viva
- Add Password Hashing (2h)
- Fix Race Condition (1.5h)
- Add Referential Integrity (1h)
- **Time: ~4.5 hours**
- **Expected gain: +7 marks**

### Quality Improvements (3-4 hours) - If time allows
- Extract Validation Service (1.5h)
- Add Comprehensive Documentation (3h)
- Add Logging (1.5h)
- **Time: ~6 hours**
- **Expected gain: +5 marks**

### Total Recommended Timeline
- **Minimum** (Critical Fixes): 6-7 hours → 77-78 marks
- **Recommended** (Critical + Quality): 10-12 hours → 82-85 marks
- **Stretch** (All fixes): 15-18 hours → 88-92 marks

---

## VIVA TIPS

### Topics to Study
1. **Design Patterns**: Be ready to explain why you didn't use MVC/DAO/Service patterns
2. **Concurrency**: Understand race conditions and locks
3. **Security**: Know about password hashing, OWASP top 10
4. **File I/O**: Explain your file format, parsing strategy
5. **OOP Principles**: DRY, SRP, LSP - be ready to identify violations

### Practice Answers For:
- "What's your biggest weakness in this project?"
- "How would you migrate this to a database?"
- "How would you secure the application?"
- "What would you do differently if you started again?"
- "How would you scale this to 100,000 students?"

### Don't Say These in Viva:
- ❌ "I wasn't thinking about security"
- ❌ "File handling was too complicated so I simplified"
- ❌ "I don't know what a race condition is"
- ❌ "We just copied code from online"
- ❌ "We ran out of time"

### Do Say These:
- ✅ "I identified security issues and here's how I'd fix them"
- ✅ "This is a known limitation of file-based storage"
- ✅ "I would use Spring Security in production"
- ✅ "Here's the trade-off: simplicity vs. robustness"
- ✅ "I chose this design because of assignment constraints"

---

## CONCLUSION

**Project Status**: BORDERLINE (70-75 marks)

**Strengths**:
- Good CRUD implementation
- Solid OOP fundamentals (inheritance, polymorphism)
- Modern Spring Boot setup
- Clean UI design
- Proper use of NIO file APIs

**Weaknesses**:
- Critical security issues (plaintext passwords, no auth)
- Race conditions in file handling
- Data integrity violations
- Weak documentation
- No test coverage
- Design smells (tight coupling, large methods)

**Recommendation**:
- **Minimum for passing (70%+)**: Fix security and race conditions (6-7 hours)
- **Target for distinction (80%+)**: Add documentation, tests, design improvements (12-15 hours)
- **Unlikely for 90%+**: Would need major refactoring + migration to database

**Viva Safety**: MODERATE RISK
- Secure answers ready for design choices
- Study concurrency and security concepts
- Be honest about tradeoffs and limitations

---

**Report Generated**: May 20, 2026  
**Evaluator**: Senior Java/OOP Assessment Marker
