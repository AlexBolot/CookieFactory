Feature: Account creation while ordering

  Background:
    Given A guest "guest"

  Scenario: Account creation while ordering
    Given An order "Order1" with 2 cookies "Dreams On"
    And A guest "guest" is ordering the order "Order1"
    And The guest "guest" gives "email@email" as email
    When The guest "guest" create an account "customer" at the name of "John" "Doe" with the password "azerty" and the phone "06.06.06.06.06"
    Then The account "customer" is saved
    And The order "Order1" is saved in the account "customer"