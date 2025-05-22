package com.example.medipot; // Definiert das Paket, in dem sich die Klasse befindet.

import javafx.application.Application; // Importiert die JavaFX-Anwendungsklasse.
import javafx.application.Platform; // Ermöglicht die Ausführung von Code im JavaFX-Thread.
import javafx.geometry.Insets; // Definiert Abstände (Padding/Margin).
import javafx.geometry.Pos; // Definiert die Ausrichtung von Elementen.
import javafx.scene.Scene; // Repräsentiert eine Szene in JavaFX.
import javafx.scene.control.*; // Importiert Steuerelemente wie Button, Label, ComboBox.
import javafx.scene.image.Image; // Repräsentiert ein Bild.
import javafx.scene.image.ImageView; // Zeigt ein Bild an.
import javafx.scene.input.KeyCode; // Repräsentiert Tasten auf der Tastatur.
import javafx.scene.layout.*; // Importiert Layouts wie VBox, HBox, BorderPane.
import javafx.scene.text.Font; // Definiert Schriftarten.
import javafx.scene.text.FontWeight; // Definiert Schriftstärken.
import javafx.stage.Stage; // Repräsentiert ein Fenster in JavaFX.

import java.util.List; // Importiert die List-Schnittstelle.
import java.util.stream.Collectors; // Ermöglicht das Sammeln von Streams in Listen.

public class MainGui extends Application { // Hauptklasse, die die JavaFX-Anwendung definiert.
    private DatabaseService databaseService = new DatabaseService(); // Instanziiert den Datenbankdienst.
    private String lastSelectedMedication = ""; // Speichert den zuletzt ausgewählten Medikamentennamen.

    @Override
    public void start(Stage primaryStage) { // Startmethode, die die Benutzeroberfläche initialisiert.
        primaryStage.setTitle("MediPot"); // Setzt den Fenstertitel.

        // Hauptlayout
        BorderPane root = new BorderPane(); // Erstellt ein BorderPane-Layout.
        root.setStyle("-fx-background-color: #f7fbff;"); // Setzt die Hintergrundfarbe.

        // Logo und Titel oben
        Image logoImage = new Image("C:\\Users\\alexa\\Documents\\GitHub\\MediPot2\\src\\main\\java\\com\\example\\medipot\\Logo_Medipot_Power.png"); // Lädt das Logo.
        ImageView logoView = new ImageView(logoImage); // Erstellt ein ImageView für das Logo.
        logoView.setFitHeight(80); // Setzt die Höhe des Logos.
        logoView.setPreserveRatio(true); // Beibehaltung des Seitenverhältnisses.

        Label titleLabel = new Label("MediPot"); // Erstellt ein Label für den Titel.
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 32)); // Setzt die Schriftart und -größe.
        titleLabel.setStyle("-fx-text-fill: #0d47a1;"); // Setzt die Schriftfarbe.
        titleLabel.setAlignment(Pos.CENTER_LEFT); // Zentriert den Text links.

        VBox titleBox = new VBox(titleLabel); // Verpackt das Label in eine VBox.
        titleBox.setAlignment(Pos.CENTER_LEFT); // Zentriert die VBox links.

        HBox headerBox = new HBox(10, logoView, titleBox); // Erstellt eine HBox für das Logo und den Titel.
        headerBox.setAlignment(Pos.CENTER_LEFT); // Zentriert die HBox links.
        headerBox.setPadding(new Insets(20)); // Fügt Innenabstände hinzu.

        // Impressum-Button rechts oben
        Button impressumButton = new Button("Impressum"); // Erstellt einen Button für das Impressum.
        impressumButton.setStyle("""
                -fx-font-size: 14px;
                -fx-background-color: #0d47a1;
                -fx-text-fill: white;
                -fx-padding: 5 15;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 5, 0, 0, 1);
                """); // Setzt das Styling des Buttons.
        impressumButton.setOnMouseEntered(e -> impressumButton.setStyle("""
                -fx-font-size: 14px;
                -fx-background-color: #1565c0;
                -fx-text-fill: white;
                -fx-padding: 5 15;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 5, 0, 0, 1);
                """)); // Ändert das Styling bei Mouse-Over.
        impressumButton.setOnMouseExited(e -> impressumButton.setStyle("""
                -fx-font-size: 14px;
                -fx-background-color: #0d47a1;
                -fx-text-fill: white;
                -fx-padding: 5 15;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 5, 0, 0, 1);
                """)); // Setzt das Styling zurück, wenn die Maus den Button verlässt.
        impressumButton.setOnAction(event -> showImpressum()); // Öffnet das Impressum-Fenster bei Klick.

        HBox topRightBox = new HBox(impressumButton); // Verpackt den Button in eine HBox.
        topRightBox.setAlignment(Pos.TOP_RIGHT); // Zentriert die HBox rechts oben.
        topRightBox.setPadding(new Insets(10)); // Fügt Innenabstände hinzu.
        BorderPane topPane = new BorderPane(); // Erstellt ein BorderPane für den oberen Bereich.
        topPane.setLeft(headerBox); // Fügt die HBox mit Logo und Titel links hinzu.
        topPane.setRight(topRightBox); // Fügt den Impressum-Button rechts hinzu.
        root.setTop(topPane); // Setzt das Top-Layout in das Hauptlayout.

        // Zentrale Suchleiste
        VBox centerBox = new VBox(20); // Erstellt eine VBox für die zentrale Suchleiste.
        centerBox.setAlignment(Pos.CENTER); // Zentriert die VBox.
        centerBox.setPadding(new Insets(20)); // Fügt Innenabstände hinzu.

        ComboBox<String> searchField = new ComboBox<>(); // Erstellt ein ComboBox-Element für die Suche.
        searchField.setEditable(true); // Macht das Feld editierbar.
        searchField.setPromptText("Bitte Medikament eingeben"); // Setzt den Platzhaltertext.
        searchField.setPrefWidth(300); // Setzt die Breite des Feldes.
        searchField.setStyle("""
                -fx-font-size: 14px;
                -fx-font-family: "Times New Roman";
                -fx-border-color: #0d47a1;
                -fx-border-width: 2px;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 1);
                """); // Setzt das Styling des Suchfeldes.

        List<Medication> medications = databaseService.getAllMedications(); // Ruft alle Medikamente aus der Datenbank ab.
        List<String> medicationNames = medications.stream() // Extrahiert die Namen der Medikamente.
                .map(Medication::getName)
                .distinct()
                .collect(Collectors.toList());

        searchField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> { // Fügt einen Listener für Texteingaben hinzu.
            if (!newValue.equals(lastSelectedMedication)) { // Überprüft, ob der eingegebene Text nicht dem zuletzt ausgewählten Medikament entspricht.
                lastSelectedMedication = ""; // Setzt die letzte Auswahl zurück.
            }

            if (newValue.isEmpty()) { // Versteckt die Vorschläge, wenn das Feld leer ist.
                searchField.hide();
                return;
            }

            List<String> filteredItems = medicationNames.stream() // Filtert die Medikamentennamen basierend auf der Eingabe.
                    .filter(name -> name.toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());

            Platform.runLater(() -> { // Aktualisiert die Vorschläge im JavaFX-Thread.
                searchField.getItems().setAll(filteredItems);
                if (!filteredItems.isEmpty()) {
                    searchField.show(); // Zeigt die Vorschläge an.
                } else {
                    searchField.hide(); // Versteckt die Vorschläge.
                }
            });
        });

        ListView<String> resultListView = new ListView<>(); // Erstellt eine ListView für die Suchergebnisse.
        resultListView.setPrefHeight(200); // Setzt die Höhe der ListView.
        resultListView.setStyle("""
                -fx-font-size: 14px;
                -fx-font-family: "Times New Roman";
                -fx-border-color: #0d47a1;
                -fx-border-width: 2px;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 1);
                """); // Setzt das Styling der ListView.

        // Warnmeldung Label
        Label warningLabel = new Label(); // Erstellt ein Label für Warnmeldungen.
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-family: 'Times New Roman'; -fx-font-style: italic;"); // Setzt das Styling.
        warningLabel.setVisible(false); // Versteckt das Label standardmäßig.

        Runnable searchAction = () -> { // Definiert die Suchaktion.
            String medicationName = searchField.getEditor().getText().trim(); // Holt den eingegebenen Medikamentennamen.
            resultListView.getItems().clear(); // Löscht die vorherigen Suchergebnisse.
            warningLabel.setVisible(false); // Versteckt die Warnmeldung.

            if (!medicationName.isEmpty()) { // Überprüft, ob ein Medikamentenname eingegeben wurde.
                resultListView.getItems().add("🔍 Gesuchtes Medikament: " + medicationName); // Fügt den gesuchten Namen zur Liste hinzu.

                List<Pharmacy> pharmacies = databaseService.getAllPharmacies(); // Ruft alle Apotheken aus der Datenbank ab.
                boolean found = false; // Gibt an, ob das Medikament gefunden wurde.
                boolean prescriptionRequired = false; // Gibt an, ob das Medikament rezeptpflichtig ist.

                for (Pharmacy pharmacy : pharmacies) { // Iteriert über alle Apotheken.
                    Medication medication = pharmacy.findMedication(medicationName); // Sucht das Medikament in der Apotheke.
                    if (medication != null) { // Wenn das Medikament gefunden wurde:
                        resultListView.getItems().add("✓ " + pharmacy.getName() + " - " + medication); // Fügt die Apotheke und das Medikament zur Liste hinzu.

                        if (medication.isPrescriptionRequired()) { // Überprüft, ob das Medikament rezeptpflichtig ist.
                            prescriptionRequired = true; // Setzt die Rezeptpflicht auf true.
                        }

                        found = true; // Setzt den Fundstatus auf true.
                    } else {
                        resultListView.getItems().add("✗ " + pharmacy.getName() + " - Nicht verfügbar"); // Fügt eine Meldung hinzu, dass das Medikament nicht verfügbar ist.
                    }
                }

                // Zeige Warnung in der Benutzeroberfläche
                if (prescriptionRequired) { // Wenn das Medikament rezeptpflichtig ist:
                    Platform.runLater(() -> { // Zeigt die Warnmeldung im JavaFX-Thread an.
                        warningLabel.setText("Das Medikament \"" + medicationName + "\" ist rezeptpflichtig!");
                        warningLabel.setVisible(true);
                    });
                }

                if (!found) { // Wenn das Medikament nicht gefunden wurde:
                    resultListView.getItems().add("❌ Kein Bestand für \"" + medicationName + "\" gefunden"); // Fügt eine Fehlermeldung hinzu.
                }
            } else {
                resultListView.getItems().add("⚠ Bitte einen Medikamentennamen eingeben"); // Zeigt eine Warnung an, wenn kein Name eingegeben wurde.
            }
        };

        searchField.getEditor().setOnKeyPressed(event -> { // Fügt einen Listener für Tastendrücke hinzu.
            if (event.getCode() == KeyCode.ENTER) { // Wenn die Enter-Taste gedrückt wird:
                String medicationName = searchField.getEditor().getText().trim(); // Holt den eingegebenen Medikamentennamen.
                if (!medicationName.isEmpty()) { // Wenn der Name nicht leer ist:
                    lastSelectedMedication = medicationName; // Speichert den Namen als letzte Auswahl.
                }
                searchAction.run(); // Führt die Suchaktion aus.
            }
        });

        searchField.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> { // Fügt einen Listener für die Auswahl hinzu.
            if (newVal != null && !newVal.isEmpty()) { // Wenn ein neuer Wert ausgewählt wurde:
                Platform.runLater(() -> { // Aktualisiert das Suchfeld im JavaFX-Thread.
                    searchField.getEditor().setText(newVal);
                    searchField.hide();
                });
                searchAction.run(); // Führt die Suchaktion aus.
            }
        });

        centerBox.getChildren().addAll(searchField, warningLabel, resultListView); // Fügt die Suchleiste, das Warnlabel und die Ergebnisliste zur VBox hinzu.
        root.setCenter(centerBox); // Setzt die VBox in das Zentrum des Hauptlayouts.

        Scene scene = new Scene(root, 600, 500); // Erstellt eine Szene mit dem Hauptlayout.
        primaryStage.setScene(scene); // Setzt die Szene in das Hauptfenster.
        primaryStage.show(); // Zeigt das Hauptfenster an.
    }

    private void showImpressum() { // Methode zum Anzeigen des Impressums.
        Stage impressumStage = new Stage(); // Erstellt ein neues Fenster.
        impressumStage.setTitle("Impressum"); // Setzt den Fenstertitel.

        Label impressumTitle = new Label("Impressum"); // Erstellt ein Label für den Titel.
        impressumTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 20)); // Setzt die Schriftart und -größe.
        impressumTitle.setStyle("-fx-text-fill: #0d47a1;"); // Setzt die Schriftfarbe.

        TextArea impressumText = new TextArea(Impressum.getImpressum()); // Erstellt ein Textfeld mit dem Impressum-Text.
        impressumText.setEditable(false); // Macht das Textfeld nicht editierbar.
        impressumText.setWrapText(true); // Aktiviert den Zeilenumbruch.
        impressumText.setStyle("""
                -fx-font-size: 14px;
                -fx-font-family: "Times New Roman";
                -fx-background-color: #f7fbff;
                -fx-border-color: #0d47a1;
                -fx-border-width: 2px;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                """); // Setzt das Styling des Textfeldes.

        VBox impressumLayout = new VBox(15, impressumTitle, impressumText); // Verpackt den Titel und den Text in eine VBox.
        impressumLayout.setPadding(new Insets(20)); // Fügt Innenabstände hinzu.
        impressumLayout.setAlignment(Pos.TOP_CENTER); // Zentriert die VBox oben.
        impressumLayout.setStyle("-fx-background-color: #ffffff; -fx-border-color: #0d47a1; -fx-border-width: 2px; -fx-border-radius: 15;"); // Setzt das Styling.

        Scene impressumScene = new Scene(impressumLayout, 400, 300); // Erstellt eine Szene für das Impressum.
        impressumStage.setScene(impressumScene); // Setzt die Szene in das Impressum-Fenster.
        impressumStage.show(); // Zeigt das Impressum-Fenster an.
    }

    public static void main(String[] args) { // Hauptmethode der Anwendung.
        launch(args); // Startet die JavaFX-Anwendung.
    }
}