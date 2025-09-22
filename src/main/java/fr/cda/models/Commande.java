package fr.cda.models;

import java.sql.Date;
import java.util.*;

/**
 * Classe de definition d'une commande
 */
public class Commande
{
    /**
     * Les caracteristiques d'une commande
     */

    private int     numero;         // numero de la commande
    private String  date;           // date de la commande. Au format JJ/MM/AAAA
    private int idClient;

    private String  client;         // nom du client
    private String status ;
    private ArrayList<String> references;// les references des produits de la commande
    private ArrayList<String> quantities;
    private String raisonsNonLivraison ;

    /**
     * Constructeur
     * @param numero
     * @param date
     * @param idClient
     * @param client
     * @param status
     * @param raisonsNonLivraison
     */
    public Commande(int numero, String date, int idClient, String client, String status, String raisonsNonLivraison) {
        this.numero = numero;
        this.date = date;
        this.idClient = idClient;
        this.client = client;
        this.status = status;
        this.references = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.raisonsNonLivraison = raisonsNonLivraison;
    }

    // Getter and Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ArrayList<String> getReferences() {
        return references;
    }

    public void setReferences(ArrayList<String> references) {
        this.references = references;
    }

    public String getRaisonsNonLivraison() {
        return raisonsNonLivraison;
    }

    public void setRaisonsNonLivraison(String raisonsNonLivraison) {
        this.raisonsNonLivraison = raisonsNonLivraison;
    }

    public ArrayList<String> getQuantities() {
        return quantities;
    }

    public void setQuantities(ArrayList<String> quantities) {
        this.quantities = quantities;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Commande{");
        sb.append("numero=").append(numero);
        sb.append(", date='").append(date).append('\'');
        sb.append(", idClient=").append(idClient);
        sb.append(", client='").append(client).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", references=").append(references);
        sb.append(", quantities=").append(quantities);
        sb.append(", raisonsNonLivraison='").append(raisonsNonLivraison).append('\'');
        sb.append('}');
        return sb.toString();
    }


}