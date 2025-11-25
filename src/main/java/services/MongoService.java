package services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoService {
    private static MongoService instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoService() {
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("employee_db");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    public static MongoService getInstance() {
        if (instance == null) {
            instance = new MongoService();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}

