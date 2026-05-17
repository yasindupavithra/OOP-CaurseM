<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Payment History | EduReg</title>
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
                <a href="/payments" class="active">Payments</a>
                <a href="/users">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="margin: 0; font-size: 2.25rem;">Payment Registry (Read Component)</h1>
                <p style="color: var(--text-muted); margin: 0.25rem 0 0 0;">Manage academic tuition receipts, card payments, and bank transfer receipts</p>
            </div>
            <div style="display: flex; gap: 0.75rem; align-items: center;">
                <form action="/payments" method="get" style="display: flex; gap: 0.5rem; margin: 0;">
                    <input type="text" name="search" value="${search}" placeholder="Search registry, method..." style="padding: 0.625rem 1rem; min-width: 260px; margin: 0;">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <c:if test="${not empty search}">
                        <a href="/payments" class="btn btn-secondary">Clear</a>
                    </c:if>
                </form>
                <a href="/payments/add" class="btn btn-primary" style="background: linear-gradient(135deg, var(--primary) 0%, #312e81 100%);">+ Record Payment</a>
            </div>
        </div>

        <div class="card" style="padding: 0; overflow: hidden;">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Receipt ID</th>
                            <th>Enrollment Reference</th>
                            <th>Amount Collected</th>
                            <th>Payment Date</th>
                            <th>Payment Method</th>
                            <th>Polymorphic Details / Receipts</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pay" items="${payments}">
                            <tr>
                                <td><code style="background-color: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-weight: bold; color: var(--primary);">${pay.id}</code></td>
                                <td><strong>${pay.registrationId}</strong></td>
                                <td><span style="font-weight: bold; color: #166534;">LKR ${pay.amount}</span></td>
                                <td>${pay.paymentDate}</td>
                                <td>
                                    <span class="badge ${pay.paymentMethod == 'CARD' ? 'badge-info' : 'badge-warning'}">
                                        ${pay.paymentMethod}
                                    </span>
                                </td>
                                <td>
                                    <span style="font-size: 0.9rem; font-weight: 500; color: var(--text-muted);">
                                        ${pay.paymentDetails}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${pay.status == 'PAID' ? 'badge-success' : 'badge-danger'}">
                                        ${pay.status}
                                    </span>
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="/payments/edit/${pay.id}" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem; margin-right: 0.25rem;">Edit</a>
                                    <a href="/payments/delete/${pay.id}" class="btn btn-danger" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">Refund</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty payments}">
                            <tr>
                                <td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">No payment receipts recorded in file database.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
