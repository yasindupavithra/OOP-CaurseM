package com.course.system.model;

import java.time.LocalDate;

public class CardPayment extends Payment {
    private String cardNumber;
    private String cardType;

    public CardPayment() { super(); }

    public CardPayment(String id, String registrationId, double amount, LocalDate paymentDate, String status, String cardNumber, String cardType) {
        super(id, registrationId, amount, paymentDate, status);
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    @Override
    public String getPaymentMethod() { return "CARD"; }

    @Override
    public String getPaymentDetails() {
        return "Paid via " + cardType + " Card Ending in " + (cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber);
    }

    @Override
    public String toString() {
        return super.toString() + "|" + cardNumber + "|" + cardType;
    }
}
