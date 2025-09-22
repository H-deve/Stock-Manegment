package fr.cda.controller;

import fr.cda.projet.Site;
import fr.cda.vus.GUISiteVue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

/**
 * La classe GUISiteController est responsable de la gestion des actions de l'interface utilisateur
 * liées aux produits et aux commandes. Elle interagit avec le modèle Site pour afficher
 * des informations sur les produits et les commandes.
 */
public class GUISiteController {
    private static final Logger log = LogManager.getLogger(GUISiteController.class);
    private Site site;


    /**
     * Constructeur
     *
     * @param site L'instance de Site à associer avec ce contrôleur
     *
     */
    public GUISiteController(Site site) {
        this.site = site;

    }



    /**
     * Affiche tous les produits disponibles dans le stock.
     *
     * @return Une chaîne contenant la liste de tous les produits, ou un message d'erreur en cas d'échec
     */
    public String afficherTousProduits() {
        try {
            String res = site.listerTousProduits();
            log.info("L'utilisateur a affiché tous les produits du stock.");
            return res;
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage des produits : " + e.getMessage());
            return "Erreur lors de l'affichage des produits.";
        }
    }

    /**
     * Affiche toutes les commandes enregistrées.
     *
     * @return Une chaîne contenant la liste de toutes les commandes, ou un message d'erreur en cas d'échec
     */
    public String afficherToutesCommandes() {
        try {
            String res = site.listerToutesCommandes();
            log.info("L'utilisateur a affiché toutes les commandes.");
            return res;
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage des commandes : " + e.getMessage());
            return "Erreur lors de l'affichage des commandes.";
        }
    }

    /**
     * Affiche les détails d'une commande spécifiée par son numéro.
     *
     * @param numero Le numéro de la commande à afficher
     * @return Une chaîne contenant les détails de la commande, ou un message d'erreur en cas d'échec
     */
    public String afficherCommande(int numero) {
        try {
            String res = site.listerCommande(numero);
            log.info("L'utilisateur a affiché la commande avec le numéro : " + numero);
            return res;
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage de la commande : " + e.getMessage());
            return "Erreur lors de l'affichage de la commande.";
        }
    }

    /**
     * Affiche toutes les commandes qui n'ont pas encore été livrées.
     *
     * @return Une chaîne contenant la liste des commandes non livrées, ou un message d'erreur en cas d'échec
     */
    public String afficherCommandesNonLivrees() {
        try {
            String res = site.listerCommandeNonLivré();
            log.info("L'utilisateur a affiché les commandes non livrées.");
            return res;
        } catch (Exception e) {
            log.error("Erreur lors de l'affichage des commandes non livrées : " + e.getMessage());
            return "Erreur lors de l'affichage des commandes non livrées.";
        }
    }


    /**
     *
     *  Ecrire dans fichier ProduitStock.CSV en utilisant la methode sauvgardCommandetProduitLivré dnas me class site
     *
     * @return Une chaîne contenant les produits dans les stock et les commande livré
     */
    public String SauvgardeFichier(){

        try{
            String res = site.sauvgardCommandetProduitLivré();
            log.info("l'utilsateur a sauvgarder les commandes livré et Produit en stock ");
            return res ;


        }catch(Exception e){
            log.error("Erreur lors du sauvgarde : " + e.getMessage());
            return "Erreur lors du sauvgarde des Produit et Commandes.";
        }
    }

    /**
     * Calcule et affiche le total des ventes des commandes livrées.
     *
     * @return Une chaîne contenant le total des ventes, ou un message d'erreur en cas d'échec
     */
    public String calculerVentes() {
        try {
            String res = site.calculCommandeLivré();
            log.info("L'utilisateur a calculé les ventes.");
            return res;
        } catch (SQLException e) {
            log.error("Erreur lors du calcul des ventes : " + e.getMessage());
            return "Erreur lors du calcul des ventes.";
        }
    }
}
