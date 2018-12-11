Feature: Remove Cookie Order

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given A guest
    Given The customer add 4 cookies "White Dog" from the "store"

  Scenario: Retirer 1 élément dans le panier
    When The customer remove 1 cookie "White Dog" from the "store"
    Then The order contains 3 cookie recipee
