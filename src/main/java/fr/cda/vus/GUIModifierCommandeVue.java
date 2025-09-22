package fr.cda.vus;

import fr.cda.controller.GUIModifierController;
import fr.cda.ihm.Formulaire;
import fr.cda.ihm.FormulaireInt;
import fr.cda.projet.Site;


/**
 * Class Vus Pou afficher la form GUIModifierCommande
 */
public class GUIModifierCommandeVue implements FormulaireInt {

    private GUIModifierController controller;
    private Formulaire form;

    /**
     * Constructeur
     * @param site
     * @param commandeId
     * @param refProduit
     */
    public GUIModifierCommandeVue(Site site, int commandeId, String refProduit) {
        this.controller = new GUIModifierController(site);

        if (controller.isCommandeDelivered(commandeId)) {
            System.out.println("Attention ! La commande est déjà livrée, modification non autorisée.");
            return;
        }

        form = new Formulaire("Modifier Commande", this, 800, 730);
        int currentQuantity = controller.fetchCurrentQuantity(commandeId, refProduit);
        form.addText("NUM_COMMANDE", "Numero de commande", true, String.valueOf(commandeId));
        form.addText("REF_PRODUIT", "Reference Produit", true, refProduit);
        form.addText("QUANTITE", "Quantite actuelle", true, String.valueOf(currentQuantity));
        form.addButton("MODIFIER_COMMAND", "Modifier");
        form.afficher();
    }

    @Override
    public void submit(Formulaire form, String nomSubmit) {
        if ("MODIFIER_COMMAND".equals(nomSubmit)) {
            try {
                int commandeId = Integer.parseInt(form.getValeurChamp("NUM_COMMANDE"));
                String refProduit = form.getValeurChamp("REF_PRODUIT");
                int nouvelleQuantite = Integer.parseInt(form.getValeurChamp("QUANTITE"));

                String result = controller.modifyCommandeQuantity(commandeId, refProduit, nouvelleQuantite);
                form.setValeurChamp("RESULTATS", result);
            } catch (NumberFormatException e) {
                form.setValeurChamp("RESULTATS", "Erreur : La quantité doit être un nombre valide.");
            }
        }
    }
}
