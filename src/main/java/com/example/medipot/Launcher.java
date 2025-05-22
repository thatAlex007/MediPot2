package com.example.medipot;

import javafx.application.Application;

import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(MainGui.class, args);

        /*
        DatabaseService db = new DatabaseService();
        List<Medication> meds = db.getAllMedications();

        System.out.println("Verfügbare Medikamentennamen:");
        for (Medication m : meds) {
            System.out.println("'" + m.getName() + "'");
        }*/


    }
    }