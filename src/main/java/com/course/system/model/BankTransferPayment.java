package com.course.system.model;

import java.time.LocalDate;

public class BankTransferPayment extends Payment {
    private String bankName;
    private String referenceNumber;

    public BankTransferPayment() { super(); }
//inheritance is used.
    public BankTransferPayment(String id, String registrationId, double amount, LocalDate paymentDate, String status, String bankName, String referenceNumber) {
        super(id, registrationId, amount, paymentDate, status);
        this.bankName = bankName;
        this.referenceNumber = referenceNumber;
    }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    @Override
    public String getPaymentMethod() { return "BANK_TRANSFER"; }

    @Override
    public String getPaymentDetails() {
        return "Bank Transfer: " + bankName + " (Ref: " + referenceNumber + ")";
    }

    @Override
    public String toString() {
        return super.toString() + "|" + bankName + "|" + referenceNumber;
    }
}
