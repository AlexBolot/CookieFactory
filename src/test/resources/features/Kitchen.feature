Feature: Manage the stock of a Kitchen

  Background:
    Given A store "emptyStore" with a tax 1 and margin on recipe 1
    And "Manager" the manager of "emptyStore"

  Scenario: Refill the kitchen for One Ingredient
    Given The kitchen of "emptyStore" is empty
    When The manager refill the stock of "topping" "White Chocolate" by 5 in the kitchen of "emptyStore"
    Then The kitchen of "emptyStore" has exactly or more than 5 "topping" "White Chocolate"