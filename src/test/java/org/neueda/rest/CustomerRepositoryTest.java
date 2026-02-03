package org.neueda.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neueda.rest.entity.Customer;
import org.neueda.rest.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepo repo;

    @Test
    void testFindAll(){
        List<Customer> customers=repo.findAll();
        Assertions.assertFalse(customers.isEmpty(),"Customer should not be Empty");
        Assertions.assertTrue(customers.size()>=2,"Should Have Atleast 2 or more Customers");
    }
    //write testcases for findById, save, delete
    @Autowired
    private CustomerRepo customerRepository;



    @Test
    void testFindById() {
        List<Customer> customers = customerRepository.findAll();
        Customer first = customers.get(0);

        Optional<Customer> found = customerRepository.findById(first.getId());
        Assertions.assertTrue(found.isPresent(), "Customer should be found by id");
        Assertions.assertEquals(first.getEmail(), found.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        Optional<Customer> customer = customerRepository.findByEmail("ns@gmail.com");

        Assertions.assertTrue(customer.isPresent(), "Customer should be found by email");
        Assertions.assertEquals("Nikunj Soni", customer.get().getName());
    }

    @Test
    void testSave() {
        Customer newCustomer = new Customer("Alexa", "alex@gmail.com");
        Customer saved = customerRepository.save(newCustomer);
        Assertions.assertNotNull(saved.getId(), "Saved customer should have an id");
        Assertions.assertEquals("Alexa", saved.getName());
    }

    @Test
    void testDelete() {
        List<Customer> customers = customerRepository.findAll();
        Customer toDelete = customers.get(0);

        customerRepository.deleteById(toDelete.getId());

        Optional<Customer> found = customerRepository.findById(toDelete.getId());
        Assertions.assertFalse(found.isPresent(), "Customer should be deleted");
    }
}
