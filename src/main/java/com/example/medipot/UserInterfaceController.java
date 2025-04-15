package com.example.medipot;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class UserInterfaceController {

    @FXML
    private TextField searchField; // Eingabefeld für die Suche
    @FXML
    private Button searchButton; // Such-Button
    @FXML
    private ListView<String> resultList; // Liste zur Anzeige der Suchergebnisse

    private Searchmodul searchmodul; // Referenz zum Suchmodul

    // Initialisierungsmethode
    public void initialize() {
        searchButton.setOnAction(event -> performSearch());
    }

    // Methode zur Initialisierung des Suchmoduls
    public void setSearchmodul(Searchmodul searchmodul) {
        this.searchmodul = searchmodul;
    }

    // Methode zur Durchführung der Suche
    private void performSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            resultList.getItems().clear();
            resultList.getItems().add("Bitte geben Sie einen Suchbegriff ein.");
            return;
        }

        List<Pharmacy> results = searchmodul.searchByMedication(query);
        resultList.getItems().clear();

        if (results.isEmpty()) {
            resultList.getItems().add("Keine Ergebnisse gefunden.");
        } else {
            for (Pharmacy pharmacy : results) {
                resultList.getItems().add(pharmacy.getName() + " - " + pharmacy.getAddress());
            }
        }
    }
}