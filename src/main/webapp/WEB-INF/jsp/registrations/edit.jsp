<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Registration | EduReg</title>
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
            <h2 style="margin-bottom: 0.5rem;">Edit Enrollment Details (Update Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Modify enrollment status or adjust calculated fees for enrollment <code>${registration.id}</code>.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/registrations/edit" method="post">
                <input type="hidden" name="id" value="${registration.id}">

                <div class="form-group">
                    <label>Student Profile</label>
                    <select name="studentId" required>
                        <c:forEach var="std" items="${students}">
                            <option value="${std.id}" ${std.id == registration.studentId ? 'selected' : ''}>${std.username} (${std.id})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Course Name</label>
                    <select name="courseId" required>
                        <c:forEach var="crs" items="${courses}">
                            <option value="${crs.id}" ${crs.id == registration.courseId ? 'selected' : ''}>${crs.title} [${crs.courseCode}]</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Adjusted Tuition Fee (LKR) <span style="color: var(--danger);">*</span></label>
                        <input type="number" step="0.01" name="registrationFee" value="${registration.registrationFee}" required>
                    </div>
                    <div class="form-group">
                        <label>Enrollment Status <span style="color: var(--danger);">*</span></label>
                        <select name="status">
                            <option value="ENROLLED" ${registration.status == 'ENROLLED' ? 'selected' : ''}>Enrolled</option>
                            <option value="PENDING" ${registration.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                            <option value="DROPPED" ${registration.status == 'DROPPED' ? 'selected' : ''}>Dropped</option>
                        </select>
                    </div>
                </div>
                <input type="hidden" name="studentType" value="REGULAR">

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Update Enrollment Details</button>
                    <a href="/registrations" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
