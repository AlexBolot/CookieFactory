Feature: Manage the stock of a Kitchen

  Background:
    Given A store "emptyStore"
    And "Manager" the manager of "emptyStore"

  Scenario: Refill the kitchen for One Ingredient
    Given The kitchen of "emptyStore" is empty
    When The manager refill the stock of "topping" "White Chocolate" by 5 in the kitchen of "emptyStore"
    Then The kitchen of "emptyStore" has exactly 5 "topping" "White Chocolate"