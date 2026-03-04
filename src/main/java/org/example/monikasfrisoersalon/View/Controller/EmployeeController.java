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
    @FXML private TableColumn<Appointment, String> employeeColumn;
    @FXML private TableColumn<Appointment, String> customerColumn;
    @FXML private TableColumn<Appointment, String> treatmentColumn;
    @FXML private TableColumn<Appointment, String> startDateColumn;
    @FXML private TableColumn<Appointment, String> endDateColumn;
    @FXML private TableColumn<Appointment, String> statusColmn;

    @FXML private TableView<Customer> customerTable; // Tabel til kunder
    @FXML private TableColumn<Appointment, String> customerNameColumn; // Navn på kunde
    @FXML private TableColumn<Appointment, String> customerTreatmentColumn; // Behandling
    @FXML private TableColumn<Appointment, String> customerPhoneNumberColumn; // Kunde telefonnummer

    // Labels
    @FXML private Label usernameText;

    // Knapper
    @FXML private Button addAppointment;
    @FXML private Button deleteAppointment;
    @FXML private Button editAppointment;
    @FXML private Button logoutButton;


    // input felter til opret appointment
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField; //
    @FXML private DatePicker appointmentDatePicker; //
    @FXML private TextField appointmentTimeField; //
    @FXML private ComboBox<Treatment> treatmentComboBox; //



    // ------------------------------------------------------------------------------------------------------------- //

    // Tilføj Appointment
    @FXML
    private void onAddAppointment() { //onAction for addAppointment knappen
        try{
            String customerName = customerNameField.getText();
            int customerPhone = Integer.parseInt(customerPhoneField.getText());
            Treatment selectedTreatment = treatmentComboBox.getValue();
            LocalDate date = appointmentDatePicker.getValue();
            String time = appointmentTimeField.getText();

            if(selectedTreatment == null || date == null || time.isEmpty()) {
            AlertController.showAlert(Alert.AlertType.WARNING, "Udfyld venligst alle felter for at oprette en aftale.");
                return;
            }

            Customer newCustomer = new Customer(0, customerName, "", customerPhone);
            //finder den kunde vi lige har oprettet, så vi kan få dens ID til at oprette aftalen
            customerService.createCustomer(newCustomer);
             for (Customer c : customerService.getCustomers()){
                if(c.getPhoneNumber() == customerPhone && c.getUsername().equals(customerName)){
                    newCustomer = c;
                    break;
                }
             }

             // Opretter LocalDateTime objektet ved at kombinere dato og tid
             java.time.LocalTime localTime = java.time.LocalTime.parse(time);
             java.time.LocalDateTime startDateTime = java.time.LocalDateTime.of(date, localTime);

             // Beregner sluttidspunktet ved at tilføje behandlingens varighed til starttidspunktet
            java.time.LocalDateTime endDateTime = startDateTime.plusMinutes(selectedTreatment.getDuration());

            Appointment newAppointment = new Appointment(0, newCustomer, (Employee) currentUser, selectedTreatment, true, startDateTime, endDateTime);
            appointmentService.createAppointment(newAppointment);
            AlertController.showAlert(Alert.AlertType.CONFIRMATION, "Aftale oprettet for " + customerName + " kl. " + time + " den " + date);

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


    }

    // Rediger Appointment
    @FXML
    private void  onEditAppointment() {
    }

    // Calendar
//    private void CalendarTable() {
//        customerNameColumn.cellFactoryProperty(new PropertyValueFactory<>("Navn")); // Navn column
//        customerTreatmentColumn.cellFactoryProperty(new PropertyValueFactory<>("Behandling")); // Behandling column
//        customerPhoneNumberColumn.cellFactoryProperty(new PropertyValueFactory<>("Telefonnummer")); // Telefonnummer column
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

}

