Feature: Create a Recipe

  Scenario: Ajout d une recette normal
    Given Une recette "montly" creer par "Bob"
    And la pate de "montly" est "chocolat"
    And le gout de "montly" est "vanille"
    And la garniture de "montly" est "mnm"
    And le melange de "montly" est "mixer"
    And la cuisson de "montly" est "cronstillant"
    When "Bob" ajoute "montly" comme recette du mois
    Then la recette du mois du magasin de "Bob" est "montly"

