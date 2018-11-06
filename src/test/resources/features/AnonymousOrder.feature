Feature: Anonymous order
  Scenario: A customer put an order anonymously
  Given A "Guest" have had cookies for a total of "10€"
    And A "Guest" selected a "timeSpan" in a store
    And A "Guest" want to "pay to the store"
    And A "Guest" entered his "email"
    And A "Guest" printed is purachse order
    When A "Guest" show his purchase order in the store
    Then The purchase order is "scan"
    And A "Guest" "pay" is cookies
    And The order is "validated"s
