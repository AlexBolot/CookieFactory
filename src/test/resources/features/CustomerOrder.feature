Feature: Customer order

  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at "8:00" and closes at "17:30"

  Scenario: A customer places an order
    Given A customer "Bob"
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The customer add 6 cookies "White Dog" from the "store"
    When The customer place his order and pay "online"
    Then The customer with the email "Bob" has 1 order in his history
    And The customer with the email "Bob" has an empty temporary order

  Scenario: A customer places an order
    Given A customer "Shaurt"
    Given The customer choose a store "store" to pickup "Monday" at 15:30
    Given The customer add 6 cookies "White Dog" from the "store"
    When The customer place his order and pay "online"
    Then It fails because it's a too short delay

  Scenario: A customer places an order
    Given A customer "Earl"
    Given The customer choose a store "store" to pickup "Monday" at 7:00
    Given The customer add 6 cookies "White Dog" from the "store"
    When The customer place his order and pay "online"
    Then It fails because it's before opening

  Scenario: A customer places an order
    Given A customer "Leit"
    Given The customer choose a store "store" to pickup "Monday" at 19:00
    Given The customer add 6 cookies "White Dog" from the "store"
    When The customer place his order and pay "online"
    Then It fails because it's after closing
