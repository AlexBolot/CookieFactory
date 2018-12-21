Feature: Cookie statistics

  Background:
    Given A store "Nice" with a tax 1 and margin on recipe 1
    And "Bob" the manager of "Nice"

  Scenario: A manager queries the cookie ratio
    Given The store "Nice" has the command set "White Dog Favorite"
    When "Bob" compute the cookie ratio
    Then The cookie ratio seen by "Bob" is for the set "White Dog Favorite"

  Scenario: A manager queries the ingredient ratio
    Given The store "Nice" has the command set "White Dog Favorite"
    When "Bob" compute the unweighted custom ingredient ratio
    Then The unweighted custom ingredient ratio seen by "Bob" is for the set "White Dog Favorite"

  Scenario: A manager queries the weigthed ingredient ratio
    Given The store "Nice" has the command set "White Dog Favorite"
    When "Bob" compute the weighted custom ingredient ratio
    Then The weighted custom ingredient ratio seen by "Bob" is for the set "White Dog Favorite"

  Scenario: A manager queries the pickUpTimeCount
    Given The store "Nice" has the command set "One cookie at multiple times"
    When "Bob" compute the pick up time count
    Then The compute pick up time count seen by "Bob" is for the set "One cookie at multiple times"

  Scenario: A manager queries the global CookieRatioStat
    Given An cookieFirm with no Store
    And A store "Beausoleil" with a tax 1 and margin on recipe 1
    And A store "La Turbie" with a tax 1 and margin on recipe 1
    And The store "Beausoleil" has the command set "White Dog Favorite"
    And The store "La Turbie" has the command set "One cookie at multiple times"
    When The cookie Firm queries the global statistics "CookieRatio"
    Then The computed CookieRatioStat for the Store "Beausoleil" is for the set "White Dog Favorite"
    And The computed CookieRatioStat for the Store "La Turbie" is for the set "One cookie at multiple times"

  Scenario: A manager queries the global CookieRatioStat
    Given An cookieFirm with no Store
    And A store "Beausoleil" with a tax 1 and margin on recipe 1
    And A store "La Turbie" with a tax 1 and margin on recipe 1
    And The store "Beausoleil" has the command set "White Dog Favorite"
    And The store "La Turbie" has the command set "One cookie at multiple times"
    When The cookie Firm queries the global statistics "PickUpTimeCount"
    Then The computed PickUpTimeCountStat for the Store "Beausoleil" is for the set "White Dog Favorite"
    And The computed PickUpTimeCountStat for the Store "La Turbie" is for the set "One cookie at multiple times"

