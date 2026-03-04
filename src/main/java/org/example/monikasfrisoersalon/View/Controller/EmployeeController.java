package org.example.monikasfrisoersalon.View.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.monikasfrisoersalon.Model.*;
import org.example.monikasfrisoersalon.Service.AppointmentService;
import org.example.monikasfrisoersalon.Service.CalendarService;
import org.example.monikasfrisoersalon.Service.CustomerService;
import org.example.monikasfrisoersalon.Service.TreatmentService;

import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeController {

    private final User currentUser;
    private final AppointmentService appointmentService;
    private final CalendarService calendarService;
    private final CustomerService customerService;
    private final TreatmentService treatmentService;



    public EmployeeController(User currentUser, AppointmentService appointmentService, CalendarService calendarService, CustomerService customerService, TreatmentService treatmentService) {
        this.currentUser = currentUser;
        this.appointmentService = appointmentService;
        this.calendarService = calendarService;
        this.customerService = customerService;
        this.treatmentService = treatmentService;

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

