# QUICK FIX CHECKLIST

## 🔴 CRITICAL (Do First - 6-7 hours)

### Security & Data Integrity
- [ ] **1. Password Hashing** (2 hours)
  - Add BCrypt dependency
  - Hash passwords on save
  - Update file format to store hashes
  - Test login with hashed passwords
  - **Mark gain**: +3-4

- [ ] **2. Add Authentication** (3 hours)
  - Add Spring Security
  - Create login page
  - Protect all endpoints
  - Test role-based access
  - **Mark gain**: +4-5

- [ ] **3. Fix Race Conditions** (1.5 hours)
  - Implement ReentrantReadWriteLock
  - Lock FileService read/write operations
  - Test concurrent access
  - **Mark gain**: +2-3

- [ ] **4. Add Referential Integrity** (1 hour)
  - Prevent deleting registration with payments
  - Prevent deleting student with registrations
  - Add validation checks
  - **Mark gain**: +2

- [ ] **5. Fix Delimiter Corruption** (1 hour)
  - Escape special characters
  - Fix parsing logic
  - Test with problematic data
  - **Mark gain**: +1-2

**Subtotal Critical: 6 items, 8.5 hours, +12-17 marks**

---

## 🟠 HIGH PRIORITY (3-4 hours if time allows)

- [ ] **6. Input Sanitization** (1 hour)
  - Validate all inputs
  - Escape special characters
  - Add constraints
  - **Mark gain**: +1-2

- [ ] **7. CSRF Protection** (0.5 hours)
  - Enable Spring Security CSRF
  - Add token to forms
  - **Mark gain**: +1

- [ ] **8. Extract Validation Service** (1.5 hours)
  - Create ValidationService class
  - Move validation logic
  - Add custom exceptions
  - **Mark gain**: +1-2

- [ ] **9. Comprehensive Documentation** (3 hours)
  - Complete README.md
  - Add ARCHITECTURE.md
  - Add DATA_FORMAT.md
  - Add API_DOCS.md
  - Add SETUP.md
  - **Mark gain**: +3-4

- [ ] **10. Logging & Error Handling** (1.5 hours)
  - Add SLF4J logging
  - Log CRUD operations
  - Better exception hierarchy
  - **Mark gain**: +1-2

**Subtotal High Priority: 5 items, 7.5 hours, +8-11 marks**

---

## 🟡 MEDIUM PRIORITY (If time allows - 3-4 hours)

- [ ] **11. Unit Tests** (3-4 hours)
  - Test Services
  - Test validation
  - Test file operations
  - Aim for 70%+ coverage
  - **Mark gain**: +2-3

- [ ] **12. UI/UX Improvements** (2 hours)
  - Client-side form validation
  - Success messages
  - Mobile responsiveness
  - Loading states
  - **Mark gain**: +1-2

- [ ] **13. Design Patterns** (2 hours)
  - Repository pattern
  - DTO pattern
  - Better exception handling
  - **Mark gain**: +1-2

- [ ] **14. Model Design** (1 hour)
  - Make Instructor concrete
  - Add enums for types
  - **Mark gain**: +1

**Subtotal Medium Priority: 4 items, 8-9 hours, +5-7 marks**

---

## ⚪ OPTIONAL (Advanced - 5-6 hours)

- [ ] **15. Database Migration** (5-6 hours)
  - Set up MySQL
  - Create Spring Data JPA repositories
  - Migrate all data access
  - **Mark gain**: +5-8

---

## TIMELINE RECOMMENDATIONS

### Option 1: MINIMUM (Before Submission) - 6-7 hours
Do items: **1, 2, 3, 4, 5**
- Expected mark: **76-78/100**
- Status: PASSING, safe for viva

### Option 2: RECOMMENDED (Ideal) - 13-15 hours
Do items: **1-10** (all critical + high priority)
- Expected mark: **80-85/100**
- Status: GOOD, strong viva performance

### Option 3: STRETCH (Premium) - 21-24 hours
Do items: **1-15** (all fixes)
- Expected mark: **88-92/100**
- Status: EXCELLENT, distinction potential

---

## QUICK REFERENCE: What Each Fix Does

| Fix # | Issue | Impact | Priority |
|-------|-------|--------|----------|
| 1 | Plaintext passwords | 🔴 Critical security | NOW |
| 2 | No login/auth | 🔴 Critical security | NOW |
| 3 | Race conditions | 🔴 Critical data loss | NOW |
| 4 | Orphaned data | 🔴 Critical integrity | NOW |
| 5 | File corruption | 🔴 Critical parsing | NOW |
| 6 | No input validation | 🟠 Security risk | SOON |
| 7 | CSRF vulnerability | 🟠 Security risk | SOON |
| 8 | Code duplication | 🟠 Code quality | SOON |
| 9 | No documentation | 🟠 Viva readiness | SOON |
| 10 | No logging | 🟠 Debugging | SOON |
| 11 | No tests | 🟡 Code confidence | IF TIME |
| 12 | Poor UX | 🟡 User experience | IF TIME |
| 13 | Bad design | 🟡 Architecture | IF TIME |
| 14 | Model issues | 🟡 OOP design | IF TIME |
| 15 | File persistence | ⚪ Technical debt | NICE |

---

## CURRENT MARKS (Before Fixes)

```
CRUD Functionality:     24/30  (80%)  ✓ Decent
OOP Concepts:          15/20  (75%)  ⚠️ Moderate
File Handling:          7/10  (70%)  ⚠️ Risky
User Interface:         8/10  (80%)  ✓ Good
Security & Quality:     5/10  (50%)  🔴 CRITICAL
Viva Readiness:         6/10  (60%)  🔴 RISKY
Documentation:          5/10  (50%)  🔴 CRITICAL
---
TOTAL:                 70/100  (70%)  = PASSING BUT RISKY
```

---

## ESTIMATED MARKS AFTER EACH FIX

```
Baseline:                                70

+ #1 (Password Hashing):                73-74
+ #2 (Authentication):                  77-79
+ #3 (Race Conditions):                 79-82
+ #4 (Referential Integrity):           81-84
+ #5 (Delimiter Fix):                   82-85
+ #6 (Input Sanitization):              83-86
+ #7 (CSRF):                            84-87
+ #8 (Validation Service):              85-88
+ #9 (Documentation):                   88-92
+ #10 (Logging):                        89-93
+ #11 (Tests):                          91-95
+ #12 (UI/UX):                          92-96
+ #13 (Design Patterns):                93-97
+ #14 (Model Design):                   94-98
+ #15 (Database Migration):             96-99
```

---

## START HERE

### Before You Do Anything:
1. Read the full AUDIT_REPORT.md
2. Identify which option (1, 2, or 3) you'll pursue
3. Set up your environment
4. Create a branch for fixes: `git checkout -b fix/security-and-integrity`

### If You Have 1-2 Hours:
✅ Do items: **5, 7**
- Fix delimiter corruption
- Add CSRF

### If You Have 3-4 Hours:
✅ Do items: **1, 3, 5**
- Add password hashing
- Fix race conditions  
- Fix delimiter corruption

### If You Have 6-8 Hours:
✅ Do items: **1, 2, 3, 4, 5**
- All critical fixes
- Expect: 76-78/100

### If You Have 12-16 Hours:
✅ Do items: **1-10**
- All critical + high priority
- Expect: 80-85/100

---

## MARKING BREAKDOWN FOR VIVA

**Viva examiner will ask about:**
- How you handle passwords (they'll be impressed if fixed ✅)
- What about race conditions? (They'll respect honest answer + fix ✅)
- How do you ensure data integrity? (FK checks will impress ✅)
- Security concerns? (Show you thought about CSRF, auth ✅)
- Design patterns used? (If you added them ✅)

**Viva examiner will be suspicious about:**
- Why passwords plaintext? (If not fixed 🔴)
- How do you prevent concurrent edits? (If no locks 🔴)
- What happens if registration deleted? (If no checks 🔴)
- Where's your documentation? (If absent 🔴)
- Why no tests? (If none exist 🔴)

