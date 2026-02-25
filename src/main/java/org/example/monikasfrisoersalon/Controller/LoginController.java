package org.example.monikasfrisoersalon.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Repoistory.*;
import org.example.monikasfrisoersalon.Service.*;

import java.util.List;

public class LoginController {

    // Possible login scene
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;

    // Possible appointment booking scene
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField employeeField;
    @FXML private TextField dateField;
    @FXML private TextField timeField;
    @FXML private Button registerButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button logoutButton;

    // Possible login
    @FXML public void onLoginButtonClick(ActionEvent event) {
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();

        // Possible Switch Case
        try { // Login Employee
            List<Employee> employees = employeeService.getEmployees();
            if (employees.isEmpty()) {
                Employee employee = employees.get(0);
                SceneSwitch.switchToAppointment();
                showAlert("Success", "Velkommen Kunde");

            } else if { // Login Customer
                List<Customer> customers = customerService.getCustomers();
                if (customers.isEmpty()) {
                    Customer customer = customers.get(0);
                    SceneSwitch.switchToAppointment();
                    showAlert("Success", "Velkommen Medarbejder");
                }

            } else if { // Login Administrator
                List<Administrator> administrators = administratorService.getAdministrators();
                if (administrators.isEmpty()) {
                    Administrator administrator = administrators.get(0);
                    SceneSwich.switchToAppointment();
                    showAlert("Success", "Administrator");
                }

            } else {
                showAlert("Login Failed", "No User Found");
            }
        } catch (Exception e) {
            showAlert("Login Failed", "An Error Occurred During Login:");
        }
    }

    // For initialize
    private AdministratorService administratorService;
    private AppointmentService appointmentService;
    private CustomerService customerService;
    private EmployeeService employeeService;
    private TreatmentService treatmentService;

    // Initialize
    public void initialize() {
        DB db = new DB();
        AdministratorRepository administratorRepo = new AdministratorRepository(db);
        AppointmentRepository appointmentRepo = new AppointmentRepository(db);
        CustomerRepository customerRepo = new CustomerRepository(db);
        EmployeeRepository employeeRepo = new EmployeeRepository(db);
        TreatmentRepository treatmentRepo = new TreatmentRepository(db);
        administratorService = new AdministratorService(administratorRepo);
        appointmentService = new AppointmentService(appointmentRepo);
        customerService = new CustomerService(customerRepo);
        employeeService = new EmployeeService(employeeRepo);
        treatmentService = new TreatmentService(treatmentRepo);
    }

    // Alerts (Confirmation, Errors etc.)
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
