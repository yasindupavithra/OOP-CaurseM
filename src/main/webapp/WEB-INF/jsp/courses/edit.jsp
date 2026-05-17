<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Course | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
    <script>
        function toggleFields() {
            const type = document.getElementById('type').value;
            const onsiteFields = document.getElementById('onsite-fields');
            const onlineFields = document.getElementById('online-fields');
            
            if (type === 'ONSITE') {
                onsiteFields.style.display = 'block';
                onlineFields.style.display = 'none';
            } else {
                onsiteFields.style.display = 'none';
                onlineFields.style.display = 'block';
            }
        }
        window.onload = toggleFields;
    </script>
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

    <div class="container" style="max-width: 650px;">
        <div class="card">
            <h2 style="margin-bottom: 0.5rem;">Edit Course Details (Update Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Modify details of course code <code>${course.courseCode}</code>.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/courses/edit" method="post">
                <input type="hidden" name="id" value="${course.id}">

                <div class="form-group">
                    <label>Course Title <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="title" value="${course.title}" required>
                </div>
                <div class="form-group">
                    <label>Instructor Name <span style="color: var(--danger);">*</span></label>
                    <input type="text" name="instructor" value="${course.instructor}" required>
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label>Course Code <span style="color: var(--danger);">*</span></label>
                        <input type="text" name="code" value="${course.courseCode}" required>
                    </div>
                    <div class="form-group">
                        <label>Credits <span style="color: var(--danger);">*</span></label>
                        <input type="number" name="credits" value="${course.credits}" min="1" max="6" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>Course Type <span style="color: var(--danger);">*</span></label>
                    <select name="type" id="type" onchange="toggleFields()">
                        <option value="ONSITE" ${course.courseType == 'ONSITE' ? 'selected' : ''}>On-Site Course</option>
                        <option value="ONLINE" ${course.courseType == 'ONLINE' ? 'selected' : ''}>Online Course</option>
                    </select>
                </div>

                <div id="onsite-fields">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Room Number</label>
                            <input type="text" name="room" value="${course.courseType == 'ONSITE' ? course.roomNumber : ''}" placeholder="e.g. A-302">
                        </div>
                        <div class="form-group">
                            <label>Campus Location</label>
                            <input type="text" name="location" value="${course.courseType == 'ONSITE' ? course.campusLocation : ''}" placeholder="e.g. Main Campus">
                        </div>
                    </div>
                </div>

                <div id="online-fields" style="display: none;">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Platform (Zoom/Teams)</label>
                            <input type="text" name="platform" value="${course.courseType == 'ONLINE' ? course.platform : ''}" placeholder="e.g. Zoom">
                        </div>
                        <div class="form-group">
                            <label>Meeting Link</label>
                            <input type="text" name="link" value="${course.courseType == 'ONLINE' ? course.meetingLink : ''}" placeholder="e.g. https://zoom.us/...">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label>Status</label>
                    <select name="openForRegistration">
                        <option value="true" ${course.openForRegistration ? 'selected' : ''}>Open for Registration</option>
                        <option value="false" ${!course.openForRegistration ? 'selected' : ''}>Closed for Registration</option>
                    </select>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Update Course</button>
                    <a href="/courses" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
