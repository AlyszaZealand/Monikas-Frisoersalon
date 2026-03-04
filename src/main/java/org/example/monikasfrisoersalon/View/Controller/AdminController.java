package org.example.monikasfrisoersalon.View.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import org.example.monikasfrisoersalon.Model.Appointment;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Service.AppointmentService;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Service.EmployeeService;
import org.example.monikasfrisoersalon.Service.TreatmentService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminController {

    // 1. Variabler til at gemme de ting, vi får fra SceneSwitch
    private final User currentUser;
    private final EmployeeService employeeService;
    private final AppointmentService appointmentService;
    private final TreatmentService treatmentService;



    public AdminController(User currentUser, EmployeeService employeeService, AppointmentService appointmentService, TreatmentService treatmentService) {
        this.currentUser = currentUser;
        this.employeeService = employeeService;
        this.appointmentService = appointmentService;
        this.treatmentService = treatmentService;
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
    @FXML private TableColumn<Employee, String> employeeNameColumn; // Navn på employee
    @FXML private TableColumn<Employee, String> employeePhoneNumberColumn; // employee telefonnummer

    // Labels
    @FXML private Label usernameText;

    //Kalender
    @FXML private DatePicker appointmentDatePicker;

    // Knapper
    @FXML private Button logoutButton;

    //Textfields
    @FXML private TextField employeeNameField;
    @FXML private TextField employeePasswordField;
    @FXML private TextField employeePhoneNumberField;

    // Admin specifikke knapper
    @FXML private Button deleteEmployeeButton;
    @FXML private Button addEmployeeButton;
    @FXML private Button editEmployeeButton;

    // ------------------------------------------------------------------------------------------------------------- //

    // Tilføj Appointment
    @FXML
    private void onAddEmployee() {
        try {
            String name = employeeNameField.getText();
            String password = employeePasswordField.getText();
            String phoneText = employeePhoneNumberField.getText();


            if (name == null || name.trim().isEmpty() ||
                    phoneText == null || phoneText.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                AlertController.showAlert(Alert.AlertType.WARNING, "Udfyld venligst Navn, Telefon og Password for at oprette en medarbejder.");
                return;
            }

            int phoneNumber = Integer.parseInt(phoneText);

            Employee newEmployee = new Employee(0, name, password, phoneNumber);
            employeeService.createEmployee(newEmployee);

            employeeNameField.clear();
            employeePasswordField.clear();
            employeePhoneNumberField.clear();

            AlertController.showAlert(Alert.AlertType.CONFIRMATION,"Medarbejder oprettet i systemet");
            loadEmployees();


        } catch (NumberFormatException e){
            AlertController.showAlert(Alert.AlertType.ERROR,"Fejl: Telefonnummeret skal være et gyldigt heltal.");
        } catch (IllegalArgumentException e){
            AlertController.showAlert(Alert.AlertType.ERROR, "Fejl: " + e.getMessage());
        } catch (java.time.format.DateTimeParseException e){
            AlertController.showAlert(Alert.AlertType.ERROR,"Vælg venligst et gyldgit tidspunkt som f.eks. '10:30'");
        } catch (Exception e){
            AlertController.showAlert(Alert.AlertType.ERROR,"Fejl ved oprettelse af medarbejder: " + e.getMessage());
        }
    }

    // Slet Appointment
    @FXML
    private void onDeleteEmployee() {

        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            AlertController.showAlert(Alert.AlertType.WARNING, "Vælg venligst en medarbejder for at slette.");
            return;
        }

        try {
            employeeService.removeEmployeeSafely(selectedEmployee.getId());
            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Medarbejder slettet uden at påvirke eksisterende aftaler.");
            loadEmployees(); // Opdater tabellen
        } catch (IllegalArgumentException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, "Kunne ikke slette");
        }

    }


    @FXML
    private void onLogout(ActionEvent event) {
        try {
            SceneSwitch.switchScene(event, "/org/example/monikasfrisoersalon/login-view.fxml",currentUser, "Login");
            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Log ud");
        } catch (IOException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, "Fejl ved indlæsning af scene: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        setupAppointmentColumns();
        loadAppointments();
        setupEmployeeColumns();
        loadEmployees();

        if (usernameText != null) {
            usernameText.setText("Logget ind som Admin: " + currentUser.getUsername());
        }

        if (employeeTable != null) {
            employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, gammelValg, nytValg) -> {
                if (nytValg != null) {
                    employeeNameField.setText(nytValg.getUsername());
                    employeePhoneNumberField.setText(String.valueOf(nytValg.getPhoneNumber()));
                    // Af sikkerhedshensyn udfylder vi ikke password-feltet. Hvis det står tomt ved redigering, beholder vi bare det gamle password.
                }
            });
        }
    }


    private void setupAppointmentColumns() {
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

    private void setupEmployeeColumns() {
        employeeNameColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getUsername())
        );
        employeePhoneNumberColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getPhoneNumber()))
        );
    }

    private void loadAppointments() {
        List<Appointment> appointmentList = appointmentService.getAllAppointments();
        appointmentTable.getItems().setAll(appointmentList);
    }

    private void loadEmployees() {
        List<Employee> employeeList = employeeService.getEmployees();
        employeeTable.getItems().setAll(employeeList);


    }
}
