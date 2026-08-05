public class SavingsAccount extends Account {

    private double annualInterestRate;
    private int monthlyWithdrawalCount;

    public SavingsAccount(Customer owner,
                          double balance,
                          double annualInterestRate) {

        super(owner, balance);
        this.annualInterestRate = annualInterestRate;
        this.monthlyWithdrawalCount = 0;
    }

    @Override
    public boolean withdraw(double amount) {

        if (status != AccountStatus.ACTIVE)
            return false;

        if (amount <= 0)
            return false;

        if (balance - amount < 0)
            return false;

        balance -= amount;
        transactionCount++;
        monthlyWithdrawalCount++;

        return true;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawalCount;
    }

    @Override
    public AccountType getType() {
        return AccountType.SAVINGS;
    }

    @Override
    public void displayDetails() {

        System.out.println("---------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Type           : Savings");
        System.out.println("Owner          : " + owner.getFullName());
        System.out.println("Balance        : $" + balance);
        System.out.println("Status         : " + status);
        System.out.println("Transactions   : " + transactionCount);
        System.out.println("Interest Rate  : " + annualInterestRate + "%");
        System.out.println("Withdrawals    : " + monthlyWithdrawalCount);
    }
}