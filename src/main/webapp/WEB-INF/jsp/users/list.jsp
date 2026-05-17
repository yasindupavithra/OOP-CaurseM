<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Accounts | EduReg</title>
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
                <a href="/registrations">Registrations</a>
                <a href="/payments">Payments</a>
                <a href="/users" class="active">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="margin: 0; font-size: 2.25rem;">User Management (Read Component)</h1>
                <p style="color: var(--text-muted); margin: 0.25rem 0 0 0;">Manage administrative and student accounts</p>
            </div>
            <div style="display: flex; gap: 0.75rem; align-items: center;">
                <form action="/users" method="get" style="display: flex; gap: 0.5rem; margin: 0;">
                    <input type="text" name="search" value="${search}" placeholder="Search username, email, type..." style="padding: 0.625rem 1rem; min-width: 260px; margin: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="/users" class="btn btn-secondary">Clear</a>
                    </c:if>
                </form>
                <a href="/users/register" class="btn btn-primary" style="background: linear-gradient(135deg, var(--primary) 0%, #312e81 100%);">+ Register User</a>
            </div>
        </div>

        <div class="card" style="padding: 0; overflow: hidden;">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Username</th>
                            <th>Email Address</th>
                            <th>User Role</th>
                            <th>Role / Access Level Description</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td><code style="background-color: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-weight: bold; color: var(--primary);">${user.id}</code></td>
                                <td><strong>${user.username}</strong></td>
                                <td>${user.email}</td>
                                <td>
                                    <span class="badge ${user.userType == 'ADMIN' ? 'badge-warning' : 'badge-success'}">
                                        ${user.userType}
                                    </span>
                                </td>
                                <td><span style="font-size: 0.9rem; color: var(--text-muted);">${user.roleDescription}</span></td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="/users/edit/${user.id}" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem; margin-right: 0.25rem;">Edit</a>
                                    <a href="/users/delete/${user.id}" class="btn btn-danger" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty users}">
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 3rem; color: var(--secondary);">No user accounts found. Register a new user!</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
