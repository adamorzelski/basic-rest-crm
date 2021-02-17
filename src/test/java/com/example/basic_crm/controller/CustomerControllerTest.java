package com.example.basic_crm.controller;

import com.example.basic_crm.model.Customer;
import com.example.basic_crm.service.domain.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;


    @Test
    void getAllCustomers_returnsCustomerList_andStatusOK() throws Exception {

        //given
        Mockito.when(customerService.getAllCustomers()).thenReturn(getMockCustomers());

        //when + then
        String url = "/api/customers";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Doe")))
                .andExpect(jsonPath("$[0].emailAddress", is("0test.email@mail.com")))
                .andExpect(jsonPath("$[1].emailAddress", is("1test.email@mail.com")));

    }

    @Test
    void getCustomerById_returnsCustomer_andStatusOK() throws Exception {

        //given
        Customer customer = new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        customer.setId(1);
        Mockito.when(customerService.getCustomerById(Mockito.anyInt())).thenReturn(Optional.of(customer));

        //when + then
        String url = "/api/customers/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andExpect(content().string(containsString("Ann")))
                                    .andReturn();

        Customer resultCustomer = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
        assertEquals(customer, resultCustomer);

    }

    @Test
    void getCustomerById_returnsStatusNotFound() throws Exception {

        //given
        Mockito.when(customerService.getCustomerById(Mockito.anyInt())).thenReturn(Optional.empty());

        //when + then
        String url = "/api/customers/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void saveNewCustomer_returnsStatusCreated() throws Exception {

        //given
        Customer newCustomer = new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        Customer savedCustomer = new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        savedCustomer.setId(1);
        Mockito.when(customerService.saveCustomer(Mockito.any(Customer.class))).thenReturn(savedCustomer);

        //when + then
        String url = "/api/customers";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                                                .post(url)
                                                .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newCustomer))
                                                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                                                .andDo(print())
                                                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost/api/customers/1",
                response.getHeader(HttpHeaders.LOCATION));
    }

    @Test
    void saveNewCustomer_returnsStatusNoContent() throws Exception {

        //given
        Customer newCustomer = new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        Mockito.when(customerService.saveCustomer(Mockito.any(Customer.class))).thenReturn(null);

        //when + then
        String url = "/api/customers";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                                                        .post(url)
                                                        .accept(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(newCustomer))
                                                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                                .perform(requestBuilder)
                                .andDo(print())
                                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void updateCustomer_returnsStatusNotFound() throws Exception {

        //given
        Mockito.when(customerService.existsById(Mockito.anyInt())).thenReturn(false);

        //when + then
        String url = "/api/customers/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Customer()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer_updatesCustomer_and_returnsStatusNoContent() throws Exception {

        //given
        Customer oldCustomer = new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        Customer newCustomer = new Customer("NewName", "NewLastName", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        Mockito.when(customerService.getCustomerById(Mockito.anyInt())).thenReturn(Optional.of(oldCustomer));
        Mockito.when(customerService.existsById(Mockito.anyInt())).thenReturn(true);

        //when + then
        String url = "/api/customers/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(newCustomer, oldCustomer);
    }

    @Test
    void deleteCustomer_returnsStatusNotFound() throws Exception {

        //given
        Mockito.when(customerService.existsById(Mockito.anyInt())).thenReturn(false);

        //when + then
        String url = "/api/customers/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomer_returnsStatusOk() throws Exception {

        //given
        Customer customer = new Customer("Ann", "Annie", "ann.annie@mail.com", "Main 3", "Lublin", "323456789");
        Mockito.when(customerService.existsById(Mockito.anyInt())).thenReturn(true);

        //when + then
        String url = "/api/customers/1";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }



    private List<Customer> getMockCustomers() {
        List<Customer> mockCustomers = Arrays.asList(
                new Customer("John", "Doe", "0test.email@mail.com", "Main 1", "Lublin", "123456789"),
                new Customer("Adam", "Smith", "1test.email@mail.com", "Main 2", "Warszawa", "223456789")
        );

        mockCustomers.get(0).setId(1);
        mockCustomers.get(1).setId(2);

        return mockCustomers;
    }
}
