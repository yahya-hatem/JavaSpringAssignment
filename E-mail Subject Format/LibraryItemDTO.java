package com.library.dto;

import com.library.model.ItemStatus;

public class LibraryItemDTO {
    private String catalogueId;
    private String category;
    private String title;
    private ItemStatus status;
    private String borrowerName;
    private int loanPeriodDays;
    private double oneDayFineCost;

    public LibraryItemDTO() {}

    public LibraryItemDTO(String catalogueId, String category, String title,
                          ItemStatus status, String borrowerName,
                          int loanPeriodDays, double oneDayFineCost) {
        this.catalogueId = catalogueId;
        this.category = category;
        this.title = title;
        this.status = status;
        this.borrowerName = borrowerName;
        this.loanPeriodDays = loanPeriodDays;
        this.oneDayFineCost = oneDayFineCost;
    }

    // Getters and Setters
    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public int getLoanPeriodDays() {
        return loanPeriodDays;
    }

    public void setLoanPeriodDays(int loanPeriodDays) {
        this.loanPeriodDays = loanPeriodDays;
    }

    public double getOneDayFineCost() {
        return oneDayFineCost;
    }

    public void setOneDayFineCost(double oneDayFineCost) {
        this.oneDayFineCost = oneDayFineCost;
    }
}
