package fr.cda.projet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import fr.cda.dao.CommandDAO;
import fr.cda.dao.ProduitDAO;
import fr.cda.fichierCsv.SauvgardeCsvFile;
import fr.cda.models.Commande;
import fr.cda.models.Produit;
import fr.cda.util.Terminal;

// Classe de definition du site de vente
//
public class Site {
    private ArrayList<Produit> stock;       // Les produits du stock
    private ArrayList<Commande> commandes;  // Les bons de commande

    // Constructeur
    //
    public Site() {
        stock = new ArrayList<Produit>();

        // acces à la bdd
        //  pour chaque ligne on cree un Produit que l'on ajoute a stock


        // acces à la bdd
        //  pour chaque ligne on cree une commande ou on ajoute une reference
        //  d'un produit a une commande existante.
        // AC AC 

    }

    // Methode qui retourne sous la forme d'une chaine de caractere
    //  tous les produits du stock
    //
    public String listerTousProduits() {
        StringBuilder res = new StringBuilder();
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> stockList = null;
        try {
            stockList = produitDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Produit prod : stockList) {
            res.append(prod.toString()).append("\n");
        }
        return res.toString();
    }

    // Methode qui retourne sous la forme d'une chaine de caractere
    //  toutes les commandes
    //
    public String listerToutesCommandes() {
        CommandDAO commandDAO;
        try {
            commandDAO = new CommandDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }

        List<Commande> commands = commandDAO.findAll();

        StringBuilder res = new StringBuilder();
        for (Commande commande : commands) {
            res.append("Commande ID: ").append(commande.getNumero()).append("\n")
                    .append("Date Creation: ").append(commande.getDate()).append("\n")
                    .append("Client nom: ").append(commande.getClient()).append("\n")
                    .append("Status: ").append(commande.getStatus()).append("\n")
                    .append("Produits :\n");

            // Assuming references and quantities lists are of the same size
            List<String> references = commande.getReferences();
            List<String> quantities = commande.getQuantities();

            for (int i = 0; i < references.size(); i++) {
                res.append(" - ").append(references.get(i)).append(" : ")
                        .append(quantities.get(i)).append("\n");
            }

            res.append("-------------------------\n");
        }

        // Return the formatted result
        return res.toString();
    }

    // Methode qui retourne sous la forme d'une chaine de caractere
    //  une commande
    //
    public String listerCommande(int numero) {
        StringBuilder res = new StringBuilder();
        CommandDAO commandeDAO = null;
        try {
            commandeDAO = new CommandDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Commande> commandes = commandeDAO.findById(numero);

        for (Commande cmd : commandes) {
            res.append("Commande ID: ").append(cmd.getNumero()).append("\n")
                    .append("Date Creation: ").append(cmd.getDate()).append("\n")
                    .append("Client nom: ").append(cmd.getClient()).append("\n")
                    .append("Status: ").append(cmd.getStatus()).append("\n")
                    .append("Produits :\n");

            // Append each reference and quantity pair in the list
            for (String ref : cmd.getReferences()) {
                res.append(ref).append("\n");
            }

            res.append("-------------------------\n");
        }
        return res.toString();
    }


    /**
     * Liste toutes les commandes non livrées avec leurs détails.
     *
     * @return Une représentation sous forme de chaîne des commandes non livrées, y compris le numéro de la commande, la date de création, le nom du client, le statut, les références des produits, et les raisons de non-livraison.
     */
    public String listerCommandeNonLivré() {

        StringBuilder res = new StringBuilder();
        CommandDAO commandeDAO = null;

        try {

            commandeDAO = new CommandDAO();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Commande> commandes = commandeDAO.getCommandsByStatus("Non Livré");

        for (Commande cmd : commandes) {
            res.append("Commande ID: ").append(cmd.getNumero()).append("\n")
                    .append("Date Creation: ").append(cmd.getDate()).append("\n")
                    .append("Client nom: ").append(cmd.getClient()).append("\n")
                    .append("Status: ").append(cmd.getStatus()).append("\n").append("refProduit :")
                    .append(cmd.getReferences()).append("\n");


            for (String ref : cmd.getReferences()) {

                res.append(ref).append("\n");
            }
            res.append(cmd.getRaisonsNonLivraison()).append("\n");
            res.append("-------------------------\n");

        }
        return res.toString();
    }

    /**
     * Met à jour la quantité d'un produit dans une commande spécifiée.
     *
     * @param commandeId L'identifiant de la commande à mettre à jour.
     * @param refProduit La référence du produit à mettre à jour.
     * @param nouvelleQuantite La nouvelle quantité pour le produit.
     * @throws SQLException Si l'opération de mise à jour échoue.
     */

    public void modifierCommandeQuantite(int commandeId, String refProduit, int nouvelleQuantite) throws SQLException {

        try {

            CommandDAO commandeDAO = new CommandDAO();
            boolean updated = commandeDAO.updateQuantityForProduit(commandeId, refProduit, nouvelleQuantite);

            if (!updated) {
                throw new SQLException("Impossible de mettre à jour la quantité pour le produit : " + refProduit);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sauvegarde les commandes livrées et les produits en stock dans un fichier CSV.
     *
     * @return Un message indiquant si la sauvegarde a réussi ou si une erreur s'est produite.
     */

    public String sauvgardCommandetProduitLivré() {
        Terminal.ecrireStringln("Début de la sauvegarde des fichiers...");
        try {
            ProduitDAO produitDAO = new ProduitDAO();
            CommandDAO commandDAO = new CommandDAO();
            SauvgardeCsvFile sauvegarde = new SauvgardeCsvFile("ProduitStock.csv", produitDAO);
//            SauvgardeCsvFile saungarde2 = new SauvgardeCsvFile("Commandes.csv", commandDAO);

            sauvegarde.writeFile();

            Terminal.ecrireStringln("Sauvegarde terminée avec succès.");
            return "Les données ont été sauvegardées avec succès.";
        } catch (IOException | SQLException e) {
            return "Erreur : la sauvegarde a échoué.";
        }
    }


    /**
     * Calcule le total des ventes pour toutes les commandes livrées et retourne les détails.
     *
     * @return Une chaîne détaillant le total de chaque commande livrée ainsi que le total général pour toutes les commandes.
     * @throws SQLException Si une erreur d'accès à la base de données se produit.
     */
    public String calculCommandeLivré() throws SQLException {
        CommandDAO commandeDAO = new CommandDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        List<Commande> commandes = commandeDAO.getCommandsByStatus("Livré");

        StringBuilder result = new StringBuilder();
        double totalGeneral = 0;

        for (Commande commande : commandes) {
            result.append("Commande ").append(commande.getNumero()).append(":\n");
            double totalCommande = 0;

            List<Produit> produits = produitDAO.findByid(commande.getNumero());

            for (Produit produit : produits) {
                double prixTotalProduit = produit.getPrix() * produit.getQuantite();
                totalCommande += prixTotalProduit;

                result.append(" - ").append(produit.getNom()).append("-----").append("\n")
                        .append("quantite : ").append(produit.getQuantite()).append("\n").append("Prix : ")
                        .append(produit.getPrix()).append("\n");
            }

            result.append("Total commande: ").append(totalCommande).append(" €\n\n");
            totalGeneral += totalCommande;
        }

        result.append("Total général: ").append(totalGeneral).append(" €");
        return result.toString();
    }





    // Chargement des données du stock
    //
    private void initialiserStock(String nomFichier) {

    }


}
