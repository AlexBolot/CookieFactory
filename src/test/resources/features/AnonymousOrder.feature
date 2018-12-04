Feature: Anonymous order
  Background:
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given The "store" opens "Monday" 5 hours ago and closes in 4 hours
    Given A guest

  Scenario: A customer put an order anonymously
    Given The customer add 4 cookies from the "store"
    Given The customer choose a store "store" to pickup "Monday" in 2 hours
    And The customer entered her "jojo@gmail.com" to place her order and pay "not online"
    And "Joelle" pay her cookies
    Then The "store" purchase the order with 2, "Monday", "jojo@gmail.com" and "Withdrawn" it
    And The order in the "store" with 2, "Monday" made by "jojo@gmail.com" is "Withdrawn"
