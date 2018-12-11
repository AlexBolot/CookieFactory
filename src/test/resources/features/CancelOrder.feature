Feature: Cancel order

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store of "Francis" opens "Monday" 5 hours ago and closes in 4 hours
    Given A customer "Jack"

  Scenario: An employee cancel an order
    Given The customer choose a store "store" to pickup "Monday" in 2 hours
    Given The customer add 6 cookies "White Dog" from the "store"
    And An employee see the "store"'s orders
    When The customer place his order and pay "online"
    Then The "store" purchase the order with 2, "Monday", "Jack@Jack.fr" and "Canceled" it
    And The order in the "store" with 2, "Monday" made by "Jack@Jack.fr" is "Canceled"
    And "Jack" receveid a "jack@jack.com"
