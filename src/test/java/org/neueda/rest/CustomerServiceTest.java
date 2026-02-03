package org.neueda.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.neueda.rest.entity.Customer;
import org.neueda.rest.exception.CustomerNotFoundException;
import org.neueda.rest.repo.CustomerRepo;
import org.neueda.rest.service.CustomerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() {
        List<Customer> mockCustomers = Arrays.asList(
                new Customer("Alex", "alex@gmail.com"),
                new Customer("Bob", "bob@gmail.com")
        );
        Mockito.when(customerRepo.findAll()).thenReturn(mockCustomers);
        List<Customer> customers = customerService.getAllCustomers();
        Assertions.assertEquals(2, customers.size());
    }

    @Test
    void testGetCustomerById_found() {
        Customer mockCustomer = new Customer("Alex", "alex@gmail.com");
        Mockito.when(customerRepo.findById(1)).thenReturn(Optional.of(mockCustomer));
        Customer customer = customerService.getCustomerById(1);
        Assertions.assertEquals("Alex", customer.getName());
    }

    @Test
    void testGetCustomerById_notFound() {
        Mockito.when(customerRepo.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(2));
    }

    @Test
    void testCreateCustomer() {
        Customer toSave = new Customer("Krizel", "kriss@gmail.com");
        Mockito.when(customerRepo.save(toSave)).thenReturn(toSave);
        Customer saved = customerService.save(toSave);
        Assertions.assertEquals("Krizel", saved.getName());
    }

    @Test
    void testUpdateCustomer_found() {
        Customer existing = new Customer("Nikuj", "nick@gmail.com");
        Customer update = new Customer("Nick Tod", "nick@microsoft.com");
        Mockito.when(customerRepo.findById(1)).thenReturn(Optional.of(existing));
        Mockito.when(customerRepo.save(existing)).thenReturn(existing);
        Customer updated = customerService.updateCustomer(1, update);
        Assertions.assertEquals("Nick Tod", updated.getName());
        Assertions.assertEquals("nick@microsoft.com", updated.getEmail());
    }

    @Test
    void testUpdateCustomer_notFound() {
        Customer update = new Customer("User1", "user@gmail.com");
        Mockito.when(customerRepo.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(2, update));
    }

    @Test
    void testDeleteCustomer_found() {
        Mockito.when(customerRepo.existsById(1)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> customerService.deleteCustomer(1));
        Mockito.verify(customerRepo).deleteById(1);
    }

    @Test
    void testDeleteCustomer_notFound() {
        Mockito.when(customerRepo.existsById(2)).thenReturn(false);
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(2));
    }
}