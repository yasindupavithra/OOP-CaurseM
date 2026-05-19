package com.course.system.model;

// OOP ANNOTATIONS for Payment.java:
// - Abstraction: 'Payment' is abstract and defines common attributes/methods for all payments.
// - Inheritance: Concrete types 'CardPayment' and 'BankTransferPayment' extend Payment.
// - Polymorphism: Methods like 'getPaymentMethod()' and 'getPaymentDetails()' are overridden to provide type-specific behavior.
// - Encapsulation: Fields are private and accessed via getters/setters; internal payment representation is hidden.

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Payment implements Serializable {
    private String id;
    private String registrationId;
    private double amount;
    private LocalDate paymentDate;
    private String status;

    public Payment() {}

    public Payment(String id, String registrationId, double amount, LocalDate paymentDate, String status) {
        this.id = id;
        this.registrationId = registrationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }
//getter and setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }    //.

    public String getRegistrationId() { return registrationId; }
    public void setRegistrationId(String registrationId) { this.registrationId = registrationId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public abstract String getPaymentMethod();
    public abstract String getPaymentDetails();

    @Override
    public String toString() {
        return id + "|" + registrationId + "|" + amount + "|" + paymentDate + "|" + status + "|" + getPaymentMethod();
    }
}
