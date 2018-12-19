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
    And The customer set banking data with "Eustache", "Eustacheo" and "B375848H4AAG"
    And The customer "Eustache" has an empty UnFaithPass
    And The customer choose a store "store" to pickup "Monday" at 16:30
    And The customer add 1 cookies "White Dog" from the "store"
    And The customer add 2 cookies "Simple Cookie" from the "store"
    And The customer place his order and pay "online"
    When The "store" purchase the order with 16:30, "Monday", "Eustache@Eustache.fr" and "WITHDRAWN" it
    And The customer "Eustache" has 5 points and 4 free Cookies on his UnFaithPass

  Scenario: A customer pay with point of his UnFaithPass at the withdrawal of his order
    Given A customer "Andrée"
    And The customer "Andrée" has an UnFaithPass with 2 points and 0 free Cookies
    And The customer choose a store "store" to pickup "Monday" at 16:30
    And The customer add 2 cookies "Simple Cookie" from the "store"
    And The customer place his order and pay "not online"
    When The "store" purchase the order with 16:30, "Monday", "Andrée@Andrée.fr" and "WITHDRAWN" it paying it with 2 UnFaithPass's points and claiming 0 free Cookies
    And The customer "Andrée" has 0 points and 0 free Cookies on his UnFaithPass

  Scenario: A customer withdraw his order and claim free cookies
    Given A customer "Robert"
    Given The customer set banking data with "Robert", "Roberto" and "B375848H4AAG"
    And The customer "Robert" has an UnFaithPass with 1 points and 2 free Cookies
    And The customer choose a store "store" to pickup "Monday" at 16:30
    And The customer add 2 cookies "Simple Cookie" from the "store"
    And The customer place his order and pay "online"
    When The "store" purchase the order with 16:30, "Monday", "Robert@Robert.fr" and "WITHDRAWN" it paying it with 0 UnFaithPass's points and claiming 2 free Cookies
    And The customer "Robert" has 1 points and 0 free Cookies on his UnFaithPass

