package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepo;


    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public void createCustomer(Customer customer) {
        String phoneNumber = String.valueOf(customer.getPhoneNumber());
        if (phoneNumber.length() != 8) {
            throw new IllegalArgumentException("Fejl: Kundens telefonnummer skal være præcis 8 cifre.");
        }

        if (customer.getUsername() == null || customer.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Fejl: Kunden skal have et navn.");
        }

        if (customer.getPassword() == null || !customer.getPassword().equals("")) {
            throw new IllegalArgumentException("Fejl: Password skal være blankt (\"\").");
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
