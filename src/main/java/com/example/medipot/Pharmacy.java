package com.example.medipot; // Definiert das Paket, in dem sich die Klasse befindet.

import java.util.List; // Importiert die List-Schnittstelle.

public class Pharmacy { // Repräsentiert eine Apotheke.
    private int id; // Eindeutige ID der Apotheke.
    private String name; // Name der Apotheke.
    private String address; // Adresse der Apotheke.
    private List<Medication> inventory; // Liste der Medikamente, die in der Apotheke verfügbar sind.

    public Pharmacy(int id, String name, String address, List<Medication> inventory) {
        // Konstruktor, der die Attribute der Apotheke initialisiert.
        this.id = id; // Setzt die ID.
        this.name = name; // Setzt den Namen.
        this.address = address; // Setzt die Adresse.
        this.inventory = inventory; // Setzt die Medikamentenliste.
    }

    public int getId() {
        // Gibt die ID der Apotheke zurück.
        return id;
    }

    public String getName() {
        // Gibt den Namen der Apotheke zurück.
        return name;
    }

    public String getAddress() {
        // Gibt die Adresse der Apotheke zurück.
        return address;
    }

    public List<Medication> getInventory() {
        // Gibt die Liste der Medikamente zurück.
        return inventory;
    }

    public void setInventory(List<Medication> inventory) {
        // Setzt die Medikamentenliste der Apotheke.
        this.inventory = inventory;
    }

    public Medication findMedication(String medicationName) {
        // Sucht ein Medikament in der Apotheke anhand des Namens.
        if (inventory != null) { // Überprüft, ob die Medikamentenliste nicht null ist.
            for (Medication medication : inventory) { // Iteriert über die Medikamentenliste.
                if (medication.getName().equalsIgnoreCase(medicationName.trim())) {
                    // Vergleicht den Namen des Medikaments (ohne Groß-/Kleinschreibung und Leerzeichen).
                    return medication; // Gibt das gefundene Medikament zurück.
                }
            }
        }
        return null; // Gibt null zurück, wenn das Medikament nicht gefunden wurde.
    }

    @Override
    public String toString() {
        // Gibt eine lesbare Darstellung der Apotheke zurück.
        return name + " (" + address + ")";
    }
}