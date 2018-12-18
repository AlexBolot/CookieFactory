Feature: Order a Monthly cookie

  Background:
    Given A store "Nice" with a tax 1 and margin on recipe 1
    And "Bob" the manager of "Nice"
    And "Bob" add the recipe named "BreakFastInAmerica" have "Play", flavor "Chili", topping "White Chocolate" and "no topping" and "no topping", mix "Mixed", cooking "Chewy"
    And Now is "Monday" 8:15

  Scenario: A client Orders a monthly cookie
    Given A customer "Alice"
    When The customer add 6 cookies "BreakFastInAmerica" from the "Nice"
    Then The order contain 1 orderLines
    And The order contains 6 cookie "BreakFastInAmerica"
