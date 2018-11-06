Scenario : Un client passe une commande anonymement
Given : Un client a sélectionné différents cookies pour 15€
And : A sélectionné une "plage de retrait" dans un magasin
And : Le client valide son type de règlement : "au magasin"
And : Le client a entré son "adresse mail" avant de valider sa commande
And : Le client a imprimer son bon de commande
When : Le client présente son bon de commande à la boutique
Then : Le bon de commande est "scanné"
And : Le client "paye les cookies"
And : La commande est "validée"
