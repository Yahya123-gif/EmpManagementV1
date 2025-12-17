package services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

// Service pour gérer la connexion à MongoDB (pattern Singleton)
public class MongoService {
    private static MongoService instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Constructeur privé pour le pattern Singleton
    private MongoService() {
        try {
            // Connexion à MongoDB sur localhost:27017
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("employee_db");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    // Retourne l'instance unique du service (Singleton)
    public static MongoService getInstance() {
        if (instance == null) {
            instance = new MongoService();
        }
        return instance;
    }

    // Retourne la base de données MongoDB
    public MongoDatabase getDatabase() {
        return database;
    }
}

