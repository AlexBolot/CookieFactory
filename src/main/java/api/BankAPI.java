package api;

public class BankAPI {

    public void pay(BankingData bankingData, double amount) {

        // Checks validity of parameters
        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is : " + amount);
        checkValidity(bankingData);

        // Then does nothing until we connect real API
    }

    public void refund(BankingData bankingData, double amount) {
        // Checks validity of parameters
        if (amount <= 0) throw new IllegalArgumentException("Amount must be strictly positive. Given is : " + amount);
        checkValidity(bankingData);

        // Then does nothing until we connect real API
    }

    private void checkValidity(BankingData bankingData) {

        if (bankingData.getFirstName().isEmpty())
            throw new IllegalArgumentException("First name is empty");

        if (bankingData.getLastName().isEmpty())
            throw new IllegalArgumentException("Last name is empty");

        if (bankingData.getAccountIdentifier().isEmpty())
            throw new IllegalArgumentException("Account identifier is empty");
    }
}
