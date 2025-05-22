package com.example.medipot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());
    private String url;

    public DatabaseService() {
        try {

            File dbFile = new File(System.getProperty("user.home") + "/.medipot/medipot.db");

            if (!dbFile.exists()) {
                try (InputStream in = getClass().getResourceAsStream("/medipot.db")) {
                    if (in == null) throw new FileNotFoundException("Ressource 'medipot.db' nicht gefunden!");
                    dbFile.getParentFile().mkdirs(); // Zielordner erstellen
                    Files.copy(in, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("medipot.db wurde extrahiert nach: " + dbFile.getAbsolutePath());
                }
            }

            this.url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim Extrahieren der Datenbank", e);
            throw new RuntimeException(e);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private <T> List<T> executeQuery(String query, QueryMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Fehler bei der Abfrage: " + query, e);
        }
        return results;
    }

    public List<Medication> getAllMedications() {
        String query = "SELECT * FROM m_medikamente";
        return executeQuery(query, resultSet -> new Medication(
                resultSet.getInt("m_id"),
                resultSet.getString("m_name"),
                resultSet.getString("m_hersteller"),
                resultSet.getDouble("m_preis"),
                resultSet.getInt("m_vorrat"),
                resultSet.getString("m_kategorie"),
                resultSet.getInt("m_rezeptpflichtig") == 1
        ));
    }

    public List<Pharmacy> getAllPharmacies() {
        String query = "SELECT * FROM a_apotheken";
        List<Pharmacy> pharmacies = executeQuery(query, resultSet -> new Pharmacy(
                resultSet.getInt("a_id"),
                resultSet.getString("a_name"),
                resultSet.getString("a_standort"),
                null // Inventar wird danach geladen
        ));

        for (Pharmacy pharmacy : pharmacies) {
            List<Medication> inventory = getInventoryByPharmacy(pharmacy.getId());
            pharmacy.setInventory(inventory);
        }

        return pharmacies;
    }

    public List<Medication> getInventoryByPharmacy(int pharmacyId) {
        String query = """
            SELECT * FROM m_medikamente WHERE m_a_id = ?
            """;
        return executeQuery(query, resultSet -> new Medication(
                resultSet.getInt("m_id"),
                resultSet.getString("m_name"),
                resultSet.getString("m_hersteller"),
                resultSet.getDouble("m_preis"),
                resultSet.getInt("m_vorrat"),
                resultSet.getString("m_kategorie"),
                resultSet.getInt("m_rezeptpflichtig") == 1
        ), pharmacyId);
    }

    public boolean isMedicationAvailableInPharmacy(int pharmacyId, String medicationName) {
        String query = """
            SELECT m_vorrat FROM m_medikamente
            WHERE m_a_id = ? AND LOWER(TRIM(m_name)) = LOWER(TRIM(?))
            """;

        List<Integer> results = executeQuery(query,
                resultSet -> resultSet.getInt("m_vorrat"),
                pharmacyId, medicationName);

        LOGGER.info("Checking availability for '" + medicationName + "' in pharmacy " + pharmacyId +
                ": " + (!results.isEmpty() && results.get(0) > 0));

        return !results.isEmpty() && results.get(0) > 0;
    }

    public void testConnection() {
        try (Connection connection = connect()) {
            if (connection != null) {
                LOGGER.info("Datenbankverbindung erfolgreich!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Fehler bei der Datenbankverbindung:", e);
        }
    }
}
