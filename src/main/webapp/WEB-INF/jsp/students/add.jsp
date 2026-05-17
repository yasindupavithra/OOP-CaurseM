<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Student | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <nav>
        <div class="container">
            <a href="/" class="nav-logo">EduReg System</a>
            <div class="nav-links">
                <a href="/">Dashboard</a>
                <a href="/courses">Courses</a>
                <a href="/students" class="active">Students</a>
                <a href="/instructors">Instructors</a>
                <a href="/registrations">Registrations</a>
                <a href="/payments">Payments</a>
                <a href="/users">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container" style="max-width: 650px;">
        <div class="card">
            <h2 style="margin-bottom: 0.5rem;">Add New Student (Create Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Register a student account with associated academic profiles.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/students/add" method="post">
                <div class="form-group">
                    <label>Full Name <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="fullName" required placeholder="e.g. Yasindu Pavithra">
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Username <span style="color: var(--danger);">*</span></label>
                        <input type="text" name="username" required placeholder="e.g. yasindu">
                    </div>
                    <div class="form-group">
                        <label>Password <span style="color: var(--danger);">*</span></label>
                        <input type="password" name="password" required placeholder="e.g. password123">
                    </div>
                </div>
                <div class="form-group">
                    <label>Email Address <span style="color: var(--danger);">*</span></label>
                    <input type="email" name="email" required placeholder="e.g. yasindu@student.com">
                </div>
                <div class="form-group">
                    <label>Degree Program / Major <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="degreeProgram" required placeholder="e.g. B.Sc. in Computer Science">
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Current Cumulative GPA <span style="color: var(--danger);">*</span></label>
                        <input type="number" step="0.01" name="gpa" value="3.5" min="0.0" max="4.0" required>
                    </div>
                    <div class="form-group">
                        <label>Year of Study <span style="color: var(--danger);">*</span></label>
                        <select name="yearOfStudy">
                            <option value="1">Year 1</option>
                            <option value="2">Year 2</option>
                            <option value="3" selected>Year 3</option>
                            <option value="4">Year 4</option>
                            <option value="5">Year 5</option>
                        </select>
                    </div>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Create Student Profile</button>
                    <a href="/students" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
