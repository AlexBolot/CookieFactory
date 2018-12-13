Feature: Add an ingredient to the firm catalog

  Scenario: Add a custom topping to the catalog
    When A new topping "ATopOfTheWorld" is added to the catalog
    Then The ingredient catalog contains "ATopOfTheWorld"

  Scenario: Add a custom flavor to the catalog
    When A new flavor "flavorite" is added to the catalog
    Then The ingredient catalog contains "flavorite"

  Scenario: Add a custom dough to the catalog
    When A new dough "Dough You Get Me" is added to the catalog
    Then The ingredient catalog contains "Dough You Get Me"

  Scenario: Create a recipe from a new ingredient
    Given A store "store1" with a tax 1 and margin on recipe 1
    And "bob" the manager of "store1"
    And A new topping "ATopOfTheWorld" is added to the catalog
    And A new dough "Doughtifull" is added to the catalog
    And A new flavor "Flavorite" is added to the catalog
    When "bob" add the recipe named "TopCook" have "Doughtifull", flavor "Flavorite", topping "ATopOfTheWorld" and "no topping" and "no topping", mix "Mixed", cooking "Chewy"
    Then The store "store1" montly recipee contains ingredient "ATopOfTheWorld"
    And  The store "store1" montly recipee contains ingredient "Doughtifull"
    And The store "store1" montly recipee contains ingredient "Flavorite"
