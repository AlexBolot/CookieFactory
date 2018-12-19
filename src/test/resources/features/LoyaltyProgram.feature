Feature: Loyalty Program

  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at 8:00 and closes at 17:30
    Given The store managed by "Francis" opens "Friday" at 8:00 and closes at 18:30

  Scenario: An customer have discount at his second order
    Given A customer "Frank"
    Given The customer set banking data with "Frank", "Franko" and "B375848H4AAG"
    Given "Frank@Frank.fr" is in the loyaltyProgram
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The customer add 40 cookies "White Dog" from the "store"
    Given The customer place his order and pay "online"
    And The customer choose a store "store" to pickup "Friday" at 17:30
    And The customer add 10 cookies "White Dog" from the "store"
    And The customer place his order and pay "online"
    When "Frank@Frank.fr" see the price of his order with 10 cookies it have a discount