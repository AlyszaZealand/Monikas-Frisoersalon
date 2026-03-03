package org.example.monikasfrisoersalon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Repository.*;
import org.example.monikasfrisoersalon.Service.*;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-org.example.monikasfrisoersalon.view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    // Put Initialize her ind.
    // Initialize kalder på alt til alle Controller. (hopefully)

    public class initialize {

        // Service klasserne
        private AdministratorService administratorService;
        private AppointmentService appointmentService;
        private AuthenticatorService authenticatorService;
        private CalendarService calendarService;
        private CustomerService customerService;
        private EmployeeService employeeService;
        private TreatmentService treatmentService;


        // Initialize metode
        public void initialize() {
            DB db = new DB();

            AdministratorRepository administratorRepository = new AdministratorRepository(db);
            AppointmentRepository appointmentRepository = new AppointmentRepository(db);
            // Authenticator
            // Calendar
            CustomerRepository customerRepository = new CustomerRepository(db);
            EmployeeRepository employeeRepository = new EmployeeRepository(db);
            TreatmentRepository treatmentRepository = new TreatmentRepository(db);

            administratorService = new AdministratorService(administratorRepository);
            appointmentService = new AppointmentService(appointmentRepository);
            // Authenticator
            // Calendar
            customerService = new CustomerService(customerRepository);
            employeeService = new EmployeeService(employeeRepository);
            treatmentService = new TreatmentService(treatmentRepository);

        }
    }

}
