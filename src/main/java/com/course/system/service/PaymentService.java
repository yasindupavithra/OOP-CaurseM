package com.course.system.service;

// OOP ANNOTATIONS for PaymentService.java:
// - Abstraction: PaymentService encapsulates payment persistence and parsing logic.
// - Polymorphism: when parsing lines, different Payment subclasses are instantiated and returned as Payment references.
// - Encapsulation: callers work with Payment interface without knowing concrete types.

import com.course.system.model.BankTransferPayment;
import com.course.system.model.CardPayment;
import com.course.system.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private static final String FILE_NAME = "payments.txt";

    @Autowired
    private FileService fileService;

    public void addPayment(Payment payment) throws IOException {
        fileService.appendToFile(FILE_NAME, payment.toString());
    }

    public List<Payment> getAllPayments() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Payment> payments = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                String id = parts[0];
                String regId = parts[1];
                double amount = Double.parseDouble(parts[2]);
                LocalDate date = LocalDate.parse(parts[3]);
                String status = parts[4];
                String method = parts[5];

                if (method.equals("CARD")) {
                    String cardNum = parts.length > 6 ? parts[6] : "";
                    String cardType = parts.length > 7 ? parts[7] : "";
                    payments.add(new CardPayment(id, regId, amount, date, status, cardNum, cardType));
                } else {
                    String bankName = parts.length > 6 ? parts[6] : "";
                    String refNum = parts.length > 7 ? parts[7] : "";
                    payments.add(new BankTransferPayment(id, regId, amount, date, status, bankName, refNum));
                }
            }
        }
        return payments;
    }

    public Optional<Payment> getPaymentById(String id) throws IOException {
        return getAllPayments().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public void updatePayment(Payment updated) throws IOException {
        List<Payment> list = getAllPayments();
        List<String> updatedLines = new ArrayList<>();
        for (Payment p : list) {
            if (p.getId().equals(updated.getId())) {
                updatedLines.add(updated.toString());
            } else {
                updatedLines.add(p.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    public void deletePayment(String id) throws IOException {
        List<Payment> list = getAllPayments();
        List<String> updatedLines = new ArrayList<>();
        for (Payment p : list) {
            if (!p.getId().equals(id)) {
                updatedLines.add(p.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
