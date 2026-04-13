# TP Jenkins – Boutique en ligne
## ICDE848 – Intégration Continue : Serveur, Tests & Métriques

---

## 📁 Structure du projet

```
tp-jenkins/
├── pom.xml                          ← Configuration Maven complète
├── checkstyle.xml                   ← Règles de style de code
├── Jenkinsfile                      ← Pipeline CI complète
├── README.md                        ← Ce fichier
└── src/
    ├── main/java/fr/epsi/
    │   ├── model/
    │   │   ├── Article.java         ← Modèle : un produit
    │   │   └── Panier.java          ← Modèle : panier d'achats
    │   └── service/
    │       └── CommandeService.java ← Logique métier
    └── test/java/fr/epsi/service/
        ├── CommandeServiceTest.java  ← Tests unitaires (11 tests)
        └── CommandeServiceIT.java   ← Tests d'intégration (3 tests)
```

---

## 🚀 Démarrage rapide

### Prérequis
- Java 17 (`java -version`)
- Maven 3.9+ (`mvn -version`)
- Git (`git --version`)

### Cloner et tester en local

```bash
# Cloner le projet
git clone https://github.com/VOTRE_USERNAME/tp-jenkins.git
cd tp-jenkins

# Tests unitaires uniquement
mvn clean test

# Tout : tests + intégration + couverture
mvn clean verify

# Tout + analyse qualité complète
mvn clean verify checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs
```

### Résultats attendus

```
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
Tests run: 3,  Failures: 0, Errors: 0, Skipped: 0  (intégration)
[INFO] BUILD SUCCESS
```

---

## 📊 Rapports générés

Après `mvn clean verify checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs` :

| Rapport | Chemin | Utilisé par Jenkins |
|---|---|---|
| Tests unitaires | `target/surefire-reports/*.xml` | Plugin JUnit |
| Tests intégration | `target/failsafe-reports/*.xml` | Plugin JUnit |
| Couverture | `target/site/jacoco/index.html` | Plugin JaCoCo |
| Checkstyle | `target/checkstyle-result.xml` | Warnings NG |
| PMD | `target/pmd.xml` | Warnings NG |
| CPD | `target/cpd.xml` | Warnings NG |
| SpotBugs | `target/spotbugsXml.xml` | Warnings NG |

---

## 🔧 Configuration Jenkins requise

### Global Tool Configuration
- JDK : nom `JDK17`, Java 17
- Maven : nom `Maven3`, version 3.9.x

### Plugins à installer
- Git plugin
- Maven Integration
- JaCoCo Plugin
- Warnings Next Generation
- Role-based Authorization Strategy
- ThinBackup
- Email Extension Plugin
- GitHub plugin (si webhook GitHub)

### Post-build Actions du job
```
Publier les résultats JUnit  : **/surefire-reports/*.xml
                               **/failsafe-reports/*.xml
JaCoCo coverage report       : **/target/jacoco.exec
Warnings NG – Checkstyle     : **/checkstyle-result.xml
Warnings NG – PMD            : **/pmd.xml
Warnings NG – CPD            : **/cpd.xml
Warnings NG – SpotBugs       : **/spotbugsXml.xml
```

---

## 📚 Ressources

- [Jenkins.io](https://jenkins.io) – Documentation officielle
- [JUnit 5](https://junit.org/junit5/) – Documentation JUnit
- [JaCoCo](https://www.jacoco.org) – Documentation couverture
- [Checkstyle](https://checkstyle.org) – Règles disponibles
- [SpotBugs](https://spotbugs.github.io) – Documentation
