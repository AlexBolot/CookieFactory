Feature: Cancel order

  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at 8:00 and closes at 17:30
    Given A customer "Jack"

  Scenario: An employee cancel an order
    Given The customer choose a store "store" to pickup "Monday" at 16:35
    Given The customer add 6 cookies "White Dog" from the "store"
    And An employee see the "store"'s orders
    When The customer place his order and pay "online"
    Then The "store" purchase the order with 16:35, "Monday", "Jack@Jack.fr" and "Canceled" it
    And The order in the "store" with 16:35, "Monday" made by "Jack@Jack.fr" is "Canceled"
    And "Jack" receveid an email at "jack@jack.com"
