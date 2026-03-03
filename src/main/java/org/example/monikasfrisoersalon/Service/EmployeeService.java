package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Repository.EmployeeRepository;

import java.util.List;

public class EmployeeService {

    private final EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public void createEmployee(Employee employee) {
        String phoneNumber = String.valueOf(employee.getPhoneNumber());
        if (phoneNumber.length() != 8) {
            throw new IllegalArgumentException("Fejl: Telefonnummeret skal være præcis 8 cifre.");
        }
        if (employee.getPassword() == null || employee.getPassword().length() < 4) {
            throw new IllegalArgumentException("Fejl: Adgangskoden skal være på mindst 4 tegn.");
        }
        employeeRepo.createEmployee(employee);
    }

    public List<Employee> getEmployees() {
        return employeeRepo.findEmployees();
    }

    public void removeEmployeeSafely(int employeeId) {
        employeeRepo.deleteEmployeeSafely(employeeId);
    }

    public void updatePassword(int employeeId, String newPassword) {
        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("Fejl: Den nye adgangskode skal være mindst 4 tegn.");
        }
        employeeRepo.updatePassword(employeeId, newPassword);
    }
}
