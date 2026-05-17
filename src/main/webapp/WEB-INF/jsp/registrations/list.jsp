<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Registrations | EduReg</title>
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

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="margin: 0; font-size: 2.25rem;">Enrollments & Registrations (Read Component)</h1>
                <p style="color: var(--text-muted); margin: 0.25rem 0 0 0;">Manage student course enrollments and calculate polymorphic tuition fees</p>
            </div>
            <div style="display: flex; gap: 0.75rem; align-items: center;">
                <form action="/registrations" method="get" style="display: flex; gap: 0.5rem; margin: 0;">
                    <input type="text" name="search" value="${search}" placeholder="Search student, course, status..." style="padding: 0.625rem 1rem; min-width: 260px; margin: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="/registrations" class="btn btn-secondary">Clear</a>
                    </c:if>
                </form>
                <a href="/registrations/register" class="btn btn-primary" style="background: linear-gradient(135deg, var(--primary) 0%, #312e81 100%);">+ New Registration</a>
            </div>
        </div>

        <div class="card" style="padding: 0; overflow: hidden;">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Registration ID</th>
                            <th>Student ID</th>
                            <th>Course ID</th>
                            <th>Enrollment Date</th>
                            <th>Calculated Fee (Polymorphic)</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${registrations}">
                            <tr>
                                <td><code style="background-color: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-weight: bold; color: var(--primary);">${r.id}</code></td>
                                <td><strong>${r.studentId}</strong></td>
                                <td><strong>${r.courseId}</strong></td>
                                <td>${r.registrationDate}</td>
                                <td>
                                    <span style="font-weight: bold; color: #166534;">
                                        LKR ${r.registrationFee}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${r.status == 'ENROLLED' ? 'badge-success' : (r.status == 'PENDING' ? 'badge-warning' : 'badge-danger')}">
                                        ${r.status}
                                    </span>
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="/registrations/edit/${r.id}" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem; margin-right: 0.25rem;">Edit</a>
                                    <a href="/registrations/delete/${r.id}" class="btn btn-danger" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty registrations}">
                            <tr>
                                <td colspan="7" style="text-align: center; padding: 3rem; color: var(--secondary);">No registration records found. Register a student in a course!</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
