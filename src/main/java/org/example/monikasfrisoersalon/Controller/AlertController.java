package org.example.monikasfrisoersalon.Controller;

// JavaFX Alert
import javafx.scene.control.Alert;

public class AlertController {

    // ShowAlert metode
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
