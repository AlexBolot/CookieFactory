Feature: Remove Cookie Order

  Scenario: Retirer 1 élément dans le panier
    Given The "customer" checks his order
    When The "customer" select 1 cookie to remove
    Then the order contains 1 cookies less
