package org.example.monikasfrisoersalon.View.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Service.AuthenticatorService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label username;
    @FXML private Label password;

    private final AuthenticatorService authenticatorService;

    public LoginController(AuthenticatorService aut){
        this.authenticatorService = aut;
    }

    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Login Sikkerhed
        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            AlertController.showAlert(Alert.AlertType.WARNING, "Skriv venligst både brugernavn og kode");
        }

        try {
            // Authenticate Brugeren
            User user = authenticatorService.login(username, password);

            // Load Admin View
            if (user instanceof Administrator) {
                SceneSwitch.switchScene(event, "/Adminitrator-view.fxml");
                AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Administrator Login Fuldført");

            // Load Employee View
            } else if (user instanceof Employee) {
                SceneSwitch.switchScene(event, "/Employee-view.fxml");
                AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Employee Login Fuldført");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
