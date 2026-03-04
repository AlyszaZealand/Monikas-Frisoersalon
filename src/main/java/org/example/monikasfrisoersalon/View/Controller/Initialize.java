package org.example.monikasfrisoersalon.View.Controller;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Repository.*;
import org.example.monikasfrisoersalon.Service.*;

public class Initialize {

    // Service klasserne
    private final AdministratorService administratorService;
    private final AppointmentService appointmentService;
    private final AuthenticatorService authenticatorService;
    private final CalendarService calendarService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final TreatmentService treatmentService;

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

        // AuthenticatorService har brug for både administratorService og employeeService, så de skal bygges først
        this.authenticatorService = new AuthenticatorService(administratorService, employeeService);
        this.calendarService = new CalendarService(appointmentService);

    }
    // Getters til at hente services
    public AuthenticatorService getAuthenticatorService() { return authenticatorService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public EmployeeService getEmployeeService() { return employeeService; }
    public TreatmentService getTreatmentService() { return treatmentService; }
    public CalendarService getCalendarService() { return calendarService; }
    public CustomerService getCustomerService() { return customerService; }
    public AdministratorService getAdministratorService() { return administratorService; }

}
