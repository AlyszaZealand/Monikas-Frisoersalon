package org.example.monikasfrisoersalon.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Service.AdministratorService;

import java.net.Authenticator;
import java.util.List;

//import static org.example.monikasfrisoersalon.Controller.AlertController.showAlert;

public class LoginController {



    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;

    private Authenticator aut;


    public void setServices(Authenticator aut){
        this.aut = aut;
    }

    @FXML public void onLoginButtonClick() {
        String email = emailField.getText();
        String password = passwordField.getText();



        switch (email, password) {
            aut.login(email, password);
            case 1:
                if (Admin) {
                    AlertController.showAlert("Login Success","Administrator Login Success");
                } else {
                    AlertController.showAlert("Login Fail", "Try Again");
                }
                break;

            case 2:
                if (Emp) {
                    AlertController.showAlert("Login Success", "Employee Login Success");
                } else {
                    AlertController.showAlert("Login Fail", "Try Again");
                }
                break;

            default:
                AlertController.showAlert("Login Fail", "Try Again");

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
