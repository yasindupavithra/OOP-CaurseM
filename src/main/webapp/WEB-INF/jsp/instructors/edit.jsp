<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Instructor | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
    <script>
        function toggleEmploymentFields() {
            const type = document.getElementById('type').value;
            const permFields = document.getElementById('permanent-fields');
            const visitFields = document.getElementById('visiting-fields');
            
            if (type === 'PERMANENT') {
                permFields.style.display = 'block';
                visitFields.style.display = 'none';
            } else {
                permFields.style.display = 'none';
                visitFields.style.display = 'block';
            }
        }
        window.onload = toggleEmploymentFields;
    </script>
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

    <div class="container" style="max-width: 650px;">
        <div class="card">
            <h2 style="margin-bottom: 0.5rem;">Edit Instructor Details (Update Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Modify details of instructor <code>${instructor.id}</code>.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/instructors/edit" method="post">
                <input type="hidden" name="id" value="${instructor.id}">

                <div class="form-group">
                    <label>Instructor Full Name <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="name" value="${instructor.name}" required>
                </div>
                <div class="form-group">
                    <label>Email Address <span style="color: var(--danger);">*</span></label>
                    <input type="email" name="email" value="${instructor.email}" required>
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Department <span style="color: var(--danger);">*</span></label>
                        <input type="text" name="department" value="${instructor.department}" required>
                    </div>
                    <div class="form-group">
                        <label>Specialization <span style="color: var(--danger);">*</span></label>
                        <input type="text" name="specialization" value="${instructor.specialization}" required>
                    </div>
                </div>

                <div class="form-group">
                    <label>Employment Type <span style="color: var(--danger);">*</span></label>
                    <select name="type" id="type" onchange="toggleEmploymentFields()">
                        <option value="PERMANENT" ${instructor.employmentType == 'PERMANENT' ? 'selected' : ''}>Permanent Faculty</option>
                        <option value="VISITING" ${instructor.employmentType == 'VISITING' ? 'selected' : ''}>Visiting Lecturer</option>
                    </select>
                </div>

                <!-- Permanent Fields -->
                <div id="permanent-fields">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Monthly Base Salary (LKR)</label>
                            <input type="number" step="0.01" name="monthlySalary" value="${instructor.employmentType == 'PERMANENT' ? instructor.monthlySalary : 120000.0}">
                        </div>
                        <div class="form-group">
                            <label>Allowance (LKR)</label>
                            <input type="number" step="0.01" name="allowance" value="${instructor.employmentType == 'PERMANENT' ? instructor.allowance : 20000.0}">
                        </div>
                    </div>
                </div>

                <!-- Visiting Fields -->
                <div id="visiting-fields" style="display: none;">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Hourly Payment Rate (LKR)</label>
                            <input type="number" step="0.01" name="hourlyRate" value="${instructor.employmentType == 'VISITING' ? instructor.hourlyRate : 3000.0}">
                        </div>
                        <div class="form-group">
                            <label>Hours Taught</label>
                            <input type="number" step="0.1" name="hoursTaught" value="${instructor.employmentType == 'VISITING' ? instructor.hoursTaught : 40}">
                        </div>
                    </div>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Update Instructor Details</button>
                    <a href="/instructors" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
