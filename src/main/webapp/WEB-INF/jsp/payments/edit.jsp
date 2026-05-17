<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Payment | EduReg</title>
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
        window.onload = togglePaymentDetails;
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
            <h2 style="margin-bottom: 0.5rem;">Edit Payment details (Update Component)</h2>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Modify details of payment transaction <code>${payment.id}</code>.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="/payments/edit" method="post">
                <input type="hidden" name="id" value="${payment.id}">
                <input type="hidden" name="registrationId" value="${payment.registrationId}">

                <div class="form-group">
                    <label>Amount Collected (LKR) <span style="color: var(--danger);">*</span></label>
                    <input type="number" step="0.01" name="amount" value="${payment.amount}" required>
                </div>
                <div class="form-group">
                    <label>Payment Method <span style="color: var(--danger);">*</span></label>
                    <select name="method" id="method" onchange="togglePaymentDetails()" required>
                        <option value="CARD" ${payment.paymentMethod == 'CARD' ? 'selected' : ''}>Credit / Debit Card</option>
                        <option value="BANK_TRANSFER" ${payment.paymentMethod == 'BANK_TRANSFER' ? 'selected' : ''}>Bank Wire Transfer</option>
                    </select>
                </div>

                <!-- Card Details -->
                <div id="card-fields">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Card Number (16-digits)</label>
                            <input type="text" name="cardNumber" value="${payment.paymentMethod == 'CARD' ? payment.cardNumber : ''}" placeholder="4111222233334444">
                        </div>
                        <div class="form-group">
                            <label>Card Network Type</label>
                            <select name="cardType">
                                <option value="Visa" ${payment.paymentMethod == 'CARD' && payment.cardType == 'Visa' ? 'selected' : ''}>Visa</option>
                                <option value="MasterCard" ${payment.paymentMethod == 'CARD' && payment.cardType == 'MasterCard' ? 'selected' : ''}>MasterCard</option>
                                <option value="Amex" ${payment.paymentMethod == 'CARD' && payment.cardType == 'Amex' ? 'selected' : ''}>Amex</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Bank Transfer Details -->
                <div id="bank-fields" style="display: none;">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Receiving Bank Name</label>
                            <input type="text" name="bankName" value="${payment.paymentMethod == 'BANK_TRANSFER' ? payment.bankName : ''}" placeholder="e.g. Commercial Bank">
                        </div>
                        <div class="form-group">
                            <label>Transaction Reference #</label>
                            <input type="text" name="referenceNumber" value="${payment.paymentMethod == 'BANK_TRANSFER' ? payment.referenceNumber : ''}" placeholder="e.g. TXN102938">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label>Status <span style="color: var(--danger);">*</span></label>
                    <select name="status">
                        <option value="PAID" ${payment.status == 'PAID' ? 'selected' : ''}>Paid</option>
                        <option value="REFUNDED" ${payment.status == 'REFUNDED' ? 'selected' : ''}>Refunded</option>
                    </select>
                </div>

                <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Update Payment Record</button>
                    <a href="/payments" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
