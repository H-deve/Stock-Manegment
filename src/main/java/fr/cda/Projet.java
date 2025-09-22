// Projet 1 CDA
// 
// NOM,Prenom
// NOM,Prenom
//
package fr.cda;

import java.io.*;
import java.util.*;

import fr.cda.controller.GUISiteController;
import fr.cda.projet.GUIModifierCommande;
import fr.cda.projet.GUISite;
import fr.cda.projet.Site;
import fr.cda.util.*;
import fr.cda.vus.GUISiteVue;

// Classe principale d'execution du projet
//
public class Projet
{
    public static void main(String a_args[])
    {
        Terminal.ecrireStringln("Execution du projet ");

        Site site = new Site();
        GUISiteVue vue = new GUISiteVue(site);
//        GUISiteController controller = new GUISiteController(site, vue);

    }
}
