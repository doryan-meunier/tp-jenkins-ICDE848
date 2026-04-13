/**
 * Jenkinsfile – Pipeline CI complète
 * Projet : Boutique en ligne – ICDE848
 *
 * Ce fichier doit être placé à la RACINE du dépôt Git.
 * Jenkins le détecte automatiquement lors de la création du job Pipeline.
 *
 * Stages :
 *   1. Checkout       → récupère le code depuis Git
 *   2. Build          → compile le code source
 *   3. Tests unitaires → lance *Test.java via Surefire
 *   4. Tests intégration → lance *IT.java via Failsafe
 *   5. Couverture     → génère le rapport JaCoCo
 *   6. Qualité        → Checkstyle + PMD + CPD + SpotBugs
 *   7. Archive        → sauvegarde le JAR dans Jenkins
 *
 * Post :
 *   - failure → email à l'équipe
 *   - fixed   → email quand le build repasse au vert
 */

pipeline {

    // Exécuter sur n'importe quel agent disponible
    agent any

    // Outils configurés dans Global Tool Configuration
    tools {
        maven 'Maven3'    // Nom exact défini dans Jenkins
        jdk   'JDK17'     // Nom exact défini dans Jenkins
    }

    // ─────────────────────────────────────────────────
    // PARAMÈTRES (optionnel – pour TP4)
    // ─────────────────────────────────────────────────
    parameters {
        string(
            name:         'BRANCH',
            defaultValue: 'main',
            description:  'Branche Git à builder'
        )
        choice(
            name:    'ENVIRONMENT',
            choices: ['dev', 'staging', 'prod'],
            description: 'Environnement de déploiement cible'
        )
        booleanParam(
            name:         'SKIP_TESTS',
            defaultValue: false,
            description:  'Ignorer les tests (urgence uniquement !)'
        )
    }

    // ─────────────────────────────────────────────────
    // STAGES
    // ─────────────────────────────────────────────────
    stages {

        // ── Stage 1 : Récupérer le code ──────────────
        stage('Checkout') {
            steps {
                checkout scm
                echo "Branch  : ${env.GIT_BRANCH}"
                echo "Commit  : ${env.GIT_COMMIT}"
            }
        }

        // ── Stage 2 : Compiler ───────────────────────
        stage('Build') {
            steps {
                sh 'mvn clean compile -B'
                // -B = batch mode (pas de couleurs, logs Jenkins-friendly)
            }
        }

        // ── Stage 3 : Tests unitaires ─────────────────
        stage('Tests unitaires') {
            when {
                // Sauter si le paramètre SKIP_TESTS est activé
                not { expression { return params.SKIP_TESTS } }
            }
            steps {
                sh 'mvn test -B'
            }
            post {
                always {
                    // Publier les résultats dans Jenkins (graphique de tendance)
                    junit '**/target/surefire-reports/*.xml'
                }
                failure {
                    echo 'Tests unitaires en ECHEC — vérifier les logs ci-dessus'
                }
            }
        }

        // ── Stage 4 : Tests d'intégration ────────────
        stage('Tests intégration') {
            when {
                not { expression { return params.SKIP_TESTS } }
            }
            steps {
                sh 'mvn verify -Dsurefire.skip=true -B'
            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }

        // ── Stage 5 : Couverture de code ─────────────
        stage('Couverture JaCoCo') {
            steps {
                sh 'mvn jacoco:report -B'
            }
            post {
                always {
                    jacoco(
                        execPattern:   '**/target/jacoco.exec',
                        classPattern:  '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        minimumLineCoverage: '70'
                    )
                }
            }
        }

        // ── Stage 6 : Analyse qualité ─────────────────
        stage('Qualité') {
            steps {
                sh '''
                    mvn checkstyle:checkstyle \
                        pmd:pmd \
                        pmd:cpd \
                        spotbugs:spotbugs \
                        -B
                '''
            }
            post {
                always {
                    recordIssues(
                        enabledForFailure: true,
                        tools: [
                            checkStyle(pattern: '**/checkstyle-result.xml'),
                            pmdParser(pattern:  '**/pmd.xml'),
                            cpd(pattern:        '**/cpd.xml'),
                            spotBugs(pattern:   '**/spotbugsXml.xml')
                        ],
                        // Rendre le build UNSTABLE si > 10 avertissements
                        qualityGates: [[
                            threshold: 10,
                            type: 'TOTAL',
                            unstable: true
                        ]]
                    )
                }
            }
        }

        // ── Stage 7 : Archiver le JAR ─────────────────
        stage('Archive') {
            steps {
                archiveArtifacts(
                    artifacts:   '**/target/*.jar',
                    fingerprint: true,
                    allowEmptyArchive: false
                )
                echo "Artefact archivé avec succès"
            }
        }

        // ── Stage 8 : Validation manuelle avant PROD ──
        // (Décommenter pour TP4 – Input step)
        /*
        stage('Validation PROD') {
            when { expression { return params.ENVIRONMENT == 'prod' } }
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    input(
                        message:   "Déployer en PRODUCTION ?",
                        ok:        "Oui, déployer",
                        submitter: "admin,tech-lead"
                    )
                }
            }
        }
        */

        // ── Stage 9 : Déploiement ─────────────────────
        // (Décommenter et adapter à votre contexte)
        /*
        stage('Deploy') {
            steps {
                sh "./deploy.sh ${params.ENVIRONMENT}"
            }
        }
        */

    } // fin stages

    // ─────────────────────────────────────────────────
    // POST — Actions après tous les stages
    // ─────────────────────────────────────────────────
    post {

        // Toujours exécuté (succès ou échec)
        always {
            echo "Pipeline terminée — statut : ${currentBuild.currentResult}"
        }

        // Seulement en cas d'échec
        failure {
            emailext(
                subject: "❌ FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Le build a échoué.

Projet  : ${env.JOB_NAME}
Build   : #${env.BUILD_NUMBER}
Branche : ${env.GIT_BRANCH}
URL     : ${env.BUILD_URL}

Consulter les logs : ${env.BUILD_URL}console
                """,
                to:          'equipe-dev@monentreprise.fr',
                attachLog:   true
            )
        }

        // Seulement quand le build repasse de FAILURE à SUCCESS
        fixed {
            emailext(
                subject: "✅ FIXED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body:    "Le build est de nouveau stable : ${env.BUILD_URL}",
                to:      'equipe-dev@monentreprise.fr'
            )
        }

    } // fin post

} // fin pipeline
