Feature: Change store schedule

  Scenario : Change store schedule
    Given The "manager" is connected for a "store"
    And The "manager" consult the parameters
    When The "manager" select a new "day" and a new "timespan"
    Then The store's schedule is changed
