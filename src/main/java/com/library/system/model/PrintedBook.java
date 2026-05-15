package com.library.system.model;

public class PrintedBook extends Book {
    private String rackLocation;
    private int numberOfPages;

    public PrintedBook() { super(); }

    public PrintedBook(String id, String title, String author, String genre, String isbn, boolean available, String rackLocation, int numberOfPages) {
        super(id, title, author, genre, isbn, available);
        this.rackLocation = rackLocation;
        this.numberOfPages = numberOfPages;
    }

    public String getRackLocation() { return rackLocation; }
    public void setRackLocation(String rackLocation) { this.rackLocation = rackLocation; }

    public int getNumberOfPages() { return numberOfPages; }
    public void setNumberOfPages(int numberOfPages) { this.numberOfPages = numberOfPages; }

    @Override
    public String getBookType() { return "PRINTED"; }

    @Override
    public String toString() {
        return super.toString() + "|" + rackLocation + "|" + numberOfPages;
    }
}
