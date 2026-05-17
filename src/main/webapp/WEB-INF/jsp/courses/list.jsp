<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Management | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <nav>
        <div class="container">
            <a href="/" class="nav-logo">EduReg System</a>
            <div class="nav-links">
                <a href="/">Dashboard</a>
                <a href="/courses" class="active">Courses</a>
                <a href="/students">Students</a>
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
                <h1 style="margin: 0; font-size: 2.25rem;">Course Catalog (Read Component)</h1>
                <p style="color: var(--text-muted); margin: 0.25rem 0 0 0;">Manage your academic course offerings</p>
            </div>
            <div style="display: flex; gap: 0.75rem; align-items: center;">
                <form action="/courses" method="get" style="display: flex; gap: 0.5rem; margin: 0;">
                    <input type="text" name="search" value="${search}" placeholder="Search code, title, instructor..." style="padding: 0.625rem 1rem; min-width: 260px; margin: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="/courses" class="btn btn-secondary">Clear</a>
                    </c:if>
                </form>
                <a href="/courses/add" class="btn btn-primary" style="background: linear-gradient(135deg, var(--primary) 0%, #312e81 100%);">+ Add Course</a>
            </div>
        </div>

        <div class="card" style="padding: 0; overflow: hidden;">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Title</th>
                            <th>Instructor</th>
                            <th>Credits</th>
                            <th>Type</th>
                            <th>Location / Meeting Details</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="course" items="${courses}">
                            <tr>
                                <td><code style="background-color: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-weight: bold; color: var(--primary);">${course.courseCode}</code></td>
                                <td><strong>${course.title}</strong></td>
                                <td>${course.instructor}</td>
                                <td><span style="font-weight: 600;">${course.credits}</span></td>
                                <td>
                                    <span class="badge ${course.courseType == 'ONLINE' ? 'badge-warning' : 'badge-success'}">
                                        ${course.courseType}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${course.courseType == 'ONLINE'}">
                                            <span style="font-size: 0.85rem; color: var(--text-muted);">${course.platform} (<a href="${course.meetingLink}" target="_blank" style="color: var(--primary);">Join</a>)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="font-size: 0.85rem; color: var(--text-muted);">${course.campusLocation} (Room ${course.roomNumber})</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${course.openForRegistration}">
                                            <span class="badge badge-success">Open</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger">Closed</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="/courses/edit/${course.id}" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem; margin-right: 0.25rem;">Edit</a>
                                    <a href="/courses/delete/${course.id}" class="btn btn-danger" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty courses}">
                            <tr>
                                <td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">No courses found. Feel free to add a new course!</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
