Feature: Change the unfaithPass ratio of a store

  Scenario: Change ratio
    Given A store "UnFaithStore" with an UnFaithPass ratio of 1
    And "Bob" the manager of "UnFaithStore"
    When "Bob" changes the UnFaithPass ratio to 2
    Then Converting 2 reward-value points in "UnFaithStore" gives 4 units of cash
