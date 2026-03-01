package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Repoistory.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepo;


    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public void createCustomer(Customer customer) {
        String phoneStr = String.valueOf(customer.getPhoneNumber());
        if (phoneStr.length() != 8) {
            throw new IllegalArgumentException("Fejl: Kundens telefonnummer skal være præcis 8 cifre.");
        }

        if (customer.getUsername() == null || customer.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Fejl: Kunden skal have et navn.");
        }

        if (customer.getPassword() == null || customer.getPassword().length() < 4) {
            throw new IllegalArgumentException("Fejl: Adgangskoden skal være på mindst 4 tegn.");
        }

        customerRepo.createCustomer(customer);
    }

    public List<Customer> getCustomers() {
        return customerRepo.findAllCustomers();
    }

    public void removeCustomerGDPR(int customerId) {
        customerRepo.anonymizeCustomer(customerId);
    }
}