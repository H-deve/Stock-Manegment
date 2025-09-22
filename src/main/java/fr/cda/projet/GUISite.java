package fr.cda.projet;

import fr.cda.dao.CommandDAO;
import fr.cda.dao.ProduitDAO;
import fr.cda.fichierCsv.CsvFichier;
import fr.cda.fichierCsv.SauvgardeCsvFile;
import fr.cda.ihm.*;
import fr.cda.models.Commande;
import fr.cda.models.Produit;
import fr.cda.util.Terminal;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

// Classe de definition de l'IHM principale du compte
//
public class GUISite implements FormulaireInt {
    private  static final Logger log = Logger.getLogger(GUISite.class.getName());
    private Site site;  // Le site

    // Constructeur
    //
    public GUISite(Site site) {
        this.site = site;

        // Creation du formulaire
        Formulaire form = new Formulaire("Site de vente", this, 1100, 730);

        //  Creation des elements de l'IHM
        //
        form.addLabel("Afficher tous les produits du stock");
        form.addButton("AFF_STOCK", "Tous le stock");
        form.addLabel("");
        form.addLabel("Afficher tous les bons de commande");
        form.addButton("AFF_COMMANDES", "Toutes les commandes");
        form.addLabel("");
        form.addText("NUM_COMMANDE", "Numero de commande", true, "1");
        form.addButton("AFF_COMMANDE", "Afficher");
        form.addLabel("");
        //button Modifier
        form.addButton("AFF_MODIFIER", "Modifier");
        form.addLabel("");
        //button LIVRÉ
        form.addButton("AFF_LIVRÉ", "Livré");
        //button Calcule Vent
        form.addButton("AFF_CALCULER_VENTES", "Calculer Ventes ");
        form.addButton("SAUVGARD", "Sauvgard");


        form.setPosition(400, 0);
        form.addZoneText("RESULTATS", "Resultats",
                true,
                "",
                600, 700);

        // Affichage du formulaire
        form.afficher();
    }



    // Methode appellee quand on clique dans un bouton
    //
    public void submit(Formulaire form, String nomSubmit) throws SQLException {

        // Affichage de tous les produits du stock
        //
        if (nomSubmit.equals("AFF_STOCK")) {


            String res = site.listerTousProduits();
            form.setValeurChamp("RESULTATS", res);
            log.info("user hh");

            // autre façon pour affichage
            /*
            ProduitDAO produitDAO = new ProduitDAO();
            List<Produit> ListOFProduit = produitDAO.findAll();

            StringBuffer res = new StringBuffer();

            for (Produit produit : ListOFProduit) {
                res.append("== Produits Stocké ==\n");

                res.append("Produit referance: ").append(produit.getReference()).append("\n")
                        .append("nom: ").append(produit.getNom()).append("\n")
                        .append("prix: ").append(produit.getPrix())
                        .append("\n").append("quantite: ").append(produit.getQuantite())
                        .append("\n").append("----------------------------------\n");

            }
            form.setValeurChamp("RESULTATS", res.toString());

             */
        }

        // Affichage de toutes les commandes
        //
        if (nomSubmit.equals("AFF_COMMANDES")) {

//            String res = site.listerToutesCommandes();
//            form.setValeurChamp("RESULTATS", res);

            CommandDAO commandDAO = new CommandDAO();

            List<Commande> commands = commandDAO.findAll();

            StringBuffer res = new StringBuffer();
            for (Commande commande : commands) {
                res.append("Commande ID: ").append(commande.getNumero()).append("\n")
                        .append("Date Creation: ").append(commande.getDate()).append("\n")
                        .append("Client nom: ").append(commande.getClient()).append("\n")
                        .append("Status: ").append(commande.getStatus()).append("\n")
                        .append("refProduit :").append("\n");


                for(String ref :commande.getReferences()){
                    for (String quantites :commande.getQuantities())
                        res.append(ref).append(" = ").append(quantites).append("\n");
                }


                        res.append("-------------------------\n");
            }
            form.setValeurChamp("RESULTATS", res.toString());



        }

        // Affichage d'une commande
        //
        if (nomSubmit.equals("AFF_COMMANDE")) {
            String numStr = form.getValeurChamp("NUM_COMMANDE");
            int num = Integer.parseInt(numStr);
            /*
            CommandDAO commandDAO = new CommandDAO();

            List<Commande> commands = commandDAO.findById(num);

            StringBuffer res = new StringBuffer();
            for (Commande commande : commands) {


                    res.append("Commande ID: ").append(commande.getNumero()).append("\n")
                            .append("Date Creation: ").append(commande.getDate()).append("\n")
                            .append("Client nom: ").append(commande.getClient()).append("\n")
                            .append("Status: ").append(commande.getStatus()).append("\n")
                            .append("refProduit :").append(commande.getReferences()).append("\n");
//
                            for(String ref :commande.getReferences()){

                                res.append(ref).append("\n");
                            }

                            res.append("-------------------------\n");


            }

             */
            String res = site.listerCommande(num);
            form.setValeurChamp("RESULTATS", res.toString());
        }

        if (nomSubmit.equals("AFF_LIVRÉ")) {

            String res = site.listerCommandeNonLivré();
            form.setValeurChamp("RESULTATS", res.toString());
        }

        if (nomSubmit.equals("AFF_MODIFIER")) {
            // Retrieve the command ID and product reference from the form
            int commandeId = Integer.parseInt(form.getValeurChamp("NUM_COMMANDE")); // Assuming you have a text field for command ID
            String refProduit = form.getValeurChamp("REF_PRODUIT"); // Assuming you have a text field for product reference

            // Pass the values to GUIModifierCommande constructor
            new GUIModifierCommande(new Site(), commandeId, refProduit);
        }

        if (nomSubmit.equals("AFF_CALCULER_VENTES")) {


            String res = site.calculCommandeLivré();

            form.setValeurChamp("RESULTATS", res);

        }

        if (nomSubmit.equals("SAUVGARD")) {

            Terminal.ecrireStringln("Début de la sauvegarde des fichiers...");
            try {


                ProduitDAO produitDAO = new ProduitDAO();
                CommandDAO commandDAO = new CommandDAO();
                SauvgardeCsvFile sauvegarde = new SauvgardeCsvFile("ProduitStock.csv", produitDAO ,commandDAO);

                sauvegarde.writeFile();

                form.setValeurChamp("RESULTATS", "Les données ont été sauvegardées avec succès.");
                Terminal.ecrireStringln("Sauvegarde terminée avec succès.");


                form.setValeurChamp("RESULTATS", "Les données ont été sauvegardées avec succès.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}