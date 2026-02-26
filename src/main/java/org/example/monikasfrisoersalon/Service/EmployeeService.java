package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Administrator;
import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Repoistory.EmployeeRepository;

import java.util.List;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    //
    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepository = employeeRepo;
    }

    // Create employee
    public void handleCreateEmployee(int employeeID) {
        employeeRepository.createEmployee(employeeID);
    }

    // Delete employee
    public void handleDeleteEmployee(int employeeID) {
        employeeRepository.deleteEmployee(employeeID);
    }

    // Get Employees
    public List<Employee> getEmployees(){
        return employeeRepository.FindEmployees();
    }

}
