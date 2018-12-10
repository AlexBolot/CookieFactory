Feature: Add cookie to order

  Background:
    Given A store "emptyStore" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    And A guest

  Scenario: Add 1 existing cookie in the order
    Given The guest see the list of cookies
    When The guest select the recipee "White Dog" of the "store"
    And add 1 cookie of the selected recipee in the "store"
    Then The order contain 1 orderLines

  @Ignore
  Scenario: Add too much cookie
    And The kitchen of "emptyStore" is empty
    And The kitchen of "emptyStore" can do 2 "White Dog"
    And The guest is ordering at the store "emptyStore"
    And The guest is ordering the "emptyOrder"
    When The guest select the recipee "White Dog" of the "store"
    And add 3 cookie of the selected recipee in the "emptyStore"
    Then The order contain 1 orderLines
    And The order contain 2 cookie "White Dog"

  Scenario: Add no cookie if cannot do the recipe
    And The kitchen of "emptyStore" is empty
    And The guest is ordering at the store "emptyStore"
    When The guest select the recipee "White Dog" of the "store"
    And add 1 cookie of the selected recipee in the "store"
    Then The order contain 0 orderLines
    When The guest order 1 cookie "White Dog" from the "store"
    Then The order contains 1 cookie "White Dog"


  @Ignore
  Scenario: Add custom cookies in the order
    Given The guest see the list of ingredients
    And  The guest choose the dough "Plain", flavor "Chili", topping "White Chocolate", mix "Topped" cooking "Crunchy"
    When The guest order 4 custom cookie "Custom"
    Then The order contains 4 cookie "Custom"


