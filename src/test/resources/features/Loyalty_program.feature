Feature: Loyalty Program

  Background:
    Given A store "store"
    Given The kitchen for "store" is infinite
    Given The store "store" opens "Monday" 5 hours ago and closes in 4 hours
    Given The store "store" opens "Friday" 5 hours ago and closes in 6 hours

@Ignore pb de prix
  Scenario: An customer have discount at his second order
    Given A customer "Frank"
    Given "Frank" is in the loyaltyProgram
    Given "Frank" made an "order1" into the "store" in 3 hours, on "Monday", with 40 cookies
    And "Frank" made an "order2" into the "store" in 4 hours, on "Friday", with 10 cookies
    When "Frank" see the price of his "order2" with 10 cookies it have a discount