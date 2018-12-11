Feature: Add cookie to order

  Background:
    Given A store "emptyStore" with a tax 1 and margin on recipe 1
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    And A guest

  Scenario: Add 1 existing cookie in the order
    Given The guest see the list of cookies
    When The guest select the recipee "White Dog" of the "store"
    And add 1 cookie of the selected recipee in the "store"
    Then The order contain 1 orderLines

  Scenario: Add too much cookie
    And The kitchen of "emptyStore" is empty
    And The manager refill the stock of "dough" "Peanut Butter" by 2 in the kitchen of "emptyStore"
    And The manager refill the stock of "flavor" "Vanilla" by 2 in the kitchen of "emptyStore"
    And The manager refill the stock of "topping" "White Chocolate" by 6 in the kitchen of "emptyStore"
    When The guest order 3 cookie "White Dog" from the "emptyStore"
    Then The order contain 1 orderLines
    And The order contains 2 cookie "White Dog"

  Scenario: Add no cookie if cannot do the recipe
    And The kitchen of "emptyStore" is empty
    When The guest order 3 cookie "White Dog" from the "emptyStore"
    Then The order contain 0 orderLines



