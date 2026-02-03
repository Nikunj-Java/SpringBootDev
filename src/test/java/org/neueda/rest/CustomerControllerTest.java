// java
package org.neueda.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neueda.rest.entity.Customer;
import org.neueda.rest.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // don't rely on an autowired ObjectMapper bean in tests
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CustomerRepo customerRepo;

    @BeforeEach
    void clean() {
        customerRepo.deleteAll();
    }

    @Test
    void testGetAllItems() throws Exception {
        Customer saved = customerRepo.save(new Customer("Alice", uniqueEmail()));
        mockMvc.perform(get("/api/v1/customers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(saved.getName()));
    }

    @Test
    void testGetItemById() throws Exception {
        Customer customer = customerRepo.save(new Customer("Devid", uniqueEmail()));
        mockMvc.perform(get("/api/v1/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Devid"));
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer item = new Customer("Alexa", uniqueEmail());
        String json = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alexa"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerRepo.save(new Customer("Alexa", uniqueEmail()));
        Customer update = new Customer("Alexander", customer.getEmail());
        String json = objectMapper.writeValueAsString(update);
        mockMvc.perform(put("/api/v1/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alexander"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerRepo.save(new Customer("TestUser", uniqueEmail()));
        mockMvc.perform(delete("/api/v1/customers/" + customer.getId()))
                .andExpect(status().isNoContent());
        assert customerRepo.findById(customer.getId()).isEmpty();
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/customers/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not Found")));
    }

    @Test
    void testCreateCustomer_InvalidParams() throws Exception {
        Customer item = new Customer(null, null);
        String json = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("provided")));
    }

    private String uniqueEmail() {
        return "test+" + UUID.randomUUID() + "@example.com";
    }
}