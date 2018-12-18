Feature: Order Withdrawal
  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at 8:00 and closes at 17:30
    Given The kitchen for "store" is infinite

  Scenario: A customer withdraws a paid order
    Given A customer "Jacko"
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The customer add 6 cookies "White Dog" from the "store"
    Given The customer place his order and pay "online"
    Then The "store" purchase the order with 16:30, "Monday", "Jacko@Jacko.fr" and "WITHDRAWN" it
    When the employee delivers the current order
    Then The order in the "store" with 16:30, "Monday" made by "Jacko@Jacko.fr" is "WITHDRAWN"

  Scenario: A customer wants to withdraw an unpaid order and pays
    Given A customer "Boby"
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The customer add 6 cookies "White Dog" from the "store"
    Given The customer place his order and pay "not online"
    Then The "store" purchase the order with 16:30, "Monday", "Boby@Boby.fr" and "WITHDRAWN" it
    And the employee delivers the current order
    Then The order in the "store" with 16:30, "Monday" made by "Boby@Boby.fr" is "WITHDRAWN"
