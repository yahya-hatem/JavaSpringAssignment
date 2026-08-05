import java.util.Scanner;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String message) {

        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static double readDouble(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid amount. Please try again.");
            }
        }
    }

    public static int readPositiveInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value > 0)
                return value;

            System.out.println("Value must be greater than zero.");
        }
    }

    public static double readPositiveDouble(String message) {

        while (true) {

            double value = readDouble(message);

            if (value > 0)
                return value;

            System.out.println("Value must be greater than zero.");
        }
    }

    public static void closeScanner() {
        scanner.close();
    }
}