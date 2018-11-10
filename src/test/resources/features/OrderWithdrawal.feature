Feature: Order Withdrawal

  Background:
    Given A customer "Bob"
    Given An order "order"
    Given A store
    Given "order" is passed in the store by "Bob"

  Scenario: A customer withdraws a paid order
    Given the customer "Bob" has paid for "order"
    And the employee scans "order"
    When the employee delivers the current order
    Then The order "order" state is "withdrawn"

  Scenario: A customer wants to with draw an unpaid order without paying
    Given the customer "Bob" has the order "order"
    When the employee scans "order"
    Then the order "order" state is "ordered"

  Scenario: A customer wants to with draw an unpaid order and pays
    Given the customer "Bob" has the order "order"
    When the employee scans "order"
    And the empoyee recieves "Bob" payment
    Then the order "order" state is "withdrawn"