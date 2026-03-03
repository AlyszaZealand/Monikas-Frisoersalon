package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Model.User;

public class AuthenticatorService {

    private final AdministratorService adminService;
    private final EmployeeService employeeService;

    private User currentUser;

    public AuthenticatorService(AdministratorService adminService, EmployeeService employeeService) {
        this.adminService = adminService;
        this.employeeService = employeeService;
    }

    public User login(String username, String password) {
        // Check administrators first
        for (Administrator admin : adminService.getAdministrators()) {
            if (admin.getUsername().equalsIgnoreCase(username) && admin.getPassword().equals(password)) {
                this.currentUser = admin;
                return admin;
            }
        }

        // Then check employees
        for (Employee emp : employeeService.getEmployees()) {
            if (emp.getUsername().equalsIgnoreCase(username) && emp.getPassword().equals(password)) {
                this.currentUser = emp;
                return emp;
            }
        }
        throw new IllegalArgumentException("Fejl: Forkert brugernavn eller adgangskode.");
    }


    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
