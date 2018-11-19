Feature: Remove Cookie Order

  Background:
    Given A guest
    Given An order with 2 recipe of 1 cookie

  Scenario: Retirer 1 élément dans le panier
    Given The customer checks his order
    When The customer select 1 cookie to remove
    Then The order contains 1 cookie recipee
