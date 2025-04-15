package com.example.medipot; // Definiert das Paket, in dem sich die Klasse befindet.

public class Medication { // Repräsentiert ein Medikament.
    private int id; // Eindeutige ID des Medikaments.
    private String name; // Name des Medikaments.
    private String manufacturer; // Hersteller des Medikaments.
    private double price; // Preis des Medikaments.
    private int stock; // Lagerbestand des Medikaments.
    private String category; // Kategorie des Medikaments (z. B. Schmerzmittel, Antibiotika).
    private boolean prescriptionRequired; // Gibt an, ob das Medikament rezeptpflichtig ist.

    public Medication(int id, String name, String manufacturer, double price, int stock, String category, boolean prescriptionRequired) {
        // Konstruktor, der die Attribute des Medikaments initialisiert.
        this.id = id; // Setzt die ID.
        this.name = name; // Setzt den Namen.
        this.manufacturer = manufacturer; // Setzt den Hersteller.
        this.price = price; // Setzt den Preis.
        this.stock = stock; // Setzt den Lagerbestand.
        this.category = category; // Setzt die Kategorie.
        this.prescriptionRequired = prescriptionRequired; // Setzt die Rezeptpflicht.
    }

    public int getId() {
        // Gibt die ID des Medikaments zurück.
        return id;
    }

    public String getName() {
        // Gibt den Namen des Medikaments zurück.
        return name;
    }

    public String getManufacturer() {
        // Gibt den Hersteller des Medikaments zurück.
        return manufacturer;
    }

    public double getPrice() {
        // Gibt den Preis des Medikaments zurück.
        return price;
    }

    public int getStock() {
        // Gibt den Lagerbestand des Medikaments zurück.
        return stock;
    }

    public String getCategory() {
        // Gibt die Kategorie des Medikaments zurück.
        return category;
    }

    public boolean isPrescriptionRequired() {
        // Gibt zurück, ob das Medikament rezeptpflichtig ist.
        return prescriptionRequired;
    }

    @Override
    public String toString() {
        // Gibt eine lesbare Darstellung des Medikaments zurück.
        return name + " (" + category + ") - " + manufacturer + " - €" + price;
    }
}