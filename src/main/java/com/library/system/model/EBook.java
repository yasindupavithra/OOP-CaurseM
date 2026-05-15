package com.library.system.model;

public class EBook extends Book {
    private String downloadUrl;
    private double fileSizeMb;

    public EBook() { super(); }

    public EBook(String id, String title, String author, String genre, String isbn, boolean available, String downloadUrl, double fileSizeMb) {
        super(id, title, author, genre, isbn, available);
        this.downloadUrl = downloadUrl;
        this.fileSizeMb = fileSizeMb;
    }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    public double getFileSizeMb() { return fileSizeMb; }
    public void setFileSizeMb(double fileSizeMb) { this.fileSizeMb = fileSizeMb; }

    @Override
    public String getBookType() { return "EBOOK"; }

    @Override
    public String toString() {
        return super.toString() + "|" + downloadUrl + "|" + fileSizeMb;
    }
}
