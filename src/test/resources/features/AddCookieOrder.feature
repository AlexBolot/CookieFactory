Feature: Add cookie to order

  Scenario : Add 1 cookie in the order
    Given The "Guest" see the list of cookies
    When The "Guest" select "1" cookie
    Then The order contain "1" cookie