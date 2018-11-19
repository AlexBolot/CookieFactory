Feature: Cancel order

  Background:
    Given A store "store"
    Given The kitchen for "store" is infinite
    Given The store "store" opens "Monday" 5 hours ago and closes in 4 hours

  Scenario: An employee cancel an order
    Given A customer "Jack"
    Given "Jack" made an "order2" into the "store" in 3 hours, on "Monday"
    And An employee see the "store"'s orders
    When An employee of "store" cancel the "order2"
    Then "order2" is "Canceled"
    And "Jack" receveid a "jack@jack.com"
