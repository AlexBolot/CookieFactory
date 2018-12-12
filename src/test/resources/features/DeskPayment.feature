Feature: Desk Payment

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store of "Francis" opens "Monday" 5 hours ago and closes in 4 hours

  Scenario: Withdrawing an order not paid online
    Given A customer "Joelle"
    Given The customer add 4 cookies "White Dog" from the "store"
    Given The customer choose a store "store" to pickup "Monday" in 2 hours
    And The customer entered her "jojo@gmail.com" to place her order and pay "not online"
    Then The "store" purchase the order with 2, "Monday", "jojo@gmail.com" and "Withdrawn" it
    And The order in the "store" with 2, "Monday" made by "jojo@gmail.com" is "Withdrawn"
