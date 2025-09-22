//package testDAO;
//
//
//
//import fr.cda.dao.ProduitDAO;
//import fr.cda.models.Produit;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//
//import java.sql.SQLException;
//import java.util.List;
//
//
//
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class testProduit {
//
//    @Test
//    @Order(1)
//    public void testAjouterProduit() throws SQLException {
//        // Arrange
//        ProduitDAO produitDAO = new ProduitDAO();
//        Produit produit = new Produit("LIV-001", "Livre - Roman",15.99 , 100);
//
//        // Act
//        String ajouté = produitDAO.ajouterProduit(produit);
//        int id = 1 ;
//        List<Produit> produitRécupéré = produitDAO.findByid(id);
//
//        // Assert
//        assertTrue(Boolean.parseBoolean("Le produit devrait être ajoute avec succès."), ajouté);
//        assertNotNull(produitRécupéré.toString(), "Le produit devrait être récupére de la base de données");
//        assertEquals("Produit Test", produitRécupéré.get(id), "Le nom du produit devrait correspondre");
//    }
//}
