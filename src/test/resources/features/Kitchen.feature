Feature: Manage the stock of a Kitchen

  Background:
    Given A store "emptyStore" with a tax 1 and margin on recipe 1
    And "Manager" the manager of "emptyStore"

  Scenario: Refill the kitchen for One Ingredient
    Given The kitchen of "emptyStore" is empty
    When The manager refill the stock of "topping" "White Chocolate" by 5 in the kitchen of "emptyStore"
    Then The kitchen of "emptyStore" has exactly or more than 5 "topping" "White Chocolate"

  Scenario: Change the margin of an ingredient
    Given The kitchen manage by "Manager" has a margin of 1 for the "flavor" "Chili"
    And The kitchen of "emptyStore" is empty
    When "Manager" change the margin of the "flavor" "Chili" to 2
    Then The margin of "emptyStore" for the "flavor" "Chili" has been changed to 2

  Scenario: Change the margin of an ingredient at 0
    Given The kitchen manage by "Manager" has a margin of 5 for the "flavor" "Chili"
    And The kitchen of "emptyStore" is empty
    When "Manager" change the margin of the "flavor" "Chili" to 0
    Then The margin of "emptyStore" for the "flavor" "Chili" has been changed to 0