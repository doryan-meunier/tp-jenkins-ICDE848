package fr.epsi.service;

import fr.epsi.model.Article;
import fr.epsi.model.Panier;
import java.util.Map;

/**
 * Service métier de gestion des commandes.
 * ICDE848 – TP Jenkins
 */
public class CommandeService {

    /**
     * Calcule le total d'un panier.
     *
     * @param panier le panier à calculer
     * @return le montant total en euros
     * @throws IllegalArgumentException si le panier est null ou vide
     */
    public double calculerTotal(Panier panier) {
        if (panier == null || panier.estVide()) {
            throw new IllegalArgumentException("Panier vide ou null");
        }
        double total = 0;
        for (Map.Entry<Article, Integer> entry : panier.getArticles().entrySet()) {
            total += entry.getKey().getPrix() * entry.getValue();
        }
        return total;
    }

    /**
     * Applique une remise en pourcentage sur un total.
     *
     * @param total      le montant brut
     * @param pourcentage la remise entre 0 et 100
     * @return le montant après remise
     * @throws IllegalArgumentException si le pourcentage est invalide
     */
    public double appliquerRemise(double total, int pourcentage) {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Remise invalide : " + pourcentage);
        }
        return total * (1 - pourcentage / 100.0);
    }

    /**
     * Catégorise une commande selon son montant.
     *
     * @param total le montant de la commande
     * @return "PETITE" < 50€, "MOYENNE" < 200€, "GRANDE" sinon
     */
    public String categoriserCommande(double total) {
        if (total < 50)       return "PETITE";
        else if (total < 200) return "MOYENNE";
        else                  return "GRANDE";
    }
}
