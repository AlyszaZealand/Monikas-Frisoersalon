package org.example.monikasfrisoersalon.View.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Service.AppointmentService;
import org.example.monikasfrisoersalon.Service.CalendarService;

import java.io.IOException;

public class EmployeeController {

    private final User currentUser;
    private final AppointmentService appointmentService;
    private final CalendarService calendarService;


    public EmployeeController(User currentUser, AppointmentService appointmentService, CalendarService calendarService) {
        this.currentUser = currentUser;
        this.appointmentService = appointmentService;
        this.calendarService = calendarService;
    }

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
    private void onLogout(ActionEvent event, User user) {
        try {
            SceneSwitch.switchScene(event, "/login-view.fxml", user, "Login");
            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Log ud");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

