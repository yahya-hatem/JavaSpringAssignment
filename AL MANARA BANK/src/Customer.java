public class Customer {

    private static int nextCustomerId = 1001;

    private int customerId;
    private String fullName;
    private String nationalId;
    private String phoneNumber;
    private CustomerTier tier;
    private int openAccounts;

    public Customer(String fullName,
                    String nationalId,
                    String phoneNumber,
                    CustomerTier tier) {

        this.customerId = nextCustomerId++;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.tier = tier;
        this.openAccounts = 0;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerTier getTier() {
        return tier;
    }

    public int getOpenAccounts() {
        return openAccounts;
    }

    public void increaseOpenAccounts() {
        openAccounts++;
    }

    public void decreaseOpenAccounts() {
        if (openAccounts > 0)
            openAccounts--;
    }

    @Override
    public String toString() {

        return "Customer ID : " + customerId +
                "\nName : " + fullName +
                "\nNational ID : " + nationalId +
                "\nPhone : " + phoneNumber +
                "\nTier : " + tier +
                "\nOpen Accounts : " + openAccounts;
    }
}