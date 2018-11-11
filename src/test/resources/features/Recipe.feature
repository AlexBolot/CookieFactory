Feature: Create a Recipe

  Background:
    Given "store" a store
    Given "Bob" the Manager of "store"

  Scenario: Adding a monthy recipe
    Given A recipe "monthly" is created by "Bob"
    And The dough of "monthly" is "Chocolate"
    And The flavor of "monthly" is "Vanilla"
    And la topping of "monthly" is "M&M's"
    And The mix of "monthly" is "Mixed"
    And The cooking of "monthly" is "Crunchy"
    When "Bob" add "monthly" as monthly recipe
    Then The monthly recipe of the store of "Bob" is "monthly"

