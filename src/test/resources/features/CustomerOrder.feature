Feature: Customer order

  Background:
    Given A store "store"
    Given The "store" opens "Monday" at "8:00" and closes at "23:30"

  Scenario: A customer places an order
    Given A customer "Bob"
    Given An order "425" at the store "store", to pickup "Monday" at "16:25"
    When "Bob" validates the order "425"
    Then "Bob" has 1 order in his history
    And "Bob" has an empty temporary order
