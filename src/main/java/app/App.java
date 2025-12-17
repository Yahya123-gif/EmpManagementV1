package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Classe principale de l'application JavaFX
public class App extends Application {
    // Point d'entrée de l'interface graphique
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charge la page d'accueil (dashboard)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
        Parent root = loader.load();
        
        // Crée la scène avec une taille de 1200x700
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode main pour lancer l'application
    public static void main(String[] args) {
        launch(args);
    }
}

