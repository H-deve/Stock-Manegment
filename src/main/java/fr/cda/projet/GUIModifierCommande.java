package fr.cda.projet;

import fr.cda.dao.CommandDAO;
import fr.cda.ihm.Formulaire;
import fr.cda.ihm.FormulaireInt;
import fr.cda.models.Commande;

import java.sql.SQLException;
import java.util.List;

public class GUIModifierCommande implements FormulaireInt {

    private Site site;
    private int commandeId;
    private String refProduit; // Reference of the product to modify

    public GUIModifierCommande(Site site, int commandeId, String refProduit) throws SQLException {
        this.site = site;
        this.commandeId = commandeId;
        this.refProduit = refProduit;


        if (isCommandeDeliverd(commandeId)) {
            System.out.println("Attention ! Si le bon de commande est livré, on ne peut pas le modifier.");
            return;  // exit
        }
        Formulaire form = new Formulaire("Modifier Quantite Commande", this, 800, 730);

        int currentQuantity = fetchCurrentQuantity(commandeId, refProduit);

        form.addText("NUM_COMMANDE", "Numero de commande", true, String.valueOf(commandeId)); // Display the command ID
        form.addText("REF_PRODUIT", "Reference Produit", true, refProduit); // Display the product reference
        form.addText("QUANTITE", "Quantite actuelle", true, String.valueOf(currentQuantity)); // Display current quantity

        form.addButton("MODIFIER_COMMAND", "Modifier");
        form.afficher();
    }

    private boolean isCommandeDeliverd(int commandeId) throws SQLException {
        CommandDAO commandDAO = new CommandDAO();
        List<Commande> commandesLivré = commandDAO.getCommandsByStatus("Livré");
        List<Commande> commandesENattente = commandDAO.getCommandsByStatus("En attente");
        List<Commande> commandesAnnulé = commandDAO.getCommandsByStatus("Annulé");


        for (Commande commande : commandesLivré) {
            if (commande.getNumero() == commandeId) {
                return true;
            }
        }

        for (Commande commande : commandesENattente) {
            if (commande.getNumero() == commandeId) {
                return true;
            }
        }

        for (Commande commande : commandesAnnulé){
            if (commande.getNumero() == commandeId) {
                return true;
            }
        }
        return false;
    }


    private int fetchCurrentQuantity(int commandeId, String refProduit) throws SQLException {
        CommandDAO commandeDAO = new CommandDAO();
        return commandeDAO.getQuantityForProduit(commandeId, refProduit);
    }

    @Override
    public void submit(Formulaire form, String nom) throws SQLException {
        if (nom.equals("MODIFIER_COMMAND")) {
            try {
                int nouvelleQuantite = Integer.parseInt(form.getValeurChamp("QUANTITE"));

                String refProduit = form.getValeurChamp("REF_PRODUIT");

                int commandeId = Integer.parseInt(form.getValeurChamp("NUM_COMMANDE"));

                site.modifierCommandeQuantite(commandeId, refProduit, nouvelleQuantite);

                form.setValeurChamp("RESULTATS", "La quantité a été modifiée avec succès.");
            } catch (NumberFormatException e) {
                form.setValeurChamp("RESULTATS", "Erreur: La quantité doit être un nombre valide.");
            } catch (SQLException e) {
                form.setValeurChamp("RESULTATS", "Erreur lors de la modification de la quantité: " + e.getMessage());
            } finally {
                form.fermer();
            }
        }
    }


}
