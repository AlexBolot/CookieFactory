package api;

public class BankAPI {

    /**
     * Given a banking data account remove money on this account to pay
     * @param bankingData account on to remove the money
     * @param amount to withdraw
     */
    public void pay(BankingData bankingData, double amount) {

        // Checks validity of parameters
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be strictly positive. Given is : " + amount);
        bankingData.checkValidity();

        // Then does nothing until we connect real API
    }

    /**
     * Given a banking data account put money on this account to refund
     * @param bankingData account where to put the money
     * @param amount to refund
     */
    public void refund(BankingData bankingData, double amount){
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be strictly positive. Given is : " + amount);
        bankingData.checkValidity();

        // Then does nothing until we connect real API
    }

}
