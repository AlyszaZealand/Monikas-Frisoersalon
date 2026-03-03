package org.example.monikasfrisoersalon.View.Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Model.Employee;

public class AdminAppointmentController {

    // Konto
    private Employee employee;
    private Administrator administrator;

    // Kolonner
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableView<Customer> customerTable;

    // Labels
    @FXML private Label usernameText;

    // Knapper
    @FXML private Button addAppointment;
    @FXML private Button deleteAppointment;
    @FXML private Button editAppointment;
    @FXML private Button logoutButton;

    // ------------------------------------------------------------------------------------------------------------- //

    // Tilføj Appointment
    @FXML
    private void onAddAppointment() {

    }

    // Slet Appointment
    @FXML
    private void onDeleteAppointment() {

    }

    // Rediger Appointment
    @FXML
    private void  onEditAppointment() {

    }

    // Log Ud
    @FXML
    private void onLogout(ActionEvent event) {
        try {
            SceneSwitch.switchScene(event, "/login-view.fxml");
            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Log ud");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
