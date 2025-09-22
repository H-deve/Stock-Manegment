package fr.cda.fichierCsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Classe abstraite pour la gestion des fichiers CSV.
 */
public abstract class CsvFichier implements Fichier {

    private String fileName = "ProduitStock.csv";

    public CsvFichier(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void readFile() throws IOException {

    }

    @Override
    public void writeFile() throws IOException {

    }

    /**
     * Écrit une liste de lignes dans le fichier CSV.
     * @param lines La liste des lignes à écrire dans le fichier.
     * @throws IOException Si une erreur se produit lors de l'écriture dans le fichier.
     */
    public void lineWrite(List<String> lines) throws IOException {

        File file = new File(fileName);

        // Vérifie si le fichier existe, sinon, le crée

        if (!file.exists()) {
            file.createNewFile();
            System.out.println("fichier a éte crée ave succée :" + fileName);
        }else {

            // Écrit les lignes dans le fichier
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error writing to the file: " + fileName, e);
        }}

    }
}