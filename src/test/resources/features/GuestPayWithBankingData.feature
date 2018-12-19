Feature: Banking data payment

  Background:
    Given Now is "Monday" 14:30
    Given A store "store" with a tax 1 and margin on recipe 1
    Given The kitchen for "store" is infinite
    Given "Francis" the manager of "store"
    Given The store managed by "Francis" opens "Monday" at 8:00 and closes at 17:30

  Scenario: Pay online with banking data
    Given A customer "Joelle"
    Given The customer set banking data with "Joelle", "Joelleo" and "A275848H474"
    Given The customer add 4 cookies "White Dog" from the "store"
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    And The customer entered her "jojo@gmail.com" to place her order and pay "online"
    Then The "store" purchase the order with 16:30, "Monday", "jojo@gmail.com" and "Withdrawn" it
    And The order in the "store" with 16:30, "Monday" made by "jojo@gmail.com" is "Withdrawn"

  Scenario: Pay online without banking data
    Given A customer "Joelle"
    Given The customer add 4 cookies "White Dog" from the "store"
    Given The customer choose a store "store" to pickup "Monday" at 16:30
    And The customer entered her "jojo@gmail.com" to place her order and pay "online"
    Then The customer order is not validated

