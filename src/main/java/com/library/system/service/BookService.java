package com.library.system.service;

import com.library.system.model.Book;
import com.library.system.model.EBook;
import com.library.system.model.PrintedBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final String FILE_NAME = "books.txt";

    @Autowired
    private FileService fileService;

    public void addBook(Book book) throws IOException {
        fileService.appendToFile(FILE_NAME, book.toString());
    }

    public List<Book> getAllBooks() throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Book> books = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                String id = parts[0];
                String title = parts[1];
                String author = parts[2];
                String genre = parts[3];
                String isbn = parts[4];
                boolean available = Boolean.parseBoolean(parts[5]);
                String type = parts[6];

                if (type.equals("EBOOK")) {
                    books.add(new EBook(id, title, author, genre, isbn, available, parts[7], Double.parseDouble(parts[8])));
                } else {
                    books.add(new PrintedBook(id, title, author, genre, isbn, available, parts[7], Integer.parseInt(parts[8])));
                }
            }
        }
        return books;
    }

    public Optional<Book> getBookById(String id) throws IOException {
        return getAllBooks().stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public void updateBook(Book updatedBook) throws IOException {
        List<Book> books = getAllBooks();
        List<String> updatedLines = new ArrayList<>();
        for (Book b : books) {
            if (b.getId().equals(updatedBook.getId())) {
                updatedLines.add(updatedBook.toString());
            } else {
                updatedLines.add(b.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }

    public void deleteBook(String id) throws IOException {
        List<Book> books = getAllBooks();
        List<String> updatedLines = new ArrayList<>();
        for (Book b : books) {
            if (!b.getId().equals(id)) {
                updatedLines.add(b.toString());
            }
        }
        fileService.writeToFile(FILE_NAME, updatedLines);
    }
}
