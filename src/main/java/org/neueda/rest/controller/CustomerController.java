package org.neueda.rest.controller;

import org.neueda.rest.entity.Customer;
import org.neueda.rest.exception.DuplicateEmailException;
import org.neueda.rest.exception.InvalidParamsException;
import org.neueda.rest.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://127.0.0.1:5500/")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerService service;

    //ResponseEntity--> Response Body, HTTP Satus Code, HTTP Headers
    @PostMapping("/")
    public ResponseEntity<Customer> save(@RequestBody Customer customer) {
        if((customer == null) ||(customer.getName()==null) || (customer.getEmail()==null)||(customer.getEmail().isBlank()||(customer.getName().isBlank()))){
            throw  new InvalidParamsException("Customer name and Email must be provided!");
        }
        // implement the logic here
        if(service.emailExists(customer.getEmail())){
            throw new DuplicateEmailException("Email already Exists: "+customer.getEmail());
        }

        Customer saveCustomer=service.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCustomer);
    }
    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers(){

        List<Customer> list=service.getAllCustomers();
        //old
        //return new ResponseEntity<>(list,HttpStatus.OK);
        //new
        return  ResponseEntity.ok(list);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id){

        Customer c=service.getCustomerById(id);
        return  ResponseEntity.ok(c);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id){
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer){
        customer.setId(id);
        Customer updatedCustomer=service.save(customer);
        return  ResponseEntity.ok(updatedCustomer);
    }
    @GetMapping("/by-email")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email){
        if(email.isBlank()){
            throw  new InvalidParamsException("Customer Email Must Be Provided!");
        }
        Customer customerEmail=service.getCustomerByEmail(email);
        return ResponseEntity.ok(customerEmail);

    }



}
