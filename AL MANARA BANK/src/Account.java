public abstract class Account {

    private static int nextAccountNumber = 10001;

    protected String accountNumber;
    protected Customer owner;
    protected double balance;
    protected AccountStatus status;
    protected int transactionCount;

    public Account(Customer owner, double balance) {

        this.accountNumber = "ACC" + nextAccountNumber++;
        this.owner = owner;
        this.balance = balance;
        this.status = AccountStatus.ACTIVE;
        this.transactionCount = 0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public boolean deposit(double amount) {

        if (status != AccountStatus.ACTIVE)
            return false;

        if (amount <= 0)
            return false;

        balance += amount;
        transactionCount++;

        return true;
    }

    public abstract boolean withdraw(double amount);

    public abstract AccountType getType();

    public abstract void displayDetails();
}