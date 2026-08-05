public class FixedDepositAccount extends Account {

    private double interestRate;
    private int durationMonths;
    private int elapsedMonths;

    public FixedDepositAccount(Customer owner,
                               double balance,
                               double interestRate,
                               int durationMonths) {

        super(owner, balance);

        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.elapsedMonths = 0;
    }

    @Override
    public boolean withdraw(double amount) {

        if (status != AccountStatus.ACTIVE)
            return false;

        if (amount <= 0)
            return false;

        if (!isMatured()) {
            System.out.println("Withdrawal rejected.");
            System.out.println("Remaining months: "
                    + (durationMonths - elapsedMonths));
            return false;
        }

        if (balance - amount < 0)
            return false;

        balance -= amount;
        transactionCount++;

        return true;
    }

    public boolean isMatured() {
        return elapsedMonths >= durationMonths;
    }

    public void advanceMonth() {

        if (elapsedMonths < durationMonths)
            elapsedMonths++;
    }

    public int getElapsedMonths() {
        return elapsedMonths;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public AccountType getType() {
        return AccountType.FIXED_DEPOSIT;
    }

    @Override
    public void displayDetails() {

        System.out.println("---------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Type           : Fixed Deposit");
        System.out.println("Owner          : " + owner.getFullName());
        System.out.println("Balance        : $" + balance);
        System.out.println("Status         : " + status);
        System.out.println("Transactions   : " + transactionCount);
        System.out.println("Interest Rate  : " + interestRate + "%");
        System.out.println("Duration       : " + durationMonths + " months");
        System.out.println("Elapsed        : " + elapsedMonths + " months");
        System.out.println("Matured        : " + isMatured());
    }
}