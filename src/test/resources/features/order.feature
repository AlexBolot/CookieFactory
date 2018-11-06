Scenario : Un client passe une commande en étant identifié
Given : Un "client" s’est connecté en donnant ses identifiants
And : Le "client" a sélectionnés 3 "cookies du mois"
And : A sélectionné une plage de retrait dans un magasin
And : Le "client" valide son type de règlement : sur internet
And : Le "client" a choisi si la commande s’inscrivait dans son programme de fidélité
When : Le "client" paye sa commande sur internet
Then : Le "client" imprime son bon de commande
And : La commande est ajoutée à son historique