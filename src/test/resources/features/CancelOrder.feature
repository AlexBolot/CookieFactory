Feature: Cancel order
  Scenario : An employee cancel an order
    Given An employee of a "store"
    Given A "costumer" made an "order" into the "store"
    And An employee see the list of "orders"
    When An employee "cancel" an "order"
    Then The order is no longer in the list of orders
    And The "costumer" receveid a "email"
