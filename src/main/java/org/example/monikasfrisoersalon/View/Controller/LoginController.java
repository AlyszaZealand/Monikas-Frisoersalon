package org.example.monikasfrisoersalon.View.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Service.AuthenticatorService;
import org.example.monikasfrisoersalon.View.Initialize;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;
    @FXML private Label username;
    @FXML private Label password;

    private final AuthenticatorService authenticatorService;

    public LoginController(AuthenticatorService aut){
        this.authenticatorService = aut;
    }

    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            AlertController.showAlert("Login Fejl", "Skriv venligst både brugernavn og kode");
        }

        try {
            User user = authenticatorService.login(username, password);

            if (user instanceof Administrator) {
                AlertController.showAlert("Login Fuldført", "Administrator Login Fuldført");
                // load admin view
            } else if (user instanceof Employee) {
                AlertController.showAlert("Login Fuldført", "Employee Login Fuldført");
                // load employee view
            }

        } catch (IllegalArgumentException e) {
            AlertController.showAlert("Login Fejl", "Prøv Igen");
        }
    }

//        @FXML public void onLoginButtonClick(ActionEvent event){
//            String email = emailField.getText();
//            statusLabel.setText("");
//
//            try{
//                User foundUser = userService.login(email);
//                SceneSwitch.switchToStreaming(event, foundUser, userService, movieService, favService);
//            } catch(ValidateException e){
//                statusLabel.setText(e.getMessage());
//            } catch(Exception e){
//                AlertController.showAlert("Error", "Der skete en fel: " + e.getMessage());
//            }
//        }
//
//    public User login(String email, String password){
//        if (email == null || email.isEmpty()){
//            throw new ValidateException("Skriv en email i feltet");
//        }
//        List<User> users = userRepo.findByEmail(email);
//
//        if(users == null || users.isEmpty()){
//            throw new ValidateException("Ingen bruger fundet med denne email");
//        }
//        return users.get(0);
//    }


}
