package com.example.medipot; // Definiert das Paket, in dem sich die Klasse befindet.

import java.util.ArrayList; // Importiert die ArrayList-Klasse.
import java.util.List; // Importiert die List-Schnittstelle.

public class Searchmodul { // Definiert die Klasse für die Suchfunktionalität.

    private List<Pharmacy> pharmacies; // Liste der Apotheken, die durchsucht werden sollen.

    public Searchmodul(List<Pharmacy> pharmacies) {
        // Konstruktor, der die Liste der Apotheken initialisiert.
        this.pharmacies = pharmacies; // Weist die übergebene Apothekenliste der Instanzvariablen zu.
    }

    public List<Pharmacy> searchByMedication(String medicationName) {
        // Methode, um Apotheken zu suchen, die ein bestimmtes Medikament führen.
        List<Pharmacy> result = new ArrayList<>(); // Erstellt eine Liste für die Suchergebnisse.
        for (Pharmacy pharmacy : pharmacies) { // Iteriert über alle Apotheken.
            Medication medication = pharmacy.findMedication(medicationName);
            // Sucht das Medikament in der aktuellen Apotheke.
            if (medication != null && medication.getStock() > 0) {
                // Überprüft, ob das Medikament existiert und auf Lager ist.
                result.add(pharmacy); // Fügt die Apotheke der Ergebnisliste hinzu.
            }
        }
        return result; // Gibt die Liste der Apotheken zurück, die das Medikament führen.
    }
}