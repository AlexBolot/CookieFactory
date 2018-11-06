Scenario : Un employé annule la commande d’un client
Given : Un employé du magasin "m"
Given : Un client a réalisé une commande à retirer au magasin "m"
And : L’employé consulte les commandes à réaliser
When : L’employé annule une commande
Then : La commande n'apparaît plus dans la liste des commandes consultables
And : Le client est notifié
