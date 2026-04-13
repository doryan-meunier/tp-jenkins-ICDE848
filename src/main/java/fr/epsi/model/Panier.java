package fr.epsi.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Panier d'achats de la boutique en ligne.
 * ICDE848 – TP Jenkins
 */
public class Panier {

    private Map<Article, Integer> articles = new HashMap<>();

    /**
     * Ajoute un article avec une quantité.
     * Si l'article existe déjà, cumule les quantités.
     */
    public void ajouter(Article article, int quantite) {
        articles.merge(article, quantite, Integer::sum);
    }

    public Map<Article, Integer> getArticles() {
        return articles;
    }

    public boolean estVide() {
        return articles.isEmpty();
    }
}
