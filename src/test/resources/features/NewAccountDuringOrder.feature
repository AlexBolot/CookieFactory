Feature: Account creation while ordering

  Scenario: Account creation while ordering
    Given The customer wants to place his order
    And The customer gives his "email"
    When The customer "ask for creation" of an account
    Then An account is created with the supplied "email"
    And The password is saved
    And The order is placed
