package fr.cda.fichierCsv;

import fr.cda.dao.CommandDAO;
import fr.cda.dao.ProduitDAO;
import fr.cda.models.Commande;
import fr.cda.models.Produit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SauvgardeCsvFile extends CsvFichier{

    private ProduitDAO produitDAO;
    private CommandDAO commandDAO;

    public SauvgardeCsvFile(String fileName , ProduitDAO produitDAO , CommandDAO commandDAO) {
        super(fileName);
        this.produitDAO = produitDAO;
        this.commandDAO = commandDAO;
    }

    public SauvgardeCsvFile(String fileName, ProduitDAO produitDAO) {
        super(fileName);
        this.produitDAO = produitDAO;
    }

    public SauvgardeCsvFile(String fileName, CommandDAO commandDAO) {
        super(fileName);
        this.commandDAO = commandDAO;
    }

    @Override
    public void readFile() throws IOException {
        super.readFile();


    }

    /**
     * Cette Method permet de Ecrire des lines dans Fichier .CSV avec list de commande Livré et produit Stocké
     * @throws IOException
     */
    @Override
    public void writeFile() throws IOException {
        try {

            List<Produit> produits = produitDAO.findAll();
            List<Commande> commandes = commandDAO.getCommandsByStatus("Livré") ;

            List<String> lines = new ArrayList<>();
            lines.add("NOM , Quantite , Prix ");
            for (Produit p : produits) {

                String nom = p.getNom();
                int quantite = p.getQuantite();
                double prix = p.getPrix();
                lines.add(nom + "," + quantite + "," + prix);
            }
            lines.add("\n");

            lines.add("id , dateCreation , nomClient ,status , raisonsNonLivraison");

            for (Commande c : commandes) {
                int id = c.getNumero() ;
                String  dateCreation = c.getDate() ;
                String nomClinet = c.getClient() ;
                String status = c.getStatus() ;
                String raisonsNonLivraison = c.getRaisonsNonLivraison() ;

                lines.add(id+ ","+dateCreation +","+nomClinet +","+status +","+raisonsNonLivraison);
            }

            lineWrite(lines);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
