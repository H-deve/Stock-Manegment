package fr.cda.controller;

import fr.cda.dao.CommandDAO;
import fr.cda.dao.ProduitDAO;
import fr.cda.projet.Site;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;


/**
 * La classe GUIModifierController est responsable de la gestion des modifications des commandes
 * liées aux produits dans l'application. Elle fournit des méthodes pour récupérer les quantités
 * actuelles de produits, modifier les quantités et vérifier le statut de livraison des commandes.
 */
public class GUIModifierController {

    /**
     * constructeur
     *
     * @param site L'instance de Site à associer avec ce contrôleur
     */
    private static final Logger log = LogManager.getLogger(GUIModifierController.class);
    private Site site;

    public GUIModifierController(Site site) {
        this.site = site;
    }

    /**
     * Fetch the current quantity for a given product in a specific command.
     *
     * @param commandeId The command ID
     * @param refProduit The product reference
     * @return The current quantity of the product in the command
     */
    public int fetchCurrentQuantity(int commandeId, String refProduit) {
        try {
            CommandDAO commandDAO = new CommandDAO();
            int quantity = commandDAO.getQuantityForProduit(commandeId, refProduit);
            log.info("Quantité actuelle récupérée pour le produit " + refProduit + " dans la commande " + commandeId + ": " + quantity);
            return quantity;
        } catch (SQLException e) {
            log.error("Erreur lors de la récupération de la quantité pour le produit " + refProduit + ": " + e.getMessage());
            return -1;// Retourne -1 en cas d'erreur
        }
    }

    /**
     * Modify la quantity de produits  dans le  command non livré.
     *
     * @param commandeId       The command ID
     * @param refProduit       The product reference
     * @param nouvelleQuantite The new quantity to be set
     * @return A message indicating the result of the operation
     */
    public String modifyCommandeQuantity(int commandeId, String refProduit, int nouvelleQuantite) {

        try {
            ProduitDAO produitDAO = new ProduitDAO();
            int quantiteStock = produitDAO.getQuantiteStock(refProduit);

            //si le produit n'exist pas
            if (quantiteStock == -1) {

                log.error("Produit non trouvé : " + refProduit);
                return "Erreur : Le produit n'existe pas.";
            }

            //si la quantité demandée depasseé la quantité dans le stock

            if (quantiteStock < nouvelleQuantite) {

                log.info("Qauntité demandée : " + nouvelleQuantite + " supérieure à la quantité en stock  " + quantiteStock);
                return "Erreur : La quantité demandée dépasse la quantité en stock";
            }

            site.modifierCommandeQuantite(commandeId, refProduit, nouvelleQuantite);

            log.info("Quantité modifiée avec succès pour le produit " + refProduit + " dans la commande " + commandeId);
            return "La quantité a été modifiée avec succès.";
        } catch (SQLException e) {
            log.error("Erreur lors de la modification de la quantité pour le produit " + refProduit + ": " + e.getMessage());
            return "Erreur lors de la modification de la quantité.";
        }
    }

    /**
     * Check if a command is delivered.
     *
     * @param commandeId The command ID
     * @return True if the command is delivered, otherwise false
     */
    public boolean isCommandeDelivered(int commandeId) {
        try {
            CommandDAO commandDAO = new CommandDAO();
            boolean delivered = commandDAO.isCommandeDelivered(commandeId);
            log.info("La commande " + commandeId + (delivered ? " est livrée." : " n'est pas livrée."));
            return delivered;
        } catch (SQLException e) {
            log.error("Erreur lors de la vérification du statut de livraison pour la commande " + commandeId + ": " + e.getMessage());
            return false;
        }
    }
}

