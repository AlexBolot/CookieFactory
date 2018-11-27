package api;

import java.util.Objects;

public class BankingData {

    private final String firstName;
    private final String lastName;
    private final String accountIdentifier;

    public BankingData(String firstName, String lastName, String accountIdentifier) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountIdentifier = accountIdentifier;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    String getAccountIdentifier() {
        return accountIdentifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, accountIdentifier);
    }
}
