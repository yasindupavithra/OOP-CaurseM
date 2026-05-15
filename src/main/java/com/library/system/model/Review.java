package com.library.system.model;

public class Review {
    private String id;
    private String bookId;
    private String username;
    private String comment;
    private int rating;

    public Review() {}

    public Review(String id, String bookId, String username, String comment, int rating) {
        this.id = id;
        this.bookId = bookId;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    @Override
    public String toString() {
        return id + "|" + bookId + "|" + username + "|" + comment + "|" + rating;
    }
}
