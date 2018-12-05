Feature: Loyalty Program

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store of "Francis" opens "Monday" 5 hours ago and closes in 4 hours
    Given The store of "Francis" opens "Friday" 5 hours ago and closes in 6 hours

  Scenario: An customer have discount at his second order
    Given A customer "Frank"
    Given "Frank@Frank.fr" is in the loyaltyProgram
    Given The customer choose a store "store" to pickup "Monday" in 2 hours
    Given The customer add 40 cookies from the "store"
    Given The customer place his order and pay "online"
    And The customer choose a store "store" to pickup "Friday" in 2 hours
    And The customer add 10 cookies from the "store"
    And The customer place his order and pay "online"
    When "Frank@Frank.fr" see the price of his order with 10 cookies it have a discount