package org.example.monikasfrisoersalon.Repoistory;

import org.example.monikasfrisoersalon.Database.DB;
import org.example.monikasfrisoersalon.Model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    private DB db;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        db = new DB();
        customerRepository = new CustomerRepository(db);
    }


    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM customer WHERE username = 'Hans' OR username = 'Anonym'";

        try (java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Kunne ikke rydde op i databasen: " + e.getMessage());
        }
    }


    @Test
    void testFindAllCustomers() {
        List<Customer> result = customerRepository.findAllCustomers();

        assertNotNull(result, "Listen må ikke være null");
        assertFalse(result.isEmpty(), "Listen burde indeholde mindst em customer fra SchemaSeed");

        Customer firstCustomer = result.get(0);
        assertEquals("klaus", firstCustomer.getUsername(), "Første customer skal have brugernavnet 'klaus'");
        assertEquals("klaus123", firstCustomer.getPassword(), "Første customer skal have password 'klaus123'");
        assertEquals(34212343, firstCustomer.getPhoneNumber(), "Første customer skal have telefonnummer 34212343");

        Customer secondCustomer = result.get(1);
        assertEquals("morten", secondCustomer.getUsername(), "Anden customer skal have brugernavnet 'morten'");
        assertEquals("morten123", secondCustomer.getPassword(), "Anden customer skal have password 'morten123'");
        assertEquals(12392154, secondCustomer.getPhoneNumber(), "Anden customer skal have telefonnummer 12392154");
    }

    @Test
    void testCreateCustomer() {

        Customer newCustomer = new Customer(0, "Hans", "hans123", 99887766);
        customerRepository.createCustomer(newCustomer);

        List<Customer> allCustomers = customerRepository.findAllCustomers();
        Customer savedCustomer = null;

        for (Customer customer : allCustomers) {
            if ("Hans".equals(customer.getUsername())) {
                savedCustomer = customer;
                break;
            }
        }

        assertNotNull(savedCustomer, "Den nyoprettede kunde 'Hans' burde kunne findes i databasen");
        assertEquals("Hans", savedCustomer.getUsername(), "Brugernavnet burde være 'Hans'");
        assertEquals("hans123", savedCustomer.getPassword(), "Adgangskoden burde være 'hans123'");
        assertEquals(99887766, savedCustomer.getPhoneNumber(), "Telefonnummeret burde være 99887766");
    }


    @Test
    void testAnonymizeCustomer() {
        Customer newCustomer = new Customer(0, "Hans", "hans123", 99887766);
        customerRepository.createCustomer(newCustomer);

        List<Customer> allCustomersBefore = customerRepository.findAllCustomers();
        int hansId = -1;

        for (Customer customer : allCustomersBefore) {
            if ("Hans".equals(customer.getUsername())) {
                hansId = customer.getId();
                break;
            }
        }

        assertTrue(hansId != -1, "Dummy-kunden 'Hans' blev ikke fundet i databasen");
        customerRepository.anonymizeCustomer(hansId);

        List<Customer> allCustomersAfter = customerRepository.findAllCustomers();
        Customer anonymizedCustomer = null;

        for (Customer customer : allCustomersAfter) {
            if (customer.getId() == hansId) {
                anonymizedCustomer = customer;
                break;
            }
        }

        assertNotNull(anonymizedCustomer, "Kunden skal stadig findes i databasen (Soft Delete)");
        assertEquals("Anonym", anonymizedCustomer.getUsername(), "Brugernavnet skal være overskrevet til 'Anonym'");
        assertEquals("GDPR_SLETTET", anonymizedCustomer.getPassword(), "Adgangskoden skal være overskrevet til 'GDPR_SLETTET'");
        assertEquals(0, anonymizedCustomer.getPhoneNumber(), "Telefonnummeret skal være nulstillet til 0");
    }
}






