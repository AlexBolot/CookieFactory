Feature: Order placing

  Scenario : A connected customer places an order
    Given A "customer" identified
    And the "customer" selected 3 "monthly cookies"
    And a pickupdate has been selected
    And the "customer" pays online
    And the "customer" chose if the order contributed to his loyalty programme
    Then the "customer" prints his orderticket
    And the order has been added to his history