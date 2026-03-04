package org.example.monikasfrisoersalon.View.Controller;

import javafx.beans.property.SimpleStringProperty;
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
import java.time.format.DateTimeFormatter;
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


    // tabel til aftaler
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> employeeColumn;
    @FXML private TableColumn<Appointment, String> customerColumn;
    @FXML private TableColumn<Appointment, String> treatmentColumn;
    @FXML private TableColumn<Appointment, String> startDateColumn;
    @FXML private TableColumn<Appointment, String> endDateColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    // input felter til opret appointment
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField; //
    @FXML private DatePicker appointmentDatePicker; //
    @FXML private TextField appointmentTimeField; //
    @FXML private ComboBox<Treatment> treatmentComboBox; //

    // LABELS OG KNAPPER
    @FXML private Label usernameText;
    @FXML private Button addAppointment;
    @FXML private Button deleteAppointment;
    @FXML private Button editAppointment;
    @FXML private Button logoutButton;



    // ------------------------------------------------------------------------------------------------------------- //

    @FXML
    public void initialize() {
        setupColumns();
        loadAppointments();

        if(usernameText != null){
            usernameText.setText("Logget ind som: " + currentUser.getUsername());
        }
        if(treatmentComboBox != null){
            treatmentComboBox.getItems().addAll(treatmentService.getActiveTreatments());
        }
    }



    // Tilføj Appointment
    @FXML
    private void onAddAppointment() { //onAction for addAppointment knappen
        try{
            // Henter data fra input felterne
            String customerName = customerNameField.getText();
            String phoneText = (customerPhoneField.getText());
            Treatment selectedTreatment = treatmentComboBox.getValue();
            LocalDate date = appointmentDatePicker.getValue();
            String time = appointmentTimeField.getText();

            // Tjekker om alle felter er udfyldt
            if (customerName == null || customerName.trim().isEmpty() ||
                    phoneText == null || phoneText.trim().isEmpty() ||
                    selectedTreatment == null || date == null || time == null || time.trim().isEmpty()) {
                AlertController.showAlert(Alert.AlertType.WARNING, "Udfyld venligst alle felter for at oprette en aftale.");
                return;
            }
            int customerPhone = Integer.parseInt(phoneText);

            // Opretter LocalDateTime objektet ved at kombinere dato og tid
            java.time.LocalTime localTime = java.time.LocalTime.parse(time);
            java.time.LocalDateTime startDateTime = java.time.LocalDateTime.of(date, localTime);

            // Beregner sluttidspunktet ved at tilføje behandlingens varighed til starttidspunktet
            java.time.LocalDateTime endDateTime = startDateTime.plusMinutes(selectedTreatment.getDuration());


            // Opretter en midlertidig kunde med de indtastede oplysninger (ID sættes til 0, da det vil blive genereret i databasen)
            Customer newCustomer = new Customer(0, customerName, "", customerPhone);
            //finder den kunde vi lige har oprettet, så vi kan få dens ID til at oprette aftalen
            customerService.createCustomer(newCustomer);
             for (Customer c : customerService.getCustomers()){
                if(c.getPhoneNumber() == customerPhone && c.getUsername().equals(customerName)){
                    newCustomer = c;
                    break;
                }
             }

            Appointment newAppointment = new Appointment(0, newCustomer, (Employee) currentUser, selectedTreatment, true, startDateTime, endDateTime);
            appointmentService.createAppointment(newAppointment);

            customerNameField.clear();
            customerPhoneField.clear();
            appointmentTimeField.clear();
            appointmentDatePicker.setValue(null);
            treatmentComboBox.setValue(null);

            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Aftale oprettet for " + customerName + " kl. " + time + " den " + date);
            loadAppointments();

        } catch (NumberFormatException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, "Telefonnummer må kun indeholde tal.");
        } catch (IllegalArgumentException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, e.getMessage());
        } catch (java.time.format.DateTimeParseException e) {
            AlertController.showAlert(Alert.AlertType.ERROR, "Skriv venligst et gyldigt tidspunktet som f.eks. '10:30'");
        } catch (Exception e) {
            AlertController.showAlert(Alert.AlertType.ERROR, "Fejl ved oprettelse af aftale: " + e.getMessage());
        }
    }


    // Slet Appointment
    @FXML
    private void onDeleteAppointment() {
        // Henter den valgte aftale fra tabellen
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();

        //Tjekker om der er valgt en aftale
        if (selected == null) {
            AlertController.showAlert(Alert.AlertType.WARNING, "Vælg venligst en aftale først");
            return;
        }

        // Viser en bekræftelsesdialog for at sikre, at brugeren vil slette aftalen
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Er du sikker på du vil aflyse aftalen for " +
                        selected.getCustomer().getUsername() + "?",
                ButtonType.YES, ButtonType.NO);

        // Håndterer brugerens svar på bekræftelsesdialogen
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                appointmentService.cancelAppointment(selected.getId());
                AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Aftalen er aflyst");
                loadAppointments(); // indlæser table
            }
        });


    }

    // Rediger Appointment
    @FXML
    private void  onEditAppointment() {
        // Henter den valgte aftale fra tabellen
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();

        //Tjekker om der er valgt en aftale
        if (selected == null) {
            AlertController.showAlert(Alert.AlertType.WARNING, "Vælg venligst en aftale først");
            return;
        }




    }

    // Log Ud
    @FXML
    private void onLogout(ActionEvent event) {
        try {
            SceneSwitch.switchScene(event, "/org/example/monikasfrisoersalon/login-view.fxml", currentUser, "Monikas Frisørsalon - Login");
            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Log ud fuldført");
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert(Alert.AlertType.ERROR, "Kunne ikke logge ud: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<Appointment> appointmentList = appointmentService.getAppointmentsForEmployee(currentUser.getId());
        appointmentTable .getItems().setAll(appointmentList);
    }

}

