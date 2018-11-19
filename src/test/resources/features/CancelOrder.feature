Feature: Cancel order

  Background:
    Given A store "store"
    Given The "store" opens "Monday" at "8:00" and closes at "23:30"

  Scenario: An employee cancel an order
    Given A customer "Jack"
    Given "Jack" made an "order2" into the "store" in 4 hours, on "Monday"
    And An employee see the "store"'s orders
    When An employee of "store" cancel the "order2"
    Then "order2" is "Canceled"
    And "Jack" receveid a "jack@jack.com"
