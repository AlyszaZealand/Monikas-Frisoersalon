package org.example.monikasfrisoersalon.View;

import javafx.application.Application;
import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.MainApplication;
import org.example.monikasfrisoersalon.Repository.*;
import org.example.monikasfrisoersalon.Service.*;

import javax.imageio.spi.ServiceRegistry;

import static javafx.application.Application.launch;


public class Initialize {

    // Service klasserne
    private AdministratorService administratorService;
    private AppointmentService appointmentService;
    private AuthenticatorService authenticatorService;
    private CalendarService calendarService;
    private CustomerService customerService;
    private EmployeeService employeeService;
    private TreatmentService treatmentService;


    // Initialize metode
    public Initialize() {
        DB db = new DB();

        AdministratorRepository administratorRepository = new AdministratorRepository(db);
        AppointmentRepository appointmentRepository = new AppointmentRepository(db);
        CustomerRepository customerRepository = new CustomerRepository(db);
        EmployeeRepository employeeRepository = new EmployeeRepository(db);
        TreatmentRepository treatmentRepository = new TreatmentRepository(db);

        this.administratorService = new AdministratorService(administratorRepository);
        this.appointmentService = new AppointmentService(appointmentRepository);
        this.customerService = new CustomerService(customerRepository);
        this.employeeService = new EmployeeService(employeeRepository);
        this.treatmentService = new TreatmentService(treatmentRepository);
        this.authenticatorService = new AuthenticatorService(administratorService, employeeService);
        this.calendarService = new CalendarService(appointmentService);

    }

    public AuthenticatorService getAuthenticatorService() { return authenticatorService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public EmployeeService getEmployeeService() { return employeeService; }
    public TreatmentService getTreatmentService() { return treatmentService; }
    public CalendarService getCalendarService() { return calendarService; }
    public CustomerService getCustomerService() { return customerService; }
    public AdministratorService getAdministratorService() { return administratorService; }


/*    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    } */
}



//package org.example.monikasfrisoersalon.Service;
//
//import org.example.monikasfrisoersalon.Database.DB;
//import org.example.monikasfrisoersalon.Repository.*;
//
//public class ServiceRegistry {
//
//    // Den eneste instans af denne klasse
//    private static ServiceRegistry instance;
//
//    // Alle jeres services
//    private AdministratorService administratorService;
//    private AppointmentService appointmentService;
//    private AuthenticatorService authenticatorService;
//    private CalendarService calendarService;
//    private CustomerService customerService;
//    private EmployeeService employeeService;
//    private TreatmentService treatmentService;
//
//    // Privat constructor - bygger alt én gang, når programmet starter!
//    private ServiceRegistry() {
//        DB db = new DB();
//
//        // 1. Byg Repositories
//        AdministratorRepository adminRepo = new AdministratorRepository(db);
//        AppointmentRepository appointmentRepo = new AppointmentRepository(db);
//        CustomerRepository customerRepo = new CustomerRepository(db);
//        EmployeeRepository employeeRepo = new EmployeeRepository(db);
//        TreatmentRepository treatmentRepo = new TreatmentRepository(db);
//
//        // 2. Byg Services
//        this.administratorService = new AdministratorService(adminRepo);
//        this.appointmentService = new AppointmentService(appointmentRepo);
//        this.customerService = new CustomerService(customerRepo);
//        this.employeeService = new EmployeeService(employeeRepo);
//        this.treatmentService = new TreatmentService(treatmentRepo);
//
//        this.authenticatorService = new AuthenticatorService(administratorService, employeeService);
//        this.calendarService = new CalendarService(appointmentService);
//    }
//
//    // Metode til at hente instansen (Singleton)
//    public static ServiceRegistry getInstance() {
//        if (instance == null) {
//            instance = new ServiceRegistry();
//        }
//        return instance;
//    }
//
//    // --- GETTERS til at hente services ---
//    public AuthenticatorService getAuthenticatorService() { return authenticatorService; }
//    public AppointmentService getAppointmentService() { return appointmentService; }
//    public EmployeeService getEmployeeService() { return employeeService; }
//    public TreatmentService getTreatmentService() { return treatmentService; }
//    public CalendarService getCalendarService() { return calendarService; }
//    public CustomerService getCustomerService() { return customerService; }
//    public AdministratorService getAdministratorService() { return administratorService; }
//}


//package com.example.streamingplatformfeedback;
//
//import com.example.streamingplatformfeedback.infrastructure.DbConfig;
//import com.example.streamingplatformfeedback.repository.FavoriteRepository;
//import com.example.streamingplatformfeedback.repository.MovieRepository;
//import com.example.streamingplatformfeedback.repository.UserRepository;
//import com.example.streamingplatformfeedback.service.FavoriteService;
//import com.example.streamingplatformfeedback.service.MovieService;
//import com.example.streamingplatformfeedback.service.UserService;
//import com.example.streamingplatformfeedback.ui.LoginController;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class MainApplication extends Application {
//
//    @Override
//    public void start(Stage stage) throws IOException {
//
//        DbConfig dbConfig = new DbConfig();
//
//        UserRepository userRepo = new UserRepository(dbConfig);
//        MovieRepository movieRepo = new MovieRepository(dbConfig);
//        FavoriteRepository favRepo = new FavoriteRepository(dbConfig);
//
//        UserService userService = new UserService(userRepo);
//        MovieService movieService = new MovieService(movieRepo);
//        FavoriteService favService = new FavoriteService(favRepo);
//
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//
//        LoginController controller = fxmlLoader.getController();
//        controller.setServices(userService, movieService, favService);
//
//        stage.setTitle("StreamingPlatform!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }