package com.example.medipot; // Definiert das Paket, in dem sich die Klasse befindet.

import java.sql.*; // Importiert Klassen für den Zugriff auf SQL-Datenbanken.
import java.util.ArrayList; // Importiert die ArrayList-Klasse.
import java.util.List; // Importiert die List-Schnittstelle.
import java.util.logging.Level; // Importiert die Level-Klasse für Logging.
import java.util.logging.Logger; // Importiert die Logger-Klasse für Protokollierung.

public class DatabaseService { // Definiert die Klasse für den Datenbankzugriff.
    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName()); // Erstellt einen Logger für die Klasse.
    private String url; // Speichert die URL der Datenbank.

    public DatabaseService() { // Konstruktor der Klasse.
        this.url = "jdbc:sqlite:C:/Users/enesy/OneDrive - HTL Spengergasse/Power - General/MediPot/DataBase/medipot.db"; // Setzt die URL der SQLite-Datenbank.
    }

    private Connection connect() throws SQLException { // Stellt eine Verbindung zur Datenbank her.
        return DriverManager.getConnection(url); // Gibt eine neue Verbindung basierend auf der URL zurück.
    }

    private <T> List<T> executeQuery(String query, QueryMapper<T> mapper, Object... params) {
        // Führt eine SQL-Abfrage aus und wandelt die Ergebnisse in Objekte vom Typ T um.
        List<T> results = new ArrayList<>(); // Erstellt eine Liste für die Ergebnisse.
        try (Connection connection = connect(); // Stellt die Verbindung her.
             PreparedStatement preparedStatement = connection.prepareStatement(query)) { // Bereitet die SQL-Abfrage vor.

            for (int i = 0; i < params.length; i++) { // Setzt die Parameter in die Abfrage ein.
                preparedStatement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) { // Führt die Abfrage aus und erhält ein ResultSet.
                while (resultSet.next()) { // Iteriert über die Ergebnisse.
                    results.add(mapper.map(resultSet)); // Wandelt jede Zeile in ein Objekt um und fügt es der Liste hinzu.
                }
            }
        } catch (SQLException e) { // Fängt SQL-Fehler ab.
            LOGGER.log(Level.SEVERE, "Fehler bei der Abfrage: " + query, e); // Protokolliert den Fehler.
        }
        return results; // Gibt die Liste der Ergebnisse zurück.
    }

    public List<Medication> getAllMedications() {
        // Ruft alle Medikamente aus der Datenbank ab.
        String query = "SELECT * FROM Medication"; // SQL-Abfrage für alle Medikamente.
        return executeQuery(query, resultSet -> new Medication( // Führt die Abfrage aus und wandelt die Ergebnisse in Medication-Objekte um.
                resultSet.getInt("id"), // ID des Medikaments.
                resultSet.getString("name"), // Name des Medikaments.
                resultSet.getString("manufacturer"), // Hersteller des Medikaments.
                resultSet.getDouble("price"), // Preis des Medikaments.
                resultSet.getInt("stock"), // Lagerbestand des Medikaments.
                resultSet.getString("category"), // Kategorie des Medikaments.
                resultSet.getBoolean("prescription_required") // Gibt an, ob ein Rezept erforderlich ist.
        ));
    }

    public List<Pharmacy> getAllPharmacies() {
        // Ruft alle Apotheken aus der Datenbank ab.
        String query = "SELECT * FROM Pharmacy"; // SQL-Abfrage für alle Apotheken.
        List<Pharmacy> pharmacies = executeQuery(query, resultSet -> new Pharmacy( // Führt die Abfrage aus und wandelt die Ergebnisse in Pharmacy-Objekte um.
                resultSet.getInt("id"), // ID der Apotheke.
                resultSet.getString("name"), // Name der Apotheke.
                resultSet.getString("address"), // Adresse der Apotheke.
                null // Das Inventar wird später hinzugefügt.
        ));

        for (Pharmacy pharmacy : pharmacies) { // Iteriert über alle Apotheken.
            List<Medication> inventory = getInventoryByPharmacy(pharmacy.getId()); // Ruft das Inventar der Apotheke ab.
            pharmacy.setInventory(inventory); // Setzt das Inventar der Apotheke.
        }

        return pharmacies; // Gibt die Liste der Apotheken zurück.
    }

    public boolean isMedicationAvailableInPharmacy(int pharmacyId, String medicationName) {
        // Überprüft, ob ein Medikament in einer bestimmten Apotheke verfügbar ist.
        String query = """
            SELECT i.quantity
            FROM Inventory i
            JOIN Medication m ON i.medication_id = m.id
            WHERE i.pharmacy_id = ? AND LOWER(TRIM(m.name)) = LOWER(TRIM(?))
            """; // SQL-Abfrage für die Verfügbarkeit eines Medikaments.

        List<Integer> results = executeQuery(query, // Führt die Abfrage aus.
                resultSet -> resultSet.getInt("quantity"), // Holt die Menge des Medikaments.
                pharmacyId, medicationName); // Setzt die Parameter für die Abfrage.

        LOGGER.info("Checking availability for '" + medicationName + "' in pharmacy " + pharmacyId +
                ": " + (!results.isEmpty() && results.get(0) > 0)); // Protokolliert die Verfügbarkeit.

        return !results.isEmpty() && results.get(0) > 0; // Gibt true zurück, wenn das Medikament verfügbar ist.
    }

    public List<Medication> getInventoryByPharmacy(int pharmacyId) {
        // Ruft das Inventar einer bestimmten Apotheke ab.
        String query = """
            SELECT m.id, m.name, m.manufacturer, m.price, i.quantity, 
                   m.category, m.prescription_required
            FROM Inventory i
            JOIN Medication m ON i.medication_id = m.id
            WHERE i.pharmacy_id = ?
            """; // SQL-Abfrage für das Inventar einer Apotheke.

        return executeQuery(query, resultSet -> new Medication( // Führt die Abfrage aus und wandelt die Ergebnisse in Medication-Objekte um.
                resultSet.getInt("id"), // ID des Medikaments.
                resultSet.getString("name"), // Name des Medikaments.
                resultSet.getString("manufacturer"), // Hersteller des Medikaments.
                resultSet.getDouble("price"), // Preis des Medikaments.
                resultSet.getInt("quantity"), // Menge des Medikaments.
                resultSet.getString("category"), // Kategorie des Medikaments.
                resultSet.getBoolean("prescription_required") // Gibt an, ob ein Rezept erforderlich ist.
        ), pharmacyId); // Setzt die Apotheke-ID als Parameter.
    }

    public void testConnection() {
        // Testet die Verbindung zur Datenbank.
        try (Connection connection = connect()) { // Stellt die Verbindung her.
            if (connection != null) { // Überprüft, ob die Verbindung erfolgreich ist.
                LOGGER.info("Datenbankverbindung erfolgreich!"); // Protokolliert den Erfolg.
            }
        } catch (SQLException e) { // Fängt SQL-Fehler ab.
            LOGGER.log(Level.SEVERE, "Fehler bei der Datenbankverbindung:", e); // Protokolliert den Fehler.
        }
    }
}