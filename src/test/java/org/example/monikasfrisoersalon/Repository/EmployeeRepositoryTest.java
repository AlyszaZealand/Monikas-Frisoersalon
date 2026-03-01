package org.example.monikasfrisoersalon.Repository;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest {

    private DB db;
    private EmployeeRepository employeeRepo;

    @BeforeEach
    void setUp() {
        db = new DB();
        employeeRepo = new EmployeeRepository(db);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM employee WHERE username = 'Jens'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Kunne ikke rydde op i databasen: " + e.getMessage());
        }
    }


    @Test
    void testFindEmployees() {
        List<Employee> result = employeeRepo.findEmployees();

        assertNotNull(result, "Listen over medarbejdere må ikke være null");
        assertFalse(result.isEmpty(), "Listen over medarbejdere må ikke være tom");

        Employee firstEmployee = result.get(0);
        assertEquals("brian", firstEmployee.getUsername(), "Første medarbejder skal have brugernavnet 'brian'");
        assertEquals("brian123", firstEmployee.getPassword(), "Første medarbejder skal have adgangskoden 'brian123'");
        assertEquals(98652387, firstEmployee.getPhoneNumber(), "Første medarbejder skal have telefonnummer 98652387");

        Employee secondEmployee = result.get(1);
        assertEquals("mette", secondEmployee.getUsername(), "second medarbejder skal have brugernavnet 'mette'");
        assertEquals("mette123", secondEmployee.getPassword(), "second medarbejder skal have adgangskoden 'mette123'");
        assertEquals(67416788, secondEmployee.getPhoneNumber(), "second medarbejder skal have telefonnummer 67416788");
    }


    @Test
    void testCreateEmployee() {
        Employee newEmployee = new Employee(0, "Jens", "jens123", 11223344);
        employeeRepo.createEmployee(newEmployee);


        List<Employee> allEmployees = employeeRepo.findEmployees();
        Employee savedEmployee = null;

        for (Employee emp : allEmployees) {
            if ("Jens".equals(emp.getUsername())) {
                savedEmployee = emp;
                break;
            }
        }

        assertNotNull(savedEmployee, "Den nyoprettede medarbejder 'Jens' burde kunne findes i databasen");
        assertEquals("Jens", savedEmployee.getUsername(), "Brugernavnet burde være 'Jens'");
        assertEquals("jens123", savedEmployee.getPassword(), "Adgangskoden burde være 'jens123'");
        assertEquals(11223344, savedEmployee.getPhoneNumber(), "Telefonnummeret burde være 11223344");
    }


    @Test
    void testUpdatePassword() {
        Employee testEmployee = new Employee(0, "Jens", "gammeltKodeord", 11223344);
        employeeRepo.createEmployee(testEmployee);

        List<Employee> employeesBefore = employeeRepo.findEmployees();
        int jensId = -1;

        for (Employee emp : employeesBefore) {
            if ("Jens".equals(emp.getUsername())) {
                jensId = emp.getId();
                break;
            }
        }

        assertTrue(jensId != -1, "Kunne ikke finde den oprettede medarbejder 'Jens' i databasen");
        String newPassword = "nytKodeord123";
        employeeRepo.updatePassword(jensId, newPassword);

        List<Employee> employeesAfter = employeeRepo.findEmployees();
        Employee updatedJens = null;

        for (Employee emp : employeesAfter) {
            if (emp.getId() == jensId) {
                updatedJens = emp;
                break;
            }
        }

        assertNotNull(updatedJens, "Jens burde stadig findes i databasen efter opdateringen");
        assertEquals(newPassword, updatedJens.getPassword(), "Kodeordet blev ikke opdateret korrekt i databasen");
    }


    @Test
    void testDeleteEmployeeSafely() throws Exception {
        Employee testEmployee = new Employee(0, "Jens", "jens123", 11223344);
        employeeRepo.createEmployee(testEmployee); // Opret en testmedarbejder, som vi senere vil slette

        // Find Jens' ID
        int jensId = -1;
        for (Employee emp : employeeRepo.findEmployees()) {
            if ("Jens".equals(emp.getUsername())) {
                jensId = emp.getId();
                break;
            }
        }
        assertTrue(jensId != -1, "Jens blev ikke oprettet korrekt");

        // Find en aftale uden tildelt medarbejder
        int unassignedAppId = -1;
        String findAppSql = "SELECT id FROM appointment WHERE employeeid IS NULL LIMIT 1"; // Vi antager, at der er mindst én aftale uden tildelt medarbejder i testdatabasen
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(findAppSql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                unassignedAppId = rs.getInt("id");
            }
        }
        assertTrue(unassignedAppId != -1, "Kunne ikke finde en ufordelt aftale at bruge til testen");

        // Tildel Jens til den ufordelte aftale
        String assignSql = "UPDATE appointment SET employeeid = ? WHERE id = ?"; // Tildel Jens til den ufordelte aftale for at teste, om deleteEmployeeSafely fjerner denne tilknytning
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(assignSql)) {
            ps.setInt(1, jensId);
            ps.setInt(2, unassignedAppId);
            ps.executeUpdate();
        }

        // Slet Jens sikkert og tjek om han er slettet
        employeeRepo.deleteEmployeeSafely(jensId);
        boolean jensExists = false;
        for (Employee emp : employeeRepo.findEmployees()) {
            if (emp.getId() == jensId) {
                jensExists = true;
                break;
            }
        }
        assertFalse(jensExists, "Jens burde være fuldstændig slettet fra databasen (Hard delete)");

        // Tjek at aftalen Jens var tildelt nu har employeeid sat til NULL
        String checkAppSql = "SELECT employeeid FROM appointment WHERE id = ?";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(checkAppSql)) {
            ps.setInt(1, unassignedAppId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Object empIdInDb = rs.getObject("employeeid");
                assertNull(empIdInDb, "Aftalens employeeid burde være sat tilbage til NULL af vores metode!");
            }
        }
    }
}
