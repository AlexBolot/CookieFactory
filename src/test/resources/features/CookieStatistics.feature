Feature: Cookie statistics


  Scenario: A manager queries the cookie ratio
    Given A store "Nice" with a tax 1 and margin on recipe 1
    And "Bob" the manager of "Nice"
    And The store "Nice" has the command set "White Dog Favorite"
    When "Bob" compute the cookie ratio
    Then The cookie ratio seen by "Bob" is for the set "White Dog Favorite"

  Scenario: