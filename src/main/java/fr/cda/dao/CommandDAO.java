package fr.cda.dao;

import fr.cda.models.Commande;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommandDAO {
    private Connection conn;
    private Commande commande;


    /**
     * commance Connextion avec base de donnée
     *
     * @throws SQLException
     */
    public CommandDAO() throws SQLException {

        this.conn = FactoryDAO.getInstance().getConnection();
    }


    /**
     * update la quantite de Produit lies avec Commande demandé
     *
     * @param commandeId
     * @param refProduit
     * @param nouvelleQuantite
     * @return
     * @throws SQLException
     */
    public boolean updateQuantityForProduit(int commandeId, String refProduit, int nouvelleQuantite) throws SQLException {
        String sql = "UPDATE Commande_Produit SET quantite = ? " +
                "WHERE idCommand = ? " +
                "AND idProduit =" +
                " (SELECT id FROM Produit WHERE refProduit = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nouvelleQuantite);
            ps.setInt(2, commandeId);
            ps.setString(3, refProduit);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Return true si au moins une row a été modifié
        }
    }

    /**
     * Get quantité de produit
     *
     * @param commandeId
     * @param refProduit
     * @return
     * @throws SQLException
     */
    public int getQuantityForProduit(int commandeId, String refProduit) throws SQLException {
        String sql = "SELECT quantite FROM Commande_Produit WHERE idCommand = ? AND idProduit " +
                " = (SELECT id FROM Produit WHERE refProduit = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            ps.setString(2, refProduit);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantite");
            } else {
                return 0; // Return 0 if not found
            }
        }
    }

    // Example method to get idProduit by name
    private int getIdProduitByName(String produitName) throws SQLException {
        String sql = "SELECT id FROM Produit WHERE name = ?"; // Update column name as necessary
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, produitName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1; // Or throw an exception if not found
    }


    /**
     * Methode pour avoir toutes les commandes stocké
     *
     * @return list commande
     */
    public List<Commande> findAll() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT Commande.id, Commande.dateCreation, Commande.idClient, Commande.nomClient, Commande.status, Commande.raisonsNonLivraison, "
                + "Produit.refProduit, Commande_Produit.quantite "
                + "FROM Commande "
                + "JOIN Commande_Produit ON Commande.id = Commande_Produit.idCommand "
                + "JOIN Produit ON Commande_Produit.idProduit = Produit.id";

        /**
         * Vous pouvez ici choisir le format de la date à récupérer
         */
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Create a new Commande for each row
                Commande commande = new Commande(
                        rs.getInt("id"),
                        sdf.format(rs.getDate("dateCreation")),
                        rs.getInt("idClient"),
                        rs.getString("nomClient"),
                        rs.getString("status"),
                        rs.getString("raisonsNonLivraison")
                );

                // Ajout  reference et quantity de produits depuis table "Produit" dans commande
                String refProduit = rs.getString("refProduit");
                String quantite = rs.getString("quantite");

                if (refProduit != null) {
                    commande.getReferences().add(refProduit);
                }

                if (quantite != null) {
                    commande.getQuantities().add(quantite);
                }

                commandes.add(commande);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    /**
     * Methode boolean pour verfier si la status de commande est Livré ou NON
     *
     * @param commandeId
     * @return
     * @throws SQLException
     */
    public boolean isCommandeDelivered(int commandeId) throws SQLException {
        String sql = "SELECT status FROM Commande WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commandeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                return "LIVRÉ".equalsIgnoreCase(status);
            }
        }
        return false;
    }


    /**
     * Method pour trouver une commande par ID
     *
     * @param id
     * @return
     */
    public List<Commande> findById(int id) {

        Commande commande = null;
        List<Commande> commandes = new ArrayList<>();

        String sql = "SELECT *, Produit.refProduit, Commande_Produit.quantite "
                + "FROM Commande "
                + "JOIN Commande_Produit ON Commande.id = Commande_Produit.idCommand "
                + "JOIN Produit ON Commande_Produit.idProduit = Produit.id "
                + "WHERE Commande.id = ?";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //si le commande excite déja
                if (commande == null || commande.getNumero() != rs.getInt("id")) {
                    commande = new Commande(
                            rs.getInt("id"),
                            sdf.format(rs.getDate("dateCreation")),
                            rs.getInt("idClient"),
                            rs.getString("nomClient"),
                            rs.getString("status"),
                            rs.getString("raisonsNonLivraison")
                    );
                    commandes.add(commande);
                }

                //trouver quantité et referances
                String refProduit = rs.getString("refProduit");
                int quantite = rs.getInt("quantite");

                // reFormat le reference et  quantity comme  "refProduit = quantite"
                if (refProduit != null) {
                    commande.getReferences().add(refProduit + " = " + quantite); // Add the formatted string
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    /**
     * Methode pou trouver commande par status
     *
     * @param status
     * @return
     */
    public List<Commande> getCommandsByStatus(String status) {

        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT Commande.*, "
                + "GROUP_CONCAT(Produit.refProduit) AS refProduits "
                + "FROM Commande "
                + "INNER JOIN Commande_Produit ON Commande.id = Commande_Produit.idCommand "
                + "INNER JOIN Produit ON Commande_Produit.idProduit = Produit.id "
                + "WHERE Commande.status = ? "
                + "GROUP BY Commande.id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status); // Set the desired status
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Create new Commande
                Commande commande = new Commande(
                        rs.getInt("id"),
                        new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("dateCreation")),
                        rs.getInt("idClient"),
                        rs.getString("nomClient"),
                        rs.getString("status"),
                        rs.getString("raisonsNonLivraison")
                );


                commandes.add(commande);

                String refProduits = rs.getString("refProduits");
                if (refProduits != null && !refProduits.isEmpty()) {
                    String[] refArray = refProduits.split(",");
                    Collections.addAll(commande.getReferences(), refArray);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }
}
