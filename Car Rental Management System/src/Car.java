public class Car {

    private int id;
    private String brand;
    private String model;
    private int year;
    private double pricePerDay;
    private boolean available;

    private static int carCount = 0;
    private static final double TAX_RATE = 0.14;


    public Car(int id, String brand, String model, int year) {
        this(id, brand, model, year, 0);
    }

    public Car(int id, String brand, String model, int year, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.available = true;
        carCount++;
    }


    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public static int getCarCount() {
        return carCount;
    }

    public static double getTaxRate() {
        return TAX_RATE;
    }



    public void setPricePerDay(double pricePerDay) {
        if (pricePerDay > 0)
            this.pricePerDay = pricePerDay;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }



    public double calculateRentalCost(int days) {
        double subtotal = days * pricePerDay;
        return subtotal + (subtotal * TAX_RATE);
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nBrand: " + brand +
                "\nModel: " + model +
                "\nYear: " + year +
                "\nPrice/Day: " + pricePerDay +
                "\nAvailable: " + (available ? "Yes" : "No");
    }

}