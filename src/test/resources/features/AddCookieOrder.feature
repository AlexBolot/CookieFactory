Feature: Add cookie to order

  Scenario : Add 1 cookie in the order
    Given The guest see the list of cookies
    When The guest select the recipee "NyILoveYou"
    And add 1 cookie of the selected recipee
    Then The order contain "1" cookie
