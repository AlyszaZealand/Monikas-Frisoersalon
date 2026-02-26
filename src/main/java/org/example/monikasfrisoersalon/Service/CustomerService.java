package org.example.monikasfrisoersalon.Service;

import org.example.monikasfrisoersalon.Model.Customer;
import org.example.monikasfrisoersalon.Repoistory.CustomerRepository;

import java.util.List;

public class CustomerService {
    private final CustomerRepository customerRepository;

    //
    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepository = customerRepo;
    }


    // Get Customers
    public List<Customer> getCustomers(){
        return customerRepository.findCustomers();
    }

}
