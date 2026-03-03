package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Employee;
import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private DB db;
    private EmployeeRepository employeeRepo;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        db = new DB();
        employeeRepo = new EmployeeRepository(db);
        employeeService = new EmployeeService(employeeRepo);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM employee WHERE username = 'Jens'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kunne ikke rydde op i databasen: " + e.getMessage());
        }
    }


    @Test
    void testCreateEmployeeThroughService() throws SQLException {
        Employee newEmployee = new Employee(0, "Jens", "jens123", 11223344);
        employeeService.createEmployee(newEmployee);

        // 3. Hent alle medarbejdere og find den nye medarbejder
        List<Employee> allEmployees = employeeRepo.findEmployees();
        Employee savedEmployee = null;
        for (Employee emp : allEmployees) {
            if ("Jens".equals(emp.getUsername())) {
                savedEmployee = emp;
                break;
            }
        }

        assertNotNull(savedEmployee, "Jens burde findes i databasen nu");
        assertEquals("jens123", savedEmployee.getPassword(), "Kodeordet skal matche det oprettede");
    }


    @Test
    void testUpdatePasswordThroughService() throws SQLException {
        Employee testEmployee = new Employee(0, "Jens", "jens123", 11223344);
        employeeRepo.createEmployee(testEmployee);

        // 2. Find Jens' ID i databasen
        int jensId = -1;
        for (Employee emp : employeeRepo.findEmployees()) {
            if ("Jens".equals(emp.getUsername())) {
                jensId = emp.getId();
                break;
            }
        }

        String newPassword = "123jens";
        employeeService.updatePassword(jensId, newPassword);

        // 3. Hent Jens igen og tjek at kodeordet er opdateret
        Employee updatedJens = null;
        for (Employee emp : employeeRepo.findEmployees()) {
            if (emp.getId() == jensId) {
                updatedJens = emp;
                break;
            }
        }

        assertNotNull(updatedJens);
        assertEquals(newPassword, updatedJens.getPassword(), "Kodeordet burde være opdateret til det nye");
    }


    @Test
    void testUpdatePasswordFailTooShort() {
        try {
            employeeService.updatePassword(1, "123");
            fail("Servicen burde have kastet en fejl over et for kort kodeord");
        } catch (IllegalArgumentException e) {
            assertEquals("Fejl: Den nye adgangskode skal være mindst 4 tegn.", e.getMessage());
        }
    }
}