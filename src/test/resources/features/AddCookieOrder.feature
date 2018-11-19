Feature: Add cookie to order

  Background:

  Scenario: Add 1 existing cookie in the order
    Given The guest see the list of cookies
    When The guest order 1 cookie "White Dog"
    Then The order contains 1 cookie "White Dog"

  Scenario: Add custom cookies in the order
    Given The guest see the list of ingredients
    And  The guest choose the dough "Plain"
    And The guest choose the flavor "Chili"
    And The guest choose the topping "White Chocolate"
    And The guest choose the mix "Topped"
    And The guest choose the cooking "Crunchy"
    When The guest order 4 custom cookie "Custom"
    Then The order contains 4 cookie "Custom"
