Feature: Anonymous order
  Background:
    Given A store "store"
    Given The "store" opens "Monday" at "8:00" and closes at "23:30"

  Scenario: A customer put an order anonymously
    Given A guest "Joelle" have selected 4 cookies in the "store"
    And "Joelle" selected a "16:30" in "store" on "Monday" and want to pay in the "store"
    And "Joelle" entered her "jojo@gmail.com" to put her "order1"
    Then The purchase "order1" is scan in the "store"
    And "Joelle" pay her cookies
    And The order is "Withdrawn"
