<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit User | EduReg</title>
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

    <div class="container" style="max-width: 650px;">
        <div class="card">
            <h2 style="margin-bottom: 0.5rem;">Edit User Details (Update Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Modify details of system user <code>${user.id}</code>.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/users/edit" method="post">
                <input type="hidden" name="id" value="${user.id}">

                <div class="form-group">
                    <label>Username <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="username" value="${user.username}" required>
                </div>
                <div class="form-group">
                    <label>Email Address <span style="color: var(--danger);">*</span></label>
                    <input type="email" name="email" value="${user.email}" required>
                </div>
                <div class="form-group">
                    <label>Password <span style="color: var(--danger);">*</span></label>
                    <input type="password" name="password" value="${user.password}" required>
                </div>
                <div class="form-group">
                    <label>User Role Type <span style="color: var(--danger);">*</span></label>
                    <select name="type" id="type" onchange="document.getElementById('student-group').style.display = this.value === 'STUDENT' ? 'block' : 'none'" required>
                        <option value="STUDENT" ${user.userType == 'STUDENT' ? 'selected' : ''}>Student Role</option>
                        <option value="ADMIN" ${user.userType == 'ADMIN' ? 'selected' : ''}>Administrator Role</option>
                    </select>
                </div>

                <div id="student-group" class="form-group" style="display: ${user.userType == 'STUDENT' ? 'block' : 'none'};">
                    <label>Degree Program</label>
                    <input type="text" name="program" value="${user.userType == 'STUDENT' ? user.degreeProgram : ''}" placeholder="e.g. B.Sc. in Computer Science">
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Update Account</button>
                    <a href="/users" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
