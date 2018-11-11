Feature: Change store schedule

  Background:
    Given A store "store"
    Given "Bob" the manager of "store"

  Scenario: Change store opening time (valid)
    Given The "store" opens "Monday" at "8:00" and closes at "17:30"
    When "Bob" changes opening time of "Monday" to "7:30"
    Then The "store" opening of "Monday" is "7:30"

  Scenario: Change store opening time (invalid)
    Given The "store" opens "Monday" at "8:00" and closes at "17:30"
    When "Bob" changes opening time of "Monday" to "18:30"
    Then Changing opening time of "Monday" fails

  Scenario: Change store closing time (valid)
    Given The "store" opens "Monday" at "8:00" and closes at "17:30"
    When "Bob" changes closing time of "Monday" to "18:30"
    Then The "store" closing of "Monday" is "18:30"

  Scenario: Change store closing time (invalid)
    Given The "store" opens "Monday" at "8:00" and closes at "17:30"
    When "Bob" changes closing time of "Monday" to "7:30"
    Then Changing closing time of "Monday" fails
