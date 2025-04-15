package com.example.medipot;

import java.nio.file.Paths;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
        port(4567);

        // Absoluter Pfad zu deinem web-Ordner
        String webPath = Paths.get("src/main/resources/web").toAbsolutePath().toString();
        externalStaticFileLocation(webPath);

        // Weiterleitung zur Startseite
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        // API-Endpunkt für Impressum
        get("/api/impressum", (req, res) -> {
            res.type("application/json");
            return "{ \"impressum\": \"" + Impressum.getImpressum().replace("\n", "\\n") + "\" }";
        });

        System.out.println("✅ Läuft unter http://localhost:4567");
    }
}