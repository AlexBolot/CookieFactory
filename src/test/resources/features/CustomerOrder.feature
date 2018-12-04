Feature: Customer order

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The "store" opens "Monday" 5 hours ago and closes in 4 hours
    Given A customer "Bob"

  Scenario: A customer places an order
    Given The customer choose a store "store" to pickup "Monday" in 2 hours
    Given The customer add 6 cookies from the "store"
    When The customer place his order and pay "online"
    Then The customer with the email "Bob" has 1 order in his history
    And The customer with the email "Bob" has an empty temporary order
