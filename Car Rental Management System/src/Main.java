import java.util.Scanner;

public class Main {

    public static final int MAX_CARS = 20;
    public static final int MAX_CUSTOMERS = 20;

    static Car[] cars = new Car[MAX_CARS];
    static Customer[] customers = new Customer[MAX_CUSTOMERS];

    static int carCount = 0;
    static int customerCount = 0;

    static double totalIncome = 0;

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        System.out.println("========================================");
        System.out.println("      SPEEDWAY RENTALS SYSTEM");
        System.out.println("========================================");

        do {

            System.out.println("\n1. Add Regular Car");
            System.out.println("2. Add Luxury Car");
            System.out.println("3. Add Customer");
            System.out.println("4. Display All Cars");
            System.out.println("5. Display Available Cars");
            System.out.println("6. Rent a Car");
            System.out.println("7. Return a Car");
            System.out.println("8. Search Car by ID");
            System.out.println("9. Search Car by Brand");
            System.out.println("10. Display All Customers");
            System.out.println("11. Display Statistics");
            System.out.println("0. Exit");

            System.out.print("Enter Choice : ");
            choice = input.nextInt();

            switch (choice) {

                case 1:
                    addRegularCar();
                    break;

                case 2:
                    addLuxuryCar();
                    break;

                case 3:
                    addCustomer();
                    break;

                case 4:
                    displayAllCars();
                    break;

                case 5:
                    displayAvailableCars();
                    break;

                case 6:
                    rentCar();
                    break;

                case 7:
                    returnCar();
                    break;

                case 8:
                    searchCarByID();
                    break;

                case 9:
                    searchCarByBrand();
                    break;

                case 10:
                    displayCustomers();
                    break;

                case 11:
                    displayStatistics();
                    break;

                case 0:

                    System.out.println("\nGood Bye!");

                    System.out.println("Total Cars : "
                            + Car.getCarCount());

                    System.out.println("Total Customers : "
                            + Customer.getCount());

                    System.out.println("Total Income : "
                            + totalIncome);

                    break;

                default:
                    System.out.println("Invalid Choice.");

            }

        } while (choice != 0);

    }

    public static Car findCar(int id) {

        for (int i = 0; i < carCount; i++) {

            if (cars[i].getId() == id)
                return cars[i];

        }

        return null;

    }

    public static Customer findCustomer(int id) {

        for (int i = 0; i < customerCount; i++) {

            if (customers[i].getId() == id)
                return customers[i];

        }

        return null;

    }

    public static void addRegularCar() {

        if (carCount == MAX_CARS) {
            System.out.println("Fleet is full.");
            return;
        }

        System.out.print("Enter Car ID: ");
        int id = input.nextInt();

        if (findCar(id) != null) {
            System.out.println("Car ID already exists.");
            return;
        }

        input.nextLine();

        System.out.print("Enter Brand: ");
        String brand = input.nextLine();

        System.out.print("Enter Model: ");
        String model = input.nextLine();

        System.out.print("Enter Year: ");
        int year = input.nextInt();

        if (year < 1990 || year > 2026) {
            System.out.println("Invalid manufacturing year.");
            return;
        }

        System.out.print("Enter Price Per Day: ");
        double price = input.nextDouble();

        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        cars[carCount] = new Car(id, brand, model, year, price);
        carCount++;

        System.out.println("Regular car added successfully.");
    }

    public static void addLuxuryCar() {

        if (carCount == MAX_CARS) {
            System.out.println("Fleet is full.");
            return;
        }

        System.out.print("Enter Car ID: ");
        int id = input.nextInt();

        if (findCar(id) != null) {
            System.out.println("Car ID already exists.");
            return;
        }

        input.nextLine();

        System.out.print("Enter Brand: ");
        String brand = input.nextLine();

        System.out.print("Enter Model: ");
        String model = input.nextLine();

        System.out.print("Enter Year: ");
        int year = input.nextInt();

        if (year < 1990 || year > 2026) {
            System.out.println("Invalid manufacturing year.");
            return;
        }

        System.out.print("Enter Price Per Day: ");
        double price = input.nextDouble();

        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        System.out.print("Enter Insurance Fee: ");
        double insurance = input.nextDouble();

        if (insurance < 0) {
            System.out.println("Insurance fee cannot be negative.");
            return;
        }

        cars[carCount] =
                new LuxuryCar(id, brand, model, year, price, insurance);

        carCount++;

        System.out.println("Luxury car added successfully.");
    }

    public static void addCustomer() {

        if (customerCount == MAX_CUSTOMERS) {
            System.out.println("Customer list is full.");
            return;
        }

        System.out.print("Enter Customer ID: ");
        int id = input.nextInt();

        if (findCustomer(id) != null) {
            System.out.println("Customer ID already exists.");
            return;
        }

        input.nextLine();

        System.out.print("Enter Name: ");
        String name = input.nextLine();

        System.out.print("Enter Phone: ");
        String phone = input.nextLine();

        customers[customerCount] = new Customer(id, name, phone);

        customerCount++;

        System.out.println("Customer added successfully.");
    }

    public static void displayAllCars() {

        if (carCount == 0) {
            System.out.println("No cars found.");
            return;
        }

        System.out.println("\n========== ALL CARS ==========");

        for (int i = 0; i < carCount; i++) {

            System.out.println("\nCar #" + (i + 1));
            System.out.println(cars[i]);
            System.out.println("---------------------------");

        }

    }

    public static void displayAvailableCars() {

        int count = 0;

        System.out.println("\n===== AVAILABLE CARS =====");

        for (int i = 0; i < carCount; i++) {

            if (cars[i].isAvailable()) {

                System.out.println(cars[i]);
                System.out.println("----------------------");

                count++;

            }

        }

        if (count == 0)
            System.out.println("No available cars.");

        else
            System.out.println("Total Available Cars : " + count);

    }

    public static void searchCarByID() {

        System.out.print("Enter Car ID : ");
        int id = input.nextInt();

        Car car = findCar(id);

        if (car == null)
            System.out.println("Car not found.");

        else
            System.out.println(car);

    }

    public static void searchCarByBrand() {

        input.nextLine();

        System.out.print("Enter Brand : ");
        String brand = input.nextLine();

        int count = 0;

        for (int i = 0; i < carCount; i++) {

            if (cars[i].getBrand().equalsIgnoreCase(brand)) {

                System.out.println(cars[i]);
                System.out.println("----------------------");

                count++;

            }

        }

        if (count == 0)
            System.out.println("No cars with this brand.");

        else
            System.out.println("Matches : " + count);

    }

    public static void rentCar() {

        System.out.print("Enter Customer ID: ");
        int customerId = input.nextInt();

        Customer customer = findCustomer(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (customer.hasCar()) {
            System.out.println("Customer already has a rented car.");
            return;
        }

        System.out.print("Enter Car ID: ");
        int carId = input.nextInt();

        Car car = findCar(carId);

        if (car == null) {
            System.out.println("Car not found.");
            return;
        }

        if (!car.isAvailable()) {
            System.out.println("Car is already rented.");
            return;
        }

        System.out.print("Enter Number of Days: ");
        int days = input.nextInt();

        if (days <= 0) {
            System.out.println("Invalid number of days.");
            return;
        }

        if (car instanceof LuxuryCar) {

            LuxuryCar luxury = (LuxuryCar) car;

            if (days < luxury.getMinimumRentalDays()) {
                System.out.println("Luxury cars must be rented for at least "
                        + luxury.getMinimumRentalDays() + " days.");
                return;
            }

        }

        double cost = car.calculateRentalCost(days);

        car.setAvailable(false);

        customer.setRentedCarId(car.getId());
        customer.setRentedDays(days);
        customer.addPayment(cost);

        totalIncome += cost;

        System.out.println("\n========== RENT RECEIPT ==========");
        System.out.println("Customer : " + customer.getName());
        System.out.println("Car : " + car.getBrand() + " " + car.getModel());
        System.out.println("Days : " + days);
        System.out.println("Total Cost : " + cost);
    }

    public static void returnCar() {

        System.out.print("Enter Customer ID: ");
        int customerId = input.nextInt();

        Customer customer = findCustomer(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (!customer.hasCar()) {
            System.out.println("Customer has no rented car.");
            return;
        }

        Car car = findCar(customer.getRentedCarId());

        if (car != null) {

            car.setAvailable(true);

            System.out.println("Returned Car : "
                    + car.getBrand() + " " + car.getModel());

        }

        customer.clearRental();

    }

    public static void displayCustomers() {

        if (customerCount == 0) {

            System.out.println("No customers.");
            return;

        }

        for (int i = 0; i < customerCount; i++) {

            Customer c = customers[i];

            System.out.println("--------------------------");
            System.out.println("ID : " + c.getId());
            System.out.println("Name : " + c.getName());
            System.out.println("Phone : " + c.getPhone());

            if (c.hasCar()) {

                Car car = findCar(c.getRentedCarId());

                if (car != null)

                    System.out.println("Current Car : "
                            + car.getBrand() + " "
                            + car.getModel());

            } else {

                System.out.println("Current Car : None");

            }

        }

    }

    public static void displayStatistics() {

        System.out.println("\n========== STATISTICS ==========");

        System.out.println("Total Income : " + totalIncome);

        int rented = 0;

        double totalPrice = 0;

        Car expensive = null;

        for (int i = 0; i < carCount; i++) {

            if (!cars[i].isAvailable())
                rented++;

            totalPrice += cars[i].getPricePerDay();

            if (expensive == null
                    || cars[i].getPricePerDay() >
                    expensive.getPricePerDay()) {

                expensive = cars[i];

            }

        }

        System.out.println("Rented Cars : " + rented);

        if (expensive != null) {

            System.out.println("Most Expensive Car : "
                    + expensive.getBrand() + " "
                    + expensive.getModel());

        }

        if (carCount > 0) {

            System.out.println("Average Daily Price : "
                    + (totalPrice / carCount));

        }

    }
}