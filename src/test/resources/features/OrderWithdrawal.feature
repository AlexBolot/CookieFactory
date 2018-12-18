@Ignore
Feature: Order Withdrawal
  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given A customer "Boby"
    Given An order "order"
    Given "order" is passed in the store "store" by "Boby"

  Scenario: A customer withdraws a paid order
    Given the customer "Boby" has paid for "order"
    And the employee of "store" scans "order"
    When the employee delivers the current order
    Then The current order state is "Withdrawn"

  Scenario: A customer wants to withdraw an unpaid order without paying
    Given the customer "Boby2" has the order "order"
    And the employee of "store" scans "order"
    When the employee delivers the current order
    Then The current order state is not "Withdrawn"

  Scenario: A customer wants to withdraw an unpaid order and pays
    Given the customer "Boby3" has the order "order"
    And the employee of "store" scans "order"
    And the employee delivers the current order
    Then The current order state is "Withdrawn"