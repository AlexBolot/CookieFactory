Feature: Guest order custom cookie

  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at "8:00" and closes at "17:30"
    Given A guest

  Scenario: A guest order a custom cookie with one topping
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The guest order 4 cookies of the custom recipe with "Peanut Butter", "Chili", "Reese's buttercup", "no topping", "no topping", "Mixed" and "Chewy"
    And The customer entered her "tada@tada.fr" to place her order and pay "online"

  Scenario: A guest order a custom cookie with 3 topping
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The guest order 4 cookies of the custom recipe with "Peanut Butter", "Chili", "Reese's buttercup", "M&m's", "Milk Chocolate", "Mixed" and "Chewy"
    And The customer entered her "tada@tada.fr" to place her order and pay "online"

  Scenario: A guest order a custom cookie with 0 flavor
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    Given The guest order 4 cookies of the custom recipe with "Peanut Butter", "no flavor", "Reese's buttercup", "M&m's", "Milk Chocolate", "Mixed" and "Chewy"
    And The customer entered her "tada@tada.fr" to place her order and pay "online"

  Scenario: A guest order a custom cookie with wrong dough
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    When The guest try to order 4 cookies of the wrong custom recipe with "Strawberry", "no flavor", "Reese's buttercup", "M&m's", "Milk Chocolate", "Mixed" and "Chewy"
    Then The custom recipe ordering fails