Feature: Customer order

  Background:
    Given A store "store"
    Given The "store" opens "Monday" 5 hours ago and closes in 4 hours
    Given A customer "Bob"


    @Ignore pb de prix
  Scenario: A customer places an order
    Given A customer "Bob" choose a store "store" to pickup "Monday" in 2 hours
    Given A customer "Bob" add 6 cookies from the "store"
    When "Bob" place his order and pay "online"
    Then "Bob" has 1 order in his history
    And "Bob" has an empty temporary order
