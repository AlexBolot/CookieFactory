Feature: Add cookie to order

  Background:

  Scenario: Add 1 cookie in the order
    Given The guest see the list of cookies
    When The guest select the recipee "White Dog"
    And add 1 cookie of the selected recipee
    Then The order contain 1 cookie
