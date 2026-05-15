package com.library.system.model;

public class PermanentAuthor extends Author {
    private int contractYears;

    public PermanentAuthor() { super(); }

    public PermanentAuthor(String id, String name, String bio, int contractYears) {
        super(id, name, bio);
        this.contractYears = contractYears;
    }

    public int getContractYears() { return contractYears; }
    public void setContractYears(int contractYears) { this.contractYears = contractYears; }

    @Override
    public String getAuthorCategory() { return "PERMANENT"; }

    @Override
    public String toString() {
        return super.toString() + "|" + contractYears;
    }
}
