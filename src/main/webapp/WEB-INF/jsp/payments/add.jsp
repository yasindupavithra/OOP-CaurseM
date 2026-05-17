<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Record Payment | EduReg</title>
    <link rel="stylesheet" href="/css/style.css">
    <script>
        function togglePaymentDetails() {
            const method = document.getElementById('method').value;
            const cardFields = document.getElementById('card-fields');
            const bankFields = document.getElementById('bank-fields');
            
            if (method === 'CARD') {
                cardFields.style.display = 'block';
                bankFields.style.display = 'none';
            } else {
                cardFields.style.display = 'none';
                bankFields.style.display = 'block';
            }
        }
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
                <a href="/instructors">Instructors</a>
                <a href="/registrations">Registrations</a>
                <a href="/payments" class="active">Payments</a>
                <a href="/users">System Users</a>
            </div>
        </div>
    </nav>

    <div class="container" style="max-width: 650px;">
        <div class="card">
            <h2 style="margin-bottom: 0.5rem;">Record Tuition Payment (Create Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Log a credit/debit card charge or reference bank transfer details.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/payments/add" method="post">
                <div class="form-group">
                    <label>Select Course Enrollment Reference <span style="color: var(--danger);">*</span></label>
                    <select name="registrationId" required>
                        <c:forEach var="reg" items="${registrations}">
                            <option value="${reg.id}">Ref: ${reg.id} - Student: ${reg.studentId} - Course: ${reg.courseId} (Fee: LKR ${reg.registrationFee})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Amount Collected (LKR) <span style="color: var(--danger);">*</span></label>
                    <input type="number" step="0.01" name="amount" value="5000.0" required>
                </div>
                <div class="form-group">
                    <label>Payment Mode <span style="color: var(--danger);">*</span></label>
                    <select name="method" id="method" onchange="togglePaymentDetails()" required>
                        <option value="CARD">Credit / Debit Card</option>
                        <option value="BANK_TRANSFER">Bank Wire Transfer</option>
                    </select>
                </div>

                <!-- Card Details -->
                <div id="card-fields">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Card Number (16-digits)</label>
                            <input type="text" name="cardNumber" maxlength="16" placeholder="4111222233334444">
                        </div>
                        <div class="form-group">
                            <label>Card Network Type</label>
                            <select name="cardType">
                                <option value="Visa">Visa</option>
                                <option value="MasterCard">MasterCard</option>
                                <option value="Amex">Amex</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Bank Transfer Details -->
                <div id="bank-fields" style="display: none;">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Receiving Bank Name</label>
                            <input type="text" name="bankName" placeholder="e.g. Commercial Bank">
                        </div>
                        <div class="form-group">
                            <label>Transaction Reference #</label>
                            <input type="text" name="referenceNumber" placeholder="e.g. TXN102938">
                        </div>
                    </div>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Register Payment</button>
                    <a href="/payments" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
