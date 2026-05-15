package com.library.system.service;

import com.library.system.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final String FILE_NAME = "reviews.txt";

    @Autowired
    private FileService fileService;

    public void addReview(Review review) throws IOException {
        fileService.appendToFile(FILE_NAME, review.toString());
    }

    public List<Review> getReviewsForBook(String bookId) throws IOException {
        List<String> lines = fileService.readFromFile(FILE_NAME);
        List<Review> reviews = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5 && parts[1].equals(bookId)) {
                reviews.add(new Review(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            }
        }
        return reviews;
    }
}
