public class CurrentAccount extends Account {

    private double overdraftLimit;

    public CurrentAccount(Customer owner,
                          double balance,
                          double overdraftLimit) {

        super(owner, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {

        if (status != AccountStatus.ACTIVE)
            return false;

        if (amount <= 0)
            return false;

        if ((balance - amount) < (-overdraftLimit))
            return false;

        balance -= amount;
        transactionCount++;

        return true;
    }

    public boolean isUsingOverdraft() {
        return balance < 0;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public AccountType getType() {
        return AccountType.CURRENT;
    }

    @Override
    public void displayDetails() {

        System.out.println("---------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Type           : Current");
        System.out.println("Owner          : " + owner.getFullName());
        System.out.println("Balance        : $" + balance);
        System.out.println("Status         : " + status);
        System.out.println("Transactions   : " + transactionCount);
        System.out.println("Overdraft      : $" + overdraftLimit);
        System.out.println("Using OD       : " + isUsingOverdraft());
    }
}