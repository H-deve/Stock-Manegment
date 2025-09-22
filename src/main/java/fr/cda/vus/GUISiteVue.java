package fr.cda.vus;
import fr.cda.controller.GUISiteController;
import fr.cda.projet.GUIModifierCommande;
import fr.cda.controller.GUISiteController;
import fr.cda.ihm.Formulaire;
import fr.cda.ihm.FormulaireInt;
import fr.cda.projet.Site;

import java.sql.SQLException;

public class GUISiteVue implements FormulaireInt {

    private GUISiteController controller;
    private Formulaire form;
    private Site site;
//    private GUISiteVue vue;

    public GUISiteVue(Site site) {
        this.site = site;
        this.controller = new GUISiteController(site);

        form = new Formulaire("Site de vente", this, 1100, 730);
        form.addLabel("Afficher tous les produits du stock");
        form.addButton("AFF_STOCK", "Tous le stock");
        form.addLabel("");
        form.addLabel("Afficher tous les bons de commande");
        form.addButton("AFF_COMMANDES", "Toutes les commandes");
        form.addLabel("");
        form.addText("NUM_COMMANDE", "Numero de commande", true, "1");
//        form.addText("REF_PRODUIT", "Référence du produit", true, "");
        form.addButton("AFF_COMMANDE", "Afficher");
        form.addLabel("");
        // Button Modifier
        form.addButton("AFF_MODIFIER", "Modifier");
        form.addLabel("");
        form.addButton("AFF_LIVRÉ", "Livré");
        form.addButton("AFF_CALCULER_VENTES", "Calculer Ventes");
        form.addLabel("");
        form.addButton("SAUVGARD", "Sauvegarder les données");

        form.setPosition(400, 0);
        form.addZoneText("RESULTATS", "Résultats", true, "", 600, 700);

        form.afficher();
    }

    @Override
    public void submit(Formulaire form, String nomSubmit) throws SQLException {
        String result;
        switch (nomSubmit) {
            case "AFF_STOCK":

                result = controller.afficherTousProduits();
                form.setValeurChamp("RESULTATS", result);
                break;

            case "AFF_COMMANDES":
                result = controller.afficherToutesCommandes();
                form.setValeurChamp("RESULTATS", result);
                break;

            case "AFF_COMMANDE":
                int numero = Integer.parseInt(form.getValeurChamp("NUM_COMMANDE"));
                result = controller.afficherCommande(numero);
                form.setValeurChamp("RESULTATS", result);
                break;

            case "AFF_MODIFIER":

                try {

                    int commandeId = Integer.parseInt(form.getValeurChamp("NUM_COMMANDE"));
                    String refProduit = form.getValeurChamp("REF_PRODUIT");


                    new GUIModifierCommande(site, commandeId, refProduit);

                } catch (NumberFormatException e) {
                    form.setValeurChamp("RESULTATS", "Veuillez entrer un numéro de commande valide.");
                } catch (SQLException e) {
                    form.setValeurChamp("RESULTATS", "Erreur lors de l'ouverture de l'interface de modification: " + e.getMessage());
                }
                break;

            case "AFF_LIVRÉ":
                result = controller.afficherCommandesNonLivrees();
                form.setValeurChamp("RESULTATS", result);
                break;

            case "AFF_CALCULER_VENTES":
                result = controller.calculerVentes();
                form.setValeurChamp("RESULTATS", result);
                break;

            case "SAUVGARD":
                form.setValeurChamp("RESULTATS", "Sauvegarde en cours...");
                result = controller.SauvgardeFichier();
                form.setValeurChamp("RESULTATS", result);
                break;

            default:
                form.setValeurChamp("RESULTATS", "Action inconnue.");
                break;
        }
    }
}

