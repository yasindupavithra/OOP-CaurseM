package com.library.system.service;

import com.library.system.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private static final String FILE_NAME = "borrowed_books.txt";

    @Autowired
    private FileService fileService;

    public void addTransaction(Transaction transaction) throws IOException {
        fileService.appendToFile(FILE_NAME, transaction.toString());
    }

    public List<Transaction> getAllTransactions() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 8) {
                Transaction t = new Transaction();
                t.setId(parts[0]);
                t.setUserId(parts[1]);
                t.setBookId(parts[2]);
                t.setBorrowDate(LocalDate.parse(parts[3]));
                t.setDueDate(LocalDate.parse(parts[4]));
                t.setReturnDate(parts[5].equals("NULL") ? null : LocalDate.parse(parts[5]));
                t.setFineAmount(Double.parseDouble(parts[6]));
                t.setCompleted(Boolean.parseBoolean(parts[7]));
                transactions.add(t);
            }
        }
        return transactions;
    }

    public void updateTransaction(Transaction updatedTransaction) throws IOException {
        List<Transaction> transactions = getAllTransactions();
        List<String> updatedLines = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getId().equals(updatedTransaction.getId())) {
                updatedLines.add(updatedTransaction.toString());
            } else {
                updatedLines.add(t.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
