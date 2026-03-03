package org.example.monikasfrisoersalon.View.Controller;

// JavaFX Alert
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertController {

    // ShowAlert metode
    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
