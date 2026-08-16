package com.library.dto;

public class FineResultDTO {
    private String itemId;
    private String membershipId;
    private int daysOverdue;
    private double baseFine;
    private double waiverAmount;
    private double administrativeCharge;
    private double totalCharged;
    private double newBalance;

    public FineResultDTO() {}

    public FineResultDTO(String itemId, String membershipId, int daysOverdue,
                         double baseFine, double waiverAmount, double administrativeCharge,
                         double totalCharged, double newBalance) {
        this.itemId = itemId;
        this.membershipId = membershipId;
        this.daysOverdue = daysOverdue;
        this.baseFine = baseFine;
        this.waiverAmount = waiverAmount;
        this.administrativeCharge = administrativeCharge;
        this.totalCharged = totalCharged;
        this.newBalance = newBalance;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public double getBaseFine() {
        return baseFine;
    }

    public double getWaiverAmount() {
        return waiverAmount;
    }

    public double getAdministrativeCharge() {
        return administrativeCharge;
    }

    public double getTotalCharged() {
        return totalCharged;
    }

    public double getNewBalance() {
        return newBalance;
    }
}
