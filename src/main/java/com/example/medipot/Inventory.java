package com.example.medipot;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Pharmacy pharmacy; // Referenz zur Apotheke
    private Map<Medication, Integer> stock; // Lagerbestand: Medikament -> Menge

    // Konstruktor
    public Inventory(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        this.stock = new HashMap<>();
    }

    // Getter und Setter
    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Map<Medication, Integer> getStock() {
        return stock;
    }

    public void setStock(Map<Medication, Integer> stock) {
        this.stock = stock;
    }

    // Methode zum Hinzufügen eines Medikaments
    public void addMedication(Medication medication, int quantity) {
        stock.put(medication, stock.getOrDefault(medication, 0) + quantity);
    }

    // Methode zum Entfernen eines Medikaments
    public boolean removeMedication(Medication medication, int quantity) {
        if (stock.containsKey(medication) && stock.get(medication) >= quantity) {
            stock.put(medication, stock.get(medication) - quantity);
            if (stock.get(medication) == 0) {
                stock.remove(medication);
            }
            return true;
        }
        return false; // Nicht genügend Bestand
    }

    // Methode zur Abfrage der Menge eines Medikaments
    public int getQuantity(Medication medication) {
        return stock.getOrDefault(medication, 0);
    }

    // Überschreiben der toString-Methode für eine lesbare Darstellung
    @Override
    public String toString() {
        return "Inventory{" +
                "pharmacy=" + pharmacy.getName() +
                ", stock=" + stock +
                '}';
    }
}