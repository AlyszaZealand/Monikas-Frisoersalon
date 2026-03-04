package org.example.monikasfrisoersalon.View.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Service.AppointmentService;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Service.EmployeeService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    @FXML private TableColumn<Appointment, String> employeeColumn;
    @FXML private TableColumn<Appointment, String> customerColumn;
    @FXML private TableColumn<Appointment, String> treatmentColumn;
    @FXML private TableColumn<Appointment, String> startDateColumn;
    @FXML private TableColumn<Appointment, String> endDateColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    @FXML private TableView<Employee> employeeTable; // Tabel til employees
    @FXML private TableColumn<Appointment, String> employeeNameColumn; // Navn på employee
    @FXML private TableColumn<Appointment, String> employeePhoneNumberColumn; // employee telefonnummer

    // Labels
    @FXML private Label usernameText;

    // Knapper
    @FXML private Button addAppointment;
    @FXML private Button deleteAppointment;
    @FXML private Button editAppointment;
    @FXML private Button logoutButton;

    // Admin specifikke knapper
    @FXML private Button deleteEmployeesButton;
    @FXML private Button editEmployeesButton;

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

    // Calendar
//    private void CalendarTable() {
//        employeeNameColumn.cellFactoryProperty(new PropertyValueFactory<>("Navn")); // Navn column
//        employeePhoneNumberColumn.cellFactoryProperty(new PropertyValueFactory<>("Telefonnummer")); // Telefonnummer column
//
//    }

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


    @FXML
    private void initialize() {
        setupColumns();
        loadAppointments();
    }

    private void setupColumns() {
        customerColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCustomer().getUsername()));

        employeeColumn.setCellValueFactory(cell -> {
            // Employee can be null if unassigned
            Employee emp = cell.getValue().getEmployee();
            return new SimpleStringProperty(emp != null ? emp.getUsername() : "Ikke tildelt");
        });

        treatmentColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTreatment().getTypeOfTreatment()));

        startDateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStartDate()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));

        endDateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getEndDate()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));

        statusColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getAppStatus() ? "Aktiv" : "Aflyst"));
    }

    private void loadAppointments() {
        List<Appointment> appointmentList = appointmentService.getAllAppointments();
        appointmentTable .getItems().setAll(appointmentList);
    }



}
