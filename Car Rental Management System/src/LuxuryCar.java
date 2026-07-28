public class LuxuryCar extends Car {

    private double insuranceFee;
    private final int minimumRentalDays;

    public LuxuryCar(int id, String brand, String model,
                     int year, double pricePerDay, double insuranceFee) {

        super(id, brand, model, year, pricePerDay);
        this.insuranceFee = insuranceFee;
        this.minimumRentalDays = 3;
    }



    public double getInsuranceFee() {
        return insuranceFee;
    }

    public int getMinimumRentalDays() {
        return minimumRentalDays;
    }



    public void setInsuranceFee(double insuranceFee) {
        if (insuranceFee >= 0) {
            this.insuranceFee = insuranceFee;
        }
    }



    @Override
    public double calculateRentalCost(int days) {

        double subtotal = (days * getPricePerDay()) + insuranceFee;

        return subtotal + (subtotal * getTaxRate());
    }

    @Override
    public String toString() {

        return super.toString()
                + "\nType: Luxury Car"
                + "\nInsurance Fee: " + insuranceFee
                + "\nMinimum Rental Days: " + minimumRentalDays;
    }

}