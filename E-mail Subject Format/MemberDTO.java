package com.library.dto;

import com.library.model.MembershipType;

public class MemberDTO {
    private String membershipId;
    private String name;
    private MembershipType category;
    private double balanceOwed;
    private int itemsHeld;

    public MemberDTO() {}

    public MemberDTO(String membershipId, String name, MembershipType category, 
                     double balanceOwed, int itemsHeld) {
        this.membershipId = membershipId;
        this.name = name;
        this.category = category;
        this.balanceOwed = balanceOwed;
        this.itemsHeld = itemsHeld;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MembershipType getCategory() {
        return category;
    }

    public void setCategory(MembershipType category) {
        this.category = category;
    }

    public double getBalanceOwed() {
        return balanceOwed;
    }

    public void setBalanceOwed(double balanceOwed) {
        this.balanceOwed = balanceOwed;
    }

    public int getItemsHeld() {
        return itemsHeld;
    }

    public void setItemsHeld(int itemsHeld) {
        this.itemsHeld = itemsHeld;
    }
}
