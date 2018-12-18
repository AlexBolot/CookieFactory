Feature: Change the unfaithPass ratio of a store

  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at 8:00 and closes at 17:30
    Given The store "store" applies an UnFaithPassProgram
    Given The kitchen for "store" is infinite

  Scenario: A customer presents his UnFaithPass when he withdraw his order to earn rewards
    Given The store "store" applies an UnFaithPass which gives 5 point for the recipe "White Dog"
    Given The store "store" applies an UnFaithPass which gives 2 free Cookies for the recipe "Simple Cookie"
    And A customer "Eustache"
    And The customer "Eustache" has an empty UnFaithPass
    And The customer choose a store "store" to pickup "Monday" at 16:30
    And The customer add 1 cookies "White Dog" from the "store"
    And The customer add 2 cookies "Simple Cookie" from the "store"
    And The customer place his order and pay "online"
    When The "store" purchase the order with 16:30, "Monday", "Eustache@Eustache.fr" and "WITHDRAWN" it
    Then the employee delivers the current order
    And The customer "Eustache" has 5 point and 4 free Cookies on his UnFaithPass
