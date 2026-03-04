package org.example.monikasfrisoersalon.View.Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Service.AppointmentService;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Service.EmployeeService;

import java.io.IOException;

public class AdminController {

    // 1. Variabler til at gemme de ting, vi får fra SceneSwitch
    private final User currentUser;
    private final EmployeeService employeeService;
    private final AppointmentService appointmentService;


    public AdminController(User currentUser, EmployeeService employeeService, AppointmentService appointmentService) {
        this.currentUser = currentUser;
        this.employeeService = employeeService;
        this.appointmentService = appointmentService;
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
