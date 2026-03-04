package org.example.monikasfrisoersalon.View.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Service.AuthenticatorService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthenticatorService authenticatorService;

    public LoginController(AuthenticatorService authenticatorService){
        this.authenticatorService = authenticatorService;
    }

    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Login Sikkerhed
        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            AlertController.showAlert(Alert.AlertType.WARNING, "Skriv venligst både brugernavn og kode");
            return;
        }

        try {
            // Authenticate Brugeren
            User user = authenticatorService.login(username, password);

            // Load Admin View
            if (user instanceof Administrator) {
                SceneSwitch.switchScene(event, "/org/example/monikasfrisoersalon/Adminitrator-view.fxml", user, "Administrator Dashboard");
                AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Administrator Login Fuldført");

            // Load Employee View
            } else if (user instanceof Employee) {
                SceneSwitch.switchScene(event, "/org/example/monikasfrisoersalon/employee-view.fxml", user, "Medarbejder Dashboard");
                AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Employee Login Fuldført");
            }

        } catch (IllegalArgumentException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, e.getMessage());
        } catch (IOException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, "Fejl ved indlæsning af scene: " + e.getMessage());
        }
    }
}
