package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Repository.CustomerRepository;
import org.example.monikasfrisoersalon.Database.DB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private DB db;
    private CustomerRepository customerRepo;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        db = new DB();
        customerRepo = new CustomerRepository(db);
        customerService = new CustomerService(customerRepo);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM customer WHERE username = 'Test-Morten'";
        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Oprydning fejlede: " + e.getMessage());
        }
    }

    @Test
    void testCreateCustomerSuccessWithEmptyPassword() throws SQLException {
        Customer testCustomer = new Customer(0, "Test-Morten", "", 88888888);

        assertDoesNotThrow(() -> customerService.createCustomer(testCustomer));

        // 3. Hent alle kunder og find den nye kunde
        List<Customer> customers = customerRepo.findAllCustomers();
        boolean found = false;
        for (Customer c : customers) {
            if ("Test-Morten".equals(c.getUsername()) && "".equals(c.getPassword())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Kunden med det tomme password burde findes i databasen");
    }

    @Test
    void testCreateCustomerFailWithNonEmptyPassword() {
        // 1. Lav en kunde med et ulovligt password (f.eks. et mellemrum eller tekst)
        Customer invalidCustomer = new Customer(0, "Test-Morten", " ", 88888888);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(invalidCustomer);
        });

        String expectedMessage = "Fejl: Password skal være blankt (\"\").";
        assertEquals(expectedMessage, exception.getMessage());
    }
}