Feature: Store

  Background:
    Given A store "Albertville" with a tax 1 and margin on recipe 10
    And "Marion" the manager of "Albertville"

  Scenario: A store is created an add to cookie firm
    Given A store "Nice" with a tax 1 and margin on recipe 2
    And "Bob" the manager of "Nice"
    Then The cookieFirm as one store name "Nice" with a manager named "Bob"


  Scenario: A manager change is store tax
    When "Marion" change the tax of her store to 3
    Then The store "Albertville" have a tax of 3
