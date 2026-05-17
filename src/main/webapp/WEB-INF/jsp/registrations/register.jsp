<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Registration | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <nav>
        <div class="container">
            <a href="/" class="nav-logo">EduReg System</a>
            <div class="nav-links">
                <a href="/">Dashboard</a>
                <a href="/courses">Courses</a>
                <a href="/students">Students</a>
                <a href="/instructors">Instructors</a>
                <a href="/registrations" class="active">Registrations</a>
                <a href="/payments">Payments</a>
                <a href="/users">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container" style="max-width: 650px;">
        <div class="card">
            <h2 style="margin-bottom: 0.5rem;">Enroll in a Course (Create Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Map a registered student to an academic course, auto-generating a polymorphic fee schedule.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/registrations/register" method="post">
                <div class="form-group">
                    <label>Select Student <span style="color: var(--danger);">*</span></label>
                    <select name="studentId" required>
                        <c:forEach var="student" items="${students}">
                            <option value="${student.id}">${student.fullName} (${student.id})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Select Course <span style="color: var(--danger);">*</span></label>
                    <select name="courseId" required>
                        <c:forEach var="course" items="${courses}">
                            <option value="${course.id}">${course.title} [${course.courseCode}]</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Student Category (Polymorphic Fee Calculation) <span style="color: var(--danger);">*</span></label>
                    <select name="studentType">
                        <option value="REGULAR">Regular Student (Base Rate)</option>
                        <option value="INTERNATIONAL">International Student (Premium Rate + Resource Charge)</option>
                    </select>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Complete Enrollment</button>
                    <a href="/registrations" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
