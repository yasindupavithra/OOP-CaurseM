package com.library.system.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private String id;
    private String userId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private boolean completed;

    public Transaction() {}

    public Transaction(String id, String userId, String bookId, LocalDate borrowDate, LocalDate dueDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.completed = false;
        this.fineAmount = 0.0;
    }

    // Encapsulation
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Polymorphism / Logic: Fine calculation
    public void calculateFine(String userType) {
        if (returnDate == null) return;
        
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (daysLate > 0) {
            double rate = userType.equalsIgnoreCase("PREMIUM") ? 5.0 : 10.0;
            this.fineAmount = daysLate * rate;
        } else {
            this.fineAmount = 0.0;
        }
    }

    @Override
    public String toString() {
        return id + "|" + userId + "|" + bookId + "|" + borrowDate + "|" + dueDate + "|" + (returnDate != null ? returnDate : "NULL") + "|" + fineAmount + "|" + completed;
    }
}
