Feature: Order Withdrawal

  Background:
    Given A store
    Given A customer "Bob"
    Given An order "order"
    Given "order" is passed in the store by "Bob"

  Scenario: A customer withdraws a paid order
    Given the customer "Bob" has paid for "order"
    And the employee scans "order"
    When the employee delivers the current order
    Then The current order state is"Withdrawn"

  Scenario: A customer wants to with draw an unpaid order without paying
    Given the customer "Bob" has the order "order"
    And the employee scans "order"
    When the employee delivers the current order
    Then The current order state is not "Withdrawn"

  Scenario: A customer wants to with draw an unpaid order and pays
    Given the customer "Bob" has the order "order"
    And the employee scans "order"
    When the employee recieves "Bob" payment
    And the employee delivers the current order
    Then The current order state is"Withdrawn"