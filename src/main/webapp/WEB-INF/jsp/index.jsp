<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Academic Dashboard | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <nav>
        <div class="container">
            <a href="/" class="nav-logo">EduReg System</a>
            <div class="nav-links">
                <a href="/" class="active">Dashboard</a>
                <a href="/courses">Courses</a>
                <a href="/students">Students</a>
                <a href="/instructors">Instructors</a>
                <a href="/registrations">Registrations</a>
                <a href="/payments">Payments</a>
                <a href="/users">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <!-- Dashboard Intro -->
        <div class="card" style="padding: 3rem 2rem; background: linear-gradient(135deg, #4f46e5 0%, #312e81 100%); color: white; border: none; border-radius: 1.25rem; margin-bottom: 2.5rem; text-align: center; box-shadow: 0 20px 25px -5px rgba(79, 70, 229, 0.2);">
            <h1 style="font-size: 2.75rem; margin-bottom: 0.5rem; color: white;">Student Course Registration System</h1>
            <p style="color: #c7d2fe; font-size: 1.15rem; margin-bottom: 2rem; max-width: 700px; margin-left: auto; margin-right: auto;">
                Welcome to EduReg. An enterprise-grade, state-of-the-art university OOP course catalog, enrollment billing, and faculty directory persistence system.
            </p>
            <div style="display: flex; gap: 1rem; justify-content: center;">
                <a href="/registrations/register" class="btn btn-primary" style="background: white; color: var(--primary); font-weight: bold; box-shadow: 0 4px 6px -1px rgba(255,255,255,0.2);">Quick Enroll Student</a>
                <a href="/payments/add" class="btn btn-secondary" style="background: rgba(255, 255, 255, 0.15); color: white; border-color: rgba(255,255,255,0.2);">Record Billing</a>
            </div>
        </div>

        <!-- Rich Interactive Statistics Cards -->
        <h2 style="margin-bottom: 1.5rem; font-size: 1.5rem; font-weight: 600;">System Analytics Summary</h2>
        <div class="grid-4" style="margin-bottom: 3rem;">
            <div class="card" style="border-top: 4px solid var(--primary); margin: 0; padding: 1.5rem;">
                <div style="font-size: 0.85rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Total Student Profiles</div>
                <div style="font-size: 2.25rem; font-weight: bold; margin-top: 0.5rem; font-family: 'Space Grotesk', sans-serif;">${totalStudents}</div>
                <span style="font-size: 0.75rem; color: var(--success); font-weight: 500;">Active accounts in file DB</span>
            </div>
            <div class="card" style="border-top: 4px solid var(--success); margin: 0; padding: 1.5rem;">
                <div style="font-size: 0.85rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Academic Courses</div>
                <div style="font-size: 2.25rem; font-weight: bold; margin-top: 0.5rem; font-family: 'Space Grotesk', sans-serif;">${totalCourses}</div>
                <span style="font-size: 0.75rem; color: var(--primary); font-weight: 500;">Cataloged semesters</span>
            </div>
            <div class="card" style="border-top: 4px solid var(--accent); margin: 0; padding: 1.5rem;">
                <div style="font-size: 0.85rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Active Faculty</div>
                <div style="font-size: 2.25rem; font-weight: bold; margin-top: 0.5rem; font-family: 'Space Grotesk', sans-serif;">${totalInstructors}</div>
                <span style="font-size: 0.75rem; color: var(--text-muted); font-weight: 500;">Permanent & visiting staff</span>
            </div>
            <div class="card" style="border-top: 4px solid var(--danger); margin: 0; padding: 1.5rem;">
                <div style="font-size: 0.85rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Revenue Collected</div>
                <div style="font-size: 1.6rem; font-weight: bold; margin-top: 0.75rem; color: #166534; font-family: 'Space Grotesk', sans-serif;">LKR ${totalRevenueCollected}</div>
                <span style="font-size: 0.75rem; color: var(--success); font-weight: 500;">Tuition fees accounted</span>
            </div>
        </div>

        <!-- 6 Dedicated Management Modules Grid -->
        <h2 style="margin-bottom: 1.5rem; font-size: 1.5rem; font-weight: 600;">System Administrative Modules</h2>
        <div class="grid-3">
            <div class="card" style="margin: 0; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.25rem; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span>👤</span> User Management
                    </h3>
                    <p style="font-size: 0.9rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 1.5rem;">
                        Manage administrative credentials, user records, roles, access levels, and security details.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="/users" class="btn btn-secondary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">View List</a>
                    <a href="/users/register" class="btn btn-primary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">+ Add User</a>
                </div>
            </div>

            <div class="card" style="margin: 0; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.25rem; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span>📚</span> Course Catalog
                    </h3>
                    <p style="font-size: 0.9rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 1.5rem;">
                        Oversee courses, platform designations, zoom/teams integration, onsite classrooms, and credits.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="/courses" class="btn btn-secondary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">View List</a>
                    <a href="/courses/add" class="btn btn-primary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">+ Add Course</a>
                </div>
            </div>

            <div class="card" style="margin: 0; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.25rem; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span>🎓</span> Student Profiles
                    </h3>
                    <p style="font-size: 0.9rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 1.5rem;">
                        Register student demographics, tracking Cumulative GPAs, enrolled majors, and semester years.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="/students" class="btn btn-secondary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">View List</a>
                    <a href="/students/add" class="btn btn-primary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">+ Add Student</a>
                </div>
            </div>

            <div class="card" style="margin: 0; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.25rem; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span>👨‍🏫</span> Faculty Registry
                    </h3>
                    <p style="font-size: 0.9rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 1.5rem;">
                        Compute salaries polymorphically for full-time professors and visiting contract-based instructors.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="/instructors" class="btn btn-secondary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">View List</a>
                    <a href="/instructors/add" class="btn btn-primary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">+ Add Instructor</a>
                </div>
            </div>

            <div class="card" style="margin: 0; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.25rem; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span>📝</span> Enrollments
                    </h3>
                    <p style="font-size: 0.9rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 1.5rem;">
                        Register students for courses, automatically computing tuition fees based on region criteria.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="/registrations" class="btn btn-secondary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">View List</a>
                    <a href="/registrations/register" class="btn btn-primary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">+ Register</a>
                </div>
            </div>

            <div class="card" style="margin: 0; display: flex; flex-direction: column; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.25rem; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span>💳</span> Billing & Payments
                    </h3>
                    <p style="font-size: 0.9rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 1.5rem;">
                        Accept card payments and bank wire receipts, logging transaction reference details with clear states.
                    </p>
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <a href="/payments" class="btn btn-secondary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">View History</a>
                    <a href="/payments/add" class="btn btn-primary" style="flex: 1; padding: 0.5rem; font-size: 0.8rem;">+ Record Pay</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
