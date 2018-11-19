Feature: Anonymous order
  Background:
    Given A store "store"
    Given The kitchen for "store" is infinite
    Given The store "store" opens "Monday" 5 hours ago and closes in 4 hours

  Scenario: A customer put an order anonymously
    Given A guest "Joelle" have selected 4 cookies in the "store"
    And "Joelle" choose to pickup her "order1" in 3 hours in the "store" on "Monday" and want to pay "in the store"
    And "Joelle" entered her "jojo@gmail.com" to put her "order1"
    Then The purchase "order1" is scan in the "store"
    And "Joelle" pay her cookies
    And The order is "Withdrawn"
