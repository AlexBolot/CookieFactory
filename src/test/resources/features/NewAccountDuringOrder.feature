Feature: Account creation while ordering

  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store of "Francis" opens "Monday" 5 hours ago and closes in 4 hours
    Given A guest

  Scenario: Account creation while ordering
    Given The customer add 4 cookies from the "store"
    Given The customer choose a store "store" to pickup "Monday" in 2 hours
    And The customer entered her "jojo@jojo.com" to place her order and pay "not online"
    When The guest create an account at the name of "Jo" "Doe" with the password "azerty" and the phone "06.06.06.06.06" from "jojo@jojo.com"
    Then The account with "jojo@jojo.com" is saved
    And The order in the "store" with 2, "Monday" made by "jojo@jojo.com" is saved