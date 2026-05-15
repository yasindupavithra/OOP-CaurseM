package com.library.system.model;

public class RegularUser extends User {

    private String membershipType; // e.g., Basic, Premium

    public RegularUser() {
        super();
        setUserType("REGULAR");
    }

    public RegularUser(String id, String username, String password, String email, String membershipType) {
        super(id, username, password, email, "REGULAR");
        this.membershipType = membershipType;
    }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    @Override
    public String getPermissions() {
        return "LIMITED_ACCESS: Borrow and Review Books";
    }

    @Override
    public String toString() {
        return super.toString() + "|" + membershipType;
    }
}
