Feature: Cancel order
  Background:
    Given A "store"

  Scenario : An employee cancel an order
    Given A "costumer" made an "order" into the "store"
    And An employee see the "store"'s orders
    When An employee cancel an "order" in the "store" with : "pickupTime", "pickupDay", "email"
    Then The order is no longer in the list of orders of the "store"
    And The "costumer" receveid a "email"
