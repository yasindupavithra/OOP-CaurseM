<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Student | EduReg</title>
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
            <h2 style="margin-bottom: 0.5rem;">Edit Student Details (Update Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Modify details of student profile <code>${student.id}</code>.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/students/edit" method="post">
                <input type="hidden" name="id" value="${student.id}">

                <div class="form-group">
                    <label>Full Name <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="fullName" value="${student.fullName}" required>
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Username <span style="color: var(--danger);">*</span></label>
                        <input type="text" name="username" value="${student.username}" required>
                    </div>
                    <div class="form-group">
                        <label>Password <span style="color: var(--danger);">*</span></label>
                        <input type="password" name="password" value="${student.password}" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Email Address <span style="color: var(--danger);">*</span></label>
                    <input type="email" name="email" value="${student.email}" required>
                </div>
                <div class="form-group">
                    <label>Degree Program / Major <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="degreeProgram" value="${student.degreeProgram}" required>
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Current Cumulative GPA <span style="color: var(--danger);">*</span></label>
                        <input type="number" step="0.01" name="gpa" value="${student.gpa}" min="0.0" max="4.0" required>
                    </div>
                    <div class="form-group">
                        <label>Year of Study <span style="color: var(--danger);">*</span></label>
                        <select name="yearOfStudy">
                            <option value="1" ${student.yearOfStudy == 1 ? 'selected' : ''}>Year 1</option>
                            <option value="2" ${student.yearOfStudy == 2 ? 'selected' : ''}>Year 2</option>
                            <option value="3" ${student.yearOfStudy == 3 ? 'selected' : ''}>Year 3</option>
                            <option value="4" ${student.yearOfStudy == 4 ? 'selected' : ''}>Year 4</option>
                            <option value="5" ${student.yearOfStudy == 5 ? 'selected' : ''}>Year 5</option>
                        </select>
                    </div>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Update Student Profile</button>
                    <a href="/students" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
