package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Model.User;
import org.example.monikasfrisoersalon.Repository.AdministratorRepository;
import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticatorServiceTest {

    private DB db;
    private EmployeeRepository employeeRepo;
    private AdministratorRepository adminRepo;

    // Vi mangler disse to for at kunne bygge AuthenticatorService
    private EmployeeService employeeService;
    private AdministratorService adminService;

    private AuthenticatorService authenticatorService;

    @BeforeEach
    void setUp() {
        db = new DB();

        employeeRepo = new EmployeeRepository(db);
        adminRepo = new AdministratorRepository(db);

        employeeService = new EmployeeService(employeeRepo);
        adminService = new AdministratorService(adminRepo);
        authenticatorService = new AuthenticatorService(adminService, employeeService);
    }


    @Test
    void testAdminLoginSuccess() {
        String username = "monika";
        String password = "monika123";

        User loginResult = authenticatorService.login(username, password);

        assertNotNull(loginResult, "Monika burde kunne logge ind og vi skal få en User tilbage");
        assertInstanceOf(Administrator.class, loginResult, "Den bruger der logger ind burde være af typen Administrator");
        assertEquals("monika", loginResult.getUsername(), "Brugernavnet på objektet skal være monika");
    }


    @Test
    void testEmployeeLoginSuccess() {
        String username = "brian";
        String password = "brian123";

        User loginResult = authenticatorService.login(username, password);

        assertNotNull(loginResult, "Brian burde kunne logge ind");
        assertInstanceOf(Employee.class, loginResult, "Den bruger der logger ind burde være af typen Employee");
    }


    @Test
    void testEmployeeLoginFailure() {
        String username = "mette";
        String password = "forkertKode123";

        assertThrows(IllegalArgumentException.class, () -> {
            authenticatorService.login(username, password);
        }, "Når koden er forkert, SKAL systemet kaste en IllegalArgumentException");
    }
}