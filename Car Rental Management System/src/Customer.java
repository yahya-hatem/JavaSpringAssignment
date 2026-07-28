public class Customer {

    private int id;
    private String name;
    private String phone;

    private int rentedCarId;
    private int rentedDays;

    private double totalPaid;

    private static int count = 0;

    public Customer(int id, String name, String phone) {

        this.id = id;
        this.name = name;
        this.phone = phone;

        rentedCarId = -1;
        rentedDays = 0;
        totalPaid = 0;

        count++;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getRentedCarId() {
        return rentedCarId;
    }

    public int getRentedDays() {
        return rentedDays;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public static int getCount() {
        return count;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRentedCarId(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    public void setRentedDays(int rentedDays) {
        this.rentedDays = rentedDays;
    }



    public void addPayment(double amount) {
        totalPaid += amount;
    }

    public boolean hasCar() {
        return rentedCarId != -1;
    }

    public void clearRental() {
        rentedCarId = -1;
        rentedDays = 0;
    }

    @Override
    public String toString() {

        return "Customer ID: " + id
                + "\nName: " + name
                + "\nPhone: " + phone
                + "\nCurrent Car ID: "
                + (rentedCarId == -1 ? "None" : rentedCarId)
                + "\nRental Days: " + rentedDays
                + "\nTotal Paid: " + totalPaid;
    }

}