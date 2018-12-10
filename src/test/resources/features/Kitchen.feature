Feature: Manage the stock of a Kitchen

  Background:
    Given A store "emptyStore" with a tax 1 and margin on recipe 1
    And "Manager" the manager of "emptyStore"

  Scenario: Refill the kitchen for One Ingredient
    Given The kitchen of "emptyStore" is empty
    When The manager refill the stock of "topping" "White Chocolate" by 5 in the kitchen of "emptyStore"
    Then The kitchen of "emptyStore" has exactly 5 "topping" "White Chocolate"

  Scenario: Change the margin of an ingredient
    Given The kitchen of "emptyStore" has a margin of 1 for the "flavor" "Chili"
    When "Manager" change the margin of the "flavor" "Chili" to 2
    Then The margin of "emptyStore" for the "flavor" "Chili" has been changed to 2