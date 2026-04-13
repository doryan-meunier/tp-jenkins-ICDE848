package fr.epsi.model;

/**
 * Représente un article du catalogue de la boutique.
 * ICDE848 – TP Jenkins
 */
public class Article {

    private String nom;
    private double prix;

    public Article(String nom, double prix) {
        this.nom  = nom;
        this.prix = prix;
    }

    public String getNom()  { return nom; }
    public double getPrix() { return prix; }
}
