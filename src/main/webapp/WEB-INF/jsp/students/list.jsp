<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Profile Management | EduReg</title>
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

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="margin: 0; font-size: 2.25rem;">Student Profiles (Read Component)</h1>
                <p style="color: var(--text-muted); margin: 0.25rem 0 0 0;">Manage academic and demographic profiles of students</p>
            </div>
            <div style="display: flex; gap: 0.75rem; align-items: center;">
                <form action="/students" method="get" style="display: flex; gap: 0.5rem; margin: 0;">
                    <input type="text" name="search" value="${search}" placeholder="Search name, major, email..." style="padding: 0.625rem 1rem; min-width: 260px; margin: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="/students" class="btn btn-secondary">Clear</a>
                    </c:if>
                </form>
                <a href="/students/add" class="btn btn-primary" style="background: linear-gradient(135deg, var(--primary) 0%, #312e81 100%);">+ Add Student</a>
            </div>
        </div>

        <div class="card" style="padding: 0; overflow: hidden;">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Student ID</th>
                            <th>Full Name</th>
                            <th>Username</th>
                            <th>Email Address</th>
                            <th>Degree Program / Major</th>
                            <th>Current GPA</th>
                            <th>Year of Study</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td><code style="background-color: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-weight: bold; color: var(--primary);">${student.id}</code></td>
                                <td><strong>${student.fullName}</strong></td>
                                <td>${student.username}</td>
                                <td>${student.email}</td>
                                <td><span style="font-weight: 500;">${student.degreeProgram}</span></td>
                                <td>
                                    <span class="badge ${student.gpa >= 3.5 ? 'badge-success' : (student.gpa >= 3.0 ? 'badge-info' : 'badge-warning')}">
                                        GPA: ${student.gpa}
                                    </span>
                                </td>
                                <td>Year ${student.yearOfStudy}</td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="/students/edit/${student.id}" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem; margin-right: 0.25rem;">Edit</a>
                                    <a href="/students/delete/${student.id}" class="btn btn-danger" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty students}">
                            <tr>
                                td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">No student profiles found. Feel free to register a new student!</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
