Feature: RecipeePrice

  Background:
    Given A store "Nice" with a tax 1 and margin on recipe 2
    And "Bob" the manager of "Nice"
    And The kitchen of "Nice" is empty

  Scenario: A Manager check a simple price
    Given "Bob" change the margin of the "dough" "Peanut butter" to 1
    And "Bob" change the supplier price of the "dough" "Peanut butter" to 10
    And "Bob" change the margin of the "flavor" "Chili" to 2
    And "Bob" change the supplier price of the "flavor" "Chili" to 10
    When "Bob" checks the price of "Pathway to hell"
    Then The price of "Pathway to hell" for "Bob" is 20.3

  Scenario: A manager check the montly recipee
    Given "Bob" add the recipe named "CustomMay" have "Plain", flavor "Oyster", topping "White Chocolate" and "Black Chocolate" and "no topping", mix "Mixed", cooking "Chewy"
    And "Bob" change the margin of the "flavor" "Oyster" to 30
    And "Bob" change the supplier price of the "flavor" "Oyster" to 10
    And "Bob" change the margin of the "topping" "White Chocolate" to 23
    And "Bob" change the supplier price of the "topping" "White Chocolate" to 10
    And "Bob" change the margin of the "topping" "Black Chocolate" to 20
    And "Bob" change the supplier price of the "topping" "Black Chocolate" to 10
    And "Bob" change the margin of the "dough" "Plain" to 7
    And "Bob" change the supplier price of the "dough" "Plain" to 10
    When "Bob" checks the price of "CustomMay"
    Then The price of "CustomMay" for "Bob" is 48.0

  Scenario: A manager check a recipee with a new ingredient
    Given A new flavor "GreenSun" is added to the catalog
    And "Bob" add the recipe named "Gataca" have "Plain", flavor "GreenSun", topping "White Chocolate" and "no topping" and "no topping", mix "Mixed", cooking "Chewy"
    And "Bob" change the margin of the "flavor" "GreenSun" to 84
    And "Bob" change the supplier price of the "flavor" "GreenSun" to 10
    And "Bob" change the margin of the "topping" "White Chocolate" to 1
    And "Bob" change the supplier price of the "topping" "White Chocolate" to 10
    And "Bob" change the margin of the "dough" "Plain" to 1
    And "Bob" change the supplier price of the "dough" "Plain" to 10
    When "Bob" checks the price of "Gataca"
    Then The price of "Gataca" for "Bob" is 38.6