public class Config {

    // Maximum storage
    public static final int MAX_CUSTOMERS = 100;
    public static final int MAX_ACCOUNTS = 300;

    // Minimum opening balances
    public static final double MIN_SAVINGS_BALANCE = 500;
    public static final double MIN_CURRENT_BALANCE = 1000;
    public static final double MIN_FIXED_BALANCE = 5000;

    // Minimum transaction amount
    public static final double MIN_TRANSACTION = 50;

    // Default account values
    public static final double SAVINGS_INTEREST = 5.0;
    public static final double FIXED_INTEREST = 8.0;
    public static final double CURRENT_OVERDRAFT = 1000;

    // Customer tiers
    public static final double STANDARD_MONTHLY_FEE = 20;
    public static final double SILVER_MONTHLY_FEE = 10;
    public static final double GOLD_MONTHLY_FEE = 0;

    public static final double STANDARD_BONUS = 0;
    public static final double SILVER_BONUS = 1;
    public static final double GOLD_BONUS = 2;

    private Config() {
    }
}