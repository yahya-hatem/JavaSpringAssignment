package com.library.dto;

public class LibraryReportDTO {
    private int catalogueSize;
    private int itemsOnLoan;
    private double loanRate;
    private double totalOutstandingFines;
    private double projectedFines;

    public LibraryReportDTO() {}

    public LibraryReportDTO(int catalogueSize, int itemsOnLoan, double loanRate,
                            double totalOutstandingFines, double projectedFines) {
        this.catalogueSize = catalogueSize;
        this.itemsOnLoan = itemsOnLoan;
        this.loanRate = loanRate;
        this.totalOutstandingFines = totalOutstandingFines;
        this.projectedFines = projectedFines;
    }

    public int getCatalogueSize() {
        return catalogueSize;
    }

    public void setCatalogueSize(int catalogueSize) {
        this.catalogueSize = catalogueSize;
    }

    public int getItemsOnLoan() {
        return itemsOnLoan;
    }

    public void setItemsOnLoan(int itemsOnLoan) {
        this.itemsOnLoan = itemsOnLoan;
    }

    public double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(double loanRate) {
        this.loanRate = loanRate;
    }

    public double getTotalOutstandingFines() {
        return totalOutstandingFines;
    }

    public void setTotalOutstandingFines(double totalOutstandingFines) {
        this.totalOutstandingFines = totalOutstandingFines;
    }

    public double getProjectedFines() {
        return projectedFines;
    }

    public void setProjectedFines(double projectedFines) {
        this.projectedFines = projectedFines;
    }

    @Override
    public String toString() {
        return String.format(
                "\n====== BAYT AL HEKMA LIBRARY REPORT ======\n" +
                "Catalogue Size: %d items\n" +
                "Items on Loan: %d\n" +
                "Loan Rate: %.2f%%\n" +
                "Total Outstanding Fines: %.2f EGP\n" +
                "Projected Fines (5-day overdue): %.2f EGP\n" +
                "==========================================\n",
                catalogueSize, itemsOnLoan, loanRate, totalOutstandingFines, projectedFines
        );
    }
}
