//package com.example.medipot;
//
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        DatabaseService databaseService = new DatabaseService();
//
//        // Test der Datenbankverbindung
//        databaseService.testConnection();
//
//        // Apotheken abrufen
//        List<Pharmacy> pharmacies = databaseService.getAllPharmacies();
//        System.out.println("Apotheken in der Datenbank:");
//        for (Pharmacy pharmacy : pharmacies) {
//            System.out.println(pharmacy);
//        }
//
//        // Lagerbestand einer Apotheke abrufen
//        int pharmacyId = 1; // Beispiel: Apotheke mit ID 1
//        List<Medication> inventory = databaseService.getInventoryByPharmacy(pharmacyId);
//        System.out.println("Lagerbestand der Apotheke mit ID " + pharmacyId + ":");
//        for (Medication medication : inventory) {
//            System.out.println(medication);
//        }
//    }
//}