package fr.cda.dao;

import fr.cda.models.Produit;

import javax.print.attribute.standard.Media;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {
    private Connection conn;

    /**
     * Constructeur
     * Commance Connexion avec base de donnée
     */
    public ProduitDAO() {

        try {

            this.conn = FactoryDAO.getInstance().getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String ajouterProduit(Produit produit) {

        String sql = "INSERT INTO Produit (refProduit ,nom , prix , qauntite ) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, produit.getReference());
            ps.setString(2, produit.getNom());
            ps.setDouble(3, produit.getPrix());
            ps.setInt(4, produit.getQuantite());

            ps.executeUpdate();

            int rowEffected = ps.getUpdateCount();

            if (rowEffected > 0) {
                return "Le produit a été ajouté avec succès";

            } else

                return "Erreur : le produit n'a pas pu être ajouté";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur lors de l'ajout du produit : " + e.getMessage();
        }

    }

    /**
     * Method pour trouver toutes les produits
     *
     * @return list de produits Stocké dans table Produit
     * @throws SQLException
     */
    public List<Produit> findAll() throws SQLException {

        List<Produit> produits = new ArrayList<Produit>();
        String sql = "SELECT * FROM Produit";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Produit produit = new Produit(

                        rs.getString("refProduit"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantiteStock"));
                produits.add(produit);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return produits;
    }

    /**
     * Methode pour trouver Produit par ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public List<Produit> findByid(int id) throws SQLException {

        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM Produit  "
                + "INNER JOIN Commande_Produit  ON Produit.id = Commande_Produit.idProduit "
                + "WHERE Commande_Produit.idCommand = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Create a new Produit object
                Produit produit = new Produit(

                        rs.getString("refProduit"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite")
                );
                produits.add(produit);
            }
        }
        return produits;
    }

    /**
     * cette Methode pour chercher la quantité de Produit Srocké
     *
     * @param refProduit
     * @return
     * @throws SQLException
     */
    public int getQuantiteStock(String refProduit) throws SQLException {

        String sql = "SELECT quantiteStock FROM Produit  where refProduit = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, refProduit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantiteStock");

            } else
                return -1; // si le produit n"existe pas
        }


    }



}




