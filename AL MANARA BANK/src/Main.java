public class Main {

    public static void main(String[] args) {

        BankSystem bank = new BankSystem();

        int choice;

        do {

            System.out.println("\n=====================================");
            System.out.println("        AL MANARA BANK SYSTEM");
            System.out.println("=====================================");
            System.out.println("1. Register New Customer");
            System.out.println("2. Open New Account");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Between Accounts");
            System.out.println("6. Display Customer Accounts");
            System.out.println("7. Display All Branch Accounts");
            System.out.println("8. Search Account By Number");
            System.out.println("9. Search Accounts By Type");
            System.out.println("10. Close Account");
            System.out.println("0. Exit");

            choice = InputHelper.readInt("\nEnter your choice: ");

            switch (choice) {

                case 1:
                    bank.registerCustomer();
                    break;

                case 2:
                    bank.openAccount();
                    break;

                case 3:
                    bank.depositMoney();
                    break;

                case 4:
                    bank.withdrawMoney();
                    break;

                case 5:
                    bank.transferMoney();
                    break;

                case 6:
                    bank.displayCustomerAccounts();
                    break;

                case 7:
                    bank.displayAllAccounts();
                    break;

                case 8:
                    bank.searchAccountByNumber();
                    break;

                case 9:
                    bank.searchAccountsByType();
                    break;

                case 10:
                    bank.closeAccount();
                    break;

                case 0:
                    System.out.println("\nThank you for using Al Manara Bank.");
                    break;

                default:
                    System.out.println("Invalid menu choice.");
            }

        } while (choice != 0);

        InputHelper.closeScanner();
    }
}