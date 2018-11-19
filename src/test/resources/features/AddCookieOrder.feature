Feature: Add cookie to order

  Background:
    Given A store "emptyStore"
    And An order "emptyOrder"

  Scenario: Add 1 existing cookie in the order
    Given The guest see the list of cookies
    When The guest select the recipee "White Dog"
    And add 1 cookie of the selected recipee
    Then The order contain 1 orderLines

  Scenario: Add no cookie if cannot do the recipe
    And The kitchen of "emptyStore" is empty
    And An order "emptyOrder" at the store "emptyStore"
    And The guest is ordering the "emptyOrder"
    When The guest select the recipee "White Dog"
    And add 1 cookie of the selected recipee
    Then The order contain 0 orderLines
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

  Scenario: Add too much cookie
    And The kitchen of "emptyStore" can do 2 "White Dog"
    And An order "emptyOrder" at the store "emptyStore"
    And The guest is ordering the "emptyOrder"
    When The guest select the recipee "White Dog"
    And add 3 cookie of the selected recipee
    Then The order contain 1 orderLines
    And The order contain 2 cookie "White Dog"