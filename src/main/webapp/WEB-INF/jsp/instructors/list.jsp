<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Instructor Directory | EduReg</title>
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
                <a href="/instructors" class="active">Instructors</a>
                <a href="/registrations">Registrations</a>
                <a href="/payments">Payments</a>
                <a href="/users">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="margin: 0; font-size: 2.25rem;">Instructor Directory (Read Component)</h1>
                <p style="color: var(--text-muted); margin: 0.25rem 0 0 0;">Manage permanent faculty and visiting lecturers</p>
            </div>
            <div style="display: flex; gap: 0.75rem; align-items: center;">
                <form action="/instructors" method="get" style="display: flex; gap: 0.5rem; margin: 0;">
                    <input type="text" name="search" value="${search}" placeholder="Search name, dept, email..." style="padding: 0.625rem 1rem; min-width: 260px; margin: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="/instructors" class="btn btn-secondary">Clear</a>
                    </c:if>
                </form>
                <a href="/instructors/add" class="btn btn-primary" style="background: linear-gradient(135deg, var(--primary) 0%, #312e81 100%);">+ Add Instructor</a>
            </div>
        </div>

        <div class="card" style="padding: 0; overflow: hidden;">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Instructor ID</th>
                            <th>Name</th>
                            <th>Email Address</th>
                            <th>Department</th>
                            <th>Specialization</th>
                            <th>Employment Type</th>
                            <th>Calculated Salary (Polymorphic)</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ins" items="${instructors}">
                            <tr>
                                <td><code style="background-color: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-weight: bold; color: var(--primary);">${ins.id}</code></td>
                                <td><strong>${ins.name}</strong></td>
                                <td>${ins.email}</td>
                                <td>${ins.department}</td>
                                <td><span style="font-weight: 500;">${ins.specialization}</span></td>
                                <td>
                                    <span class="badge ${ins.employmentType == 'PERMANENT' ? 'badge-success' : 'badge-warning'}">
                                        ${ins.employmentType}
                                    </span>
                                </td>
                                <td>
                                    <span style="font-weight: 600; color: #166534;">
                                        LKR ${ins.calculateSalary()} 
                                    </span>
                                    <span style="font-size: 0.75rem; color: var(--text-muted); display: block;">
                                        <c:choose>
                                            <c:when test="${ins.employmentType == 'PERMANENT'}">
                                                (Base: ${ins.monthlySalary} + Allow: ${ins.allowance})
                                            </c:when>
                                            <c:otherwise>
                                                (Rate: ${ins.hourlyRate}/hr &times; Hours: ${ins.hoursTaught})
                                            </c:when>
                                        </c:choose>
                                    </span>
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="/instructors/edit/${ins.id}" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem; margin-right: 0.25rem;">Edit</a>
                                    <a href="/instructors/delete/${ins.id}" class="btn btn-danger" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty instructors}">
                            <tr>
                                <td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">No instructors found. Register a new instructor!</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
