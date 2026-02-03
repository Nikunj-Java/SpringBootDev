package org.neueda.rest.service;

import org.neueda.rest.entity.Customer;
import org.neueda.rest.exception.CustomerNotFoundException;
import org.neueda.rest.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepo repo;

    public Customer save(Customer customer) {

        return repo.save(customer);
    }
    public List<Customer> getAllCustomers(){
        return  repo.findAll();
    }
    public Customer getCustomerById(Integer id){
        return  repo.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer Not Found"));
    }
    public void deleteCustomer(Integer id){
        if (!repo.existsById(id)) {
            throw new CustomerNotFoundException("Customer with id " + id + " not found");
        }
        repo.deleteById(id);
    }
    //update by id--> we will use Save Method only
    public Customer updateCustomer(Integer id, Customer updated) {
        return repo.findById(id)
                .map(existingItem -> {
                    existingItem.setName(updated.getName());
                    existingItem.setEmail(updated.getEmail());
                    return repo.save(existingItem);
                })
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
    }
    //custome method
    public Customer getCustomerByEmail(String email){
       return repo.findByEmail(email).orElse(null);
    }
     //Email Exists
    public boolean emailExists(String email){
        return  repo.existsByEmail(email);
    }

}
