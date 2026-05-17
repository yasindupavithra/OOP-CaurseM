<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Delete Registration | EduReg</title>
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

    <div class="container" style="max-width: 600px;">
        <div class="card" style="border-top: 4px solid var(--danger); text-align: center;">
            <h2 style="color: var(--danger); margin-bottom: 1rem;">Delete Enrollment Confirmation (Delete Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem; font-size: 1.05rem;">
                Are you sure you want to permanently delete the enrollment of student <strong>${registration.studentId}</strong> in course <strong>${registration.courseId}</strong> (<code>${registration.id}</code>)? 
                This action is destructive and cannot be undone.
            </p>

            <div style="background-color: #f8fafc; padding: 1.5rem; border-radius: 0.75rem; border: 1px solid var(--border); margin-bottom: 2rem; text-align: left;">
                <div style="margin-bottom: 0.5rem;"><strong>Registration ID:</strong> ${registration.id}</div>
                <div style="margin-bottom: 0.5rem;"><strong>Student ID:</strong> ${registration.studentId}</div>
                <div style="margin-bottom: 0.5rem;"><strong>Course ID:</strong> ${registration.courseId}</div>
                <div style="margin-bottom: 0.5rem;"><strong>Enrollment Date:</strong> ${registration.registrationDate}</div>
                <div><strong>Assigned Fee:</strong> LKR ${registration.registrationFee}</div>
            </div>

            <form action="/registrations/delete" method="post">
                <input type="hidden" name="id" value="${registration.id}">
                <div style="display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-danger" style="flex: 1;">Yes, Delete Permanently</button>
                    <a href="/registrations" class="btn btn-secondary" style="flex: 1; text-align: center;">No, Go Back</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
