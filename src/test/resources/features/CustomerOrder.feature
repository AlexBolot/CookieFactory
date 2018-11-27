Feature: Customer order

  Background:
    Given A store "store"
    Given The store "store" opens "Monday" 5 hours ago and closes in 4 hours

@Ignore
  Scenario: A customer places an order
    Given A customer "Bob"
    Given An order "425" at the store "store", to pickup "Monday" 1 hour before closing time
    When "Bob" validates the order "425"
    Then "Bob" has 1 order in his history
    And "Bob" has an empty temporary order
