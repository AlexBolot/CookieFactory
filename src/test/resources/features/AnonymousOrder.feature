Feature: Anonymous order
  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at "8:00" and closes at "17:30"
    Given A guest


  Scenario: A customer put an order anonymously
    Given A customer "Joelle"
    Given The customer add 4 cookies "White Dog" from the "store"
    Given The customer choose a store "store" to pickup "Monday" at 16:35
    And The customer entered her "jojo@gmail.com" to place her order and pay "not online"
    And "Joelle" pay her cookies
    Then The "store" purchase the order with 16:35, "Monday", "jojo@gmail.com" and "Withdrawn" it
    And The order in the "store" with 16:35, "Monday" made by "jojo@gmail.com" is "Withdrawn"
