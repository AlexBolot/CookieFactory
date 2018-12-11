Feature: Create a Recipe

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given "Bob" the manager of "store"

  Scenario: Adding a monthy recipe
    Given "Bob" add the recipe named "New" have "Chocolate", flavor "Vanilla", topping "M&M's" and "no topping" and "no topping", mix "Mixed", cooking "Crunchy"
    Then The monthly recipe of the "store" is "New"