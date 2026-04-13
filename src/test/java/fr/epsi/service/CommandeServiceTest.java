package fr.epsi.service;

import fr.epsi.model.Article;
import fr.epsi.model.Panier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires du CommandeService.
 *
 * Convention de nommage :
 *   methode_Scenario_ResultatAttendu()
 *
 * Pattern AAA :
 *   GIVEN  → préparer le contexte
 *   WHEN   → exécuter l'action
 *   THEN   → vérifier le résultat
 *
 * ICDE848 – TP Jenkins
 */
class CommandeServiceTest {

    private CommandeService service;
    private Panier panier;

    /** Exécuté avant chaque test — repart d'un état propre */
    @BeforeEach
    void setUp() {
        service = new CommandeService();
        panier  = new Panier();
    }

    // ─────────────────────────────────────────────────
    // calculerTotal
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("Total correct pour 3 stylos à 2€")
    void calculerTotal_TroisStylos_RetourneSix() {
        // GIVEN
        panier.ajouter(new Article("Stylo", 2.0), 3);

        // WHEN
        double total = service.calculerTotal(panier);

        // THEN
        assertEquals(6.0, total, 0.001);
    }

    @Test
    @DisplayName("Total correct pour plusieurs articles différents")
    void calculerTotal_PlusieursArticles_RetourneSomme() {
        // GIVEN
        panier.ajouter(new Article("Stylo",  2.0), 3);  // 6€
        panier.ajouter(new Article("Cahier", 5.0), 2);  // 10€

        // WHEN
        double total = service.calculerTotal(panier);

        // THEN
        assertEquals(16.0, total, 0.001);
    }

    @Test
    @DisplayName("Panier vide lève une IllegalArgumentException")
    void calculerTotal_PanierVide_LeveException() {
        assertThrows(IllegalArgumentException.class,
            () -> service.calculerTotal(new Panier()));
    }

    @Test
    @DisplayName("Panier null lève une IllegalArgumentException")
    void calculerTotal_PanierNull_LeveException() {
        assertThrows(IllegalArgumentException.class,
            () -> service.calculerTotal(null));
    }

    // ─────────────────────────────────────────────────
    // appliquerRemise
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("Remise 10% sur 100€ = 90€")
    void appliquerRemise_DixPourcent_RetourneQuatreVingtDix() {
        double resultat = service.appliquerRemise(100.0, 10);
        assertEquals(90.0, resultat, 0.001);
    }

    @Test
    @DisplayName("Remise 0% ne change pas le total")
    void appliquerRemise_ZeroPourcent_RetourneTotalInchange() {
        double resultat = service.appliquerRemise(100.0, 0);
        assertEquals(100.0, resultat, 0.001);
    }

    @Test
    @DisplayName("Remise négative lève une IllegalArgumentException")
    void appliquerRemise_RemiseNegative_LeveException() {
        assertThrows(IllegalArgumentException.class,
            () -> service.appliquerRemise(100.0, -5));
    }

    @Test
    @DisplayName("Remise > 100 lève une IllegalArgumentException")
    void appliquerRemise_RemiseSupCent_LeveException() {
        assertThrows(IllegalArgumentException.class,
            () -> service.appliquerRemise(100.0, 150));
    }

    // ─────────────────────────────────────────────────
    // categoriserCommande
    // ─────────────────────────────────────────────────

    @Test
    @DisplayName("30€ → catégorie PETITE")
    void categoriser_TrenteEuros_RetournePetite() {
        assertEquals("PETITE", service.categoriserCommande(30.0));
    }

    @Test
    @DisplayName("150€ → catégorie MOYENNE")
    void categoriser_CentCinquanteEuros_RetourneMoyenne() {
        assertEquals("MOYENNE", service.categoriserCommande(150.0));
    }

    @Test
    @DisplayName("500€ → catégorie GRANDE")
    void categoriser_CinqCentsEuros_RetourneGrande() {
        assertEquals("GRANDE", service.categoriserCommande(500.0));
    }
}
