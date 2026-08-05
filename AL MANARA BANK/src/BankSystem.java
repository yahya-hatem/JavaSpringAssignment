

public class BankSystem {

    private final Customer[] customers;
    private final Account[] accounts;

    private int customerCount;
    private int accountCount;



    public BankSystem() {

        customers = new Customer[Config.MAX_CUSTOMERS];
        accounts = new Account[Config.MAX_ACCOUNTS];

        customerCount = 0;
        accountCount = 0;


    }

    // ==========================
    // Search Methods
    // ==========================

    public Customer findCustomerById(int id) {

        for (int i = 0; i < customerCount; i++) {

            if (customers[i].getCustomerId() == id)
                return customers[i];
        }

        return null;
    }

    public Customer findCustomerByNationalId(String nationalId) {

        for (int i = 0; i < customerCount; i++) {

            if (customers[i].getNationalId().equals(nationalId))
                return customers[i];
        }

        return null;
    }

    public Account findAccount(String accountNumber) {

        for (int i = 0; i < accountCount; i++) {

            if (accounts[i].getAccountNumber().equalsIgnoreCase(accountNumber))
                return accounts[i];
        }

        return null;
    }

    // ==========================
    // Validation
    // ==========================

    private boolean validPhone(String phone) {

        if (phone.isEmpty())
            return true;

        if (phone.length() < 7 || phone.length() > 15)
            return false;

        for (int i = 0; i < phone.length(); i++) {

            if (!Character.isDigit(phone.charAt(i)))
                return false;
        }

        return true;
    }

    // ==========================
    // Register Customer
    // ==========================

    public void registerCustomer() {

        if (customerCount >= Config.MAX_CUSTOMERS) {

            System.out.println("Customer storage is full.");
            return;
        }


         String name = InputHelper.readString("Full Name: ");

        if (name.isEmpty()) {

            System.out.println("Name cannot be empty.");
            return;
        }
        String nationalId = InputHelper.readString("National ID: ").trim();

        if (findCustomerByNationalId(nationalId) != null) {

            System.out.println("National ID already exists.");
            return;
        }
        String phone = InputHelper.readString("Phone (optional): ").trim();
        if (!validPhone(phone)) {

            System.out.println("Invalid phone number.");
            return;
        }

        System.out.println("Customer Tier");
        System.out.println("1. STANDARD");
        System.out.println("2. SILVER");
        System.out.println("3. GOLD");



        int choice = InputHelper.readInt("Choose Tier: ");
        CustomerTier tier;

        switch (choice) {

            case 1:
                tier = CustomerTier.STANDARD;
                break;

            case 2:
                tier = CustomerTier.SILVER;
                break;

            case 3:
                tier = CustomerTier.GOLD;
                break;

            default:
                System.out.println("Invalid tier.");
                return;
        }

        Customer customer = new Customer(name, nationalId, phone, tier);

        customers[customerCount++] = customer;

        System.out.println();
        System.out.println("Customer registered successfully.");
        System.out.println(customer);
    }
    // ==========================
// Open New Account
// ==========================
    public void openAccount() {

        if (accountCount >= Config.MAX_ACCOUNTS) {
            System.out.println("Account storage is full.");
            return;
        }

        System.out.print("Enter Customer ID: ");

        int customerId;

        try {
            customerId = Integer.parseInt(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid Customer ID.");
            return;
        }

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.println("3. Fixed Deposit Account");
        System.out.print("Choose Account Type: ");

        int choice;

        try {
            choice = Integer.parseInt(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Opening Balance: ");

        double balance;

        try {
            balance = Double.parseDouble(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid balance.");
            return;
        }

        Account account = null;

        switch (choice) {
            case 1:
                if (balance < Config.MIN_SAVINGS_BALANCE) {
                    System.out.println("Minimum opening balance is " + Config.MIN_SAVINGS_BALANCE);
                    return;
                }
                account = new SavingsAccount(customer, balance, Config.SAVINGS_INTEREST);
                break;

            case 2:
                if (balance < Config.MIN_CURRENT_BALANCE) {
                    System.out.println("Minimum opening balance is " + Config.MIN_CURRENT_BALANCE);
                    return;
                }
                account = new CurrentAccount(customer, balance, Config.CURRENT_OVERDRAFT);
                break;

            case 3:
                if (balance < Config.MIN_FIXED_BALANCE) {
                    System.out.println("Minimum opening balance is " + Config.MIN_FIXED_BALANCE);
                    return;
                }

                System.out.print("Duration (Months): ");

                int months;
                try {
                    months = Integer.parseInt(InputHelper.readString(""));
                } catch (Exception e) {
                    System.out.println("Invalid duration.");
                    return;
                }

                account = new FixedDepositAccount(customer, balance, Config.FIXED_INTEREST, months);
                break;

            default:
                System.out.println("Invalid account type.");
                return;
        }

        accounts[accountCount++] = account;
        customer.increaseOpenAccounts();

        System.out.println("Account created successfully.");
        System.out.println("Account Number: " + account.getAccountNumber());
    }

    // ==========================
// Deposit Money
// ==========================
    public void depositMoney() {

        System.out.print("Enter Account Number: ");
        String accountNumber = InputHelper.readString("");

        Account account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Deposit Amount: ");

        double amount;

        try {
            amount = Double.parseDouble(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount < Config.MIN_TRANSACTION) {
            System.out.println("Minimum transaction amount is " + Config.MIN_TRANSACTION);
            return;
        }

        if (account.deposit(amount)) {
            System.out.println("Deposit successful.");
            System.out.println("New Balance = " + account.getBalance());
        } else {
            System.out.println("Deposit failed.");
        }
    }
    // ==========================
// Withdraw Money
// ==========================
    public void withdrawMoney() {

        System.out.print("Enter Account Number: ");
        String accountNumber = InputHelper.readString("");

        Account account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Withdrawal Amount: ");

        double amount;

        try {
            amount = Double.parseDouble(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount < Config.MIN_TRANSACTION) {
            System.out.println("Minimum transaction amount is "
                    + Config.MIN_TRANSACTION);
            return;
        }

        if (account.withdraw(amount)) {

            System.out.println("Withdrawal Successful.");
            System.out.println("Current Balance = "
                    + account.getBalance());

        } else {

            System.out.println("Withdrawal Failed.");
        }
    }
    // ==========================
// Transfer Money
// ==========================
    public void transferMoney() {

        System.out.print("Enter Source Account Number: ");
        String sourceNumber = InputHelper.readString("");

        Account source = findAccount(sourceNumber);

        if (source == null) {
            System.out.println("Source account not found.");
            return;
        }

        System.out.print("Enter Destination Account Number: ");
        String destinationNumber = InputHelper.readString("");

        Account destination = findAccount(destinationNumber);

        if (destination == null) {
            System.out.println("Destination account not found.");
            return;
        }

        if (source == destination) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        System.out.print("Transfer Amount: ");

        double amount;

        try {
            amount = Double.parseDouble(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount < Config.MIN_TRANSACTION) {
            System.out.println("Minimum transaction amount is "
                    + Config.MIN_TRANSACTION);
            return;
        }

        // Withdraw from source
        if (!source.withdraw(amount)) {
            System.out.println("Transfer failed. Unable to withdraw from source account.");
            return;
        }

        // Deposit to destination
        if (!destination.deposit(amount)) {

            // Rollback
            source.deposit(amount);

            System.out.println("Transfer failed. Money has been restored.");
            return;
        }

        System.out.println("Transfer Successful.");
        System.out.println("Source Balance = " + source.getBalance());
        System.out.println("Destination Balance = " + destination.getBalance());
    }
    // ==========================
// Display Customer Accounts
// ==========================
    public void displayCustomerAccounts() {

        System.out.print("Enter Customer ID: ");

        int customerId;

        try {
            customerId = Integer.parseInt(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid Customer ID.");
            return;
        }

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("\n========== CUSTOMER ==========");
        System.out.println(customer);

        boolean found = false;
        double totalBalance = 0;

        System.out.println("\n========== ACCOUNTS ==========");

        for (int i = 0; i < accountCount; i++) {

            if (accounts[i].getOwner().getCustomerId() == customerId) {

                found = true;

                System.out.println("-----------------------------");
                System.out.println("Account Number : " + accounts[i].getAccountNumber());
                System.out.println("Type           : " + accounts[i].getType());
                System.out.println("Balance        : $" + accounts[i].getBalance());
                System.out.println("Status         : " + accounts[i].getStatus());
                System.out.println("Transactions   : " + accounts[i].getTransactionCount());

                totalBalance += accounts[i].getBalance();
            }
        }

        if (!found) {
            System.out.println("This customer has no accounts.");
        }

        System.out.println("-----------------------------");
        System.out.println("Total Balance = $" + totalBalance);
    }
    // ==========================
// Display All Branch Accounts
// ==========================
    public void displayAllAccounts() {

        if (accountCount == 0) {

            System.out.println("No accounts found.");
            return;
        }

        System.out.println("\n========== ALL BRANCH ACCOUNTS ==========");

        for (int i = 0; i < accountCount; i++) {

            System.out.println("-----------------------------");

            System.out.println("Account Number : "
                    + accounts[i].getAccountNumber());

            System.out.println("Owner          : "
                    + accounts[i].getOwner().getFullName());

            System.out.println("Type           : "
                    + accounts[i].getType());

            System.out.println("Balance        : $"
                    + accounts[i].getBalance());

            System.out.println("Status         : "
                    + accounts[i].getStatus());

            System.out.println("Transactions   : "
                    + accounts[i].getTransactionCount());
        }
    }
    // ==========================
// Search Account By Number
// ==========================
    public void searchAccountByNumber() {

        System.out.print("Enter Account Number: ");
        String accountNumber = InputHelper.readString("");

        Account account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        account.displayDetails();
    }

    // ==========================
// Search Accounts By Type
// ==========================
    public void searchAccountsByType() {

        System.out.println("1. Savings");
        System.out.println("2. Current");
        System.out.println("3. Fixed Deposit");
        System.out.print("Choose Type: ");

        int choice;

        try {
            choice = Integer.parseInt(InputHelper.readString(""));
        } catch (Exception e) {
            System.out.println("Invalid choice.");
            return;
        }

        AccountType type;

        switch (choice) {
            case 1:
                type = AccountType.SAVINGS;
                break;
            case 2:
                type = AccountType.CURRENT;
                break;
            case 3:
                type = AccountType.FIXED_DEPOSIT;
                break;
            default:
                System.out.println("Invalid type.");
                return;
        }

        int count = 0;
        double total = 0;

        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getType() == type) {
                accounts[i].displayDetails();
                count++;
                total += accounts[i].getBalance();
            }
        }

        System.out.println("Matching Accounts = " + count);
        System.out.println("Combined Balance = $" + total);
    }

    // ==========================
// Close Account
// ==========================
    public void closeAccount() {

        System.out.print("Enter Account Number: ");
        String accountNumber = InputHelper.readString("");

        Account account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        if (account.getStatus() == AccountStatus.CLOSED) {
            System.out.println("Account is already closed.");
            return;
        }

        if (account.getBalance() != 0) {
            System.out.println("Account balance must be zero before closing.");
            return;
        }

        if (account instanceof FixedDepositAccount fd) {
            if (!fd.isMatured()) {
                System.out.println("Cannot close an unmatured fixed deposit account.");
                return;
            }
        }

        account.setStatus(AccountStatus.CLOSED);
        account.getOwner().decreaseOpenAccounts();

        System.out.println("Account closed successfully.");
    }
}