package com.vrrom.dtos;

import com.vrrom.customer.CustomerDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNameValid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setName("Alice");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "name");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testNameInvalidContainsNumbers() {
        CustomerDTO customer = new CustomerDTO();
        customer.setName("Alice1");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "name");
        assertEquals(1, violations.size());
    }

    @Test
    public void testNameInvalidTooLong() {
        CustomerDTO customer = new CustomerDTO();
        customer.setName("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "name");
        assertEquals(1, violations.size());
    }

    @Test
    public void testSurnameValid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setSurname("Smith");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "surname");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testSurnameInvalidContainsNumbers() {
        CustomerDTO customer = new CustomerDTO();
        customer.setSurname("Smith1");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "surname");
        assertEquals(1, violations.size());
    }

    @Test
    public void testSurnameInvalidTooLong() {
        CustomerDTO customer = new CustomerDTO();
        customer.setSurname("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "surname");
        assertEquals(1, violations.size());
    }

    @Test
    public void testEmailValid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setEmail("alice@example.com");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "email");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmailInvalid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setEmail("alice#example.com");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "email");
        assertEquals(1, violations.size());
    }

    @Test
    public void testBirthDateValid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setBirthDate(new Date(100, 1, 1));  // Deprecated constructor for example purposes
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "birthDate");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testBirthDateInvalidFutureDate() {
        CustomerDTO customer = new CustomerDTO();
        customer.setBirthDate(new Date(System.currentTimeMillis() + 100000));  // Future date
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "birthDate");
        assertEquals(1, violations.size());
    }

    @Test
    public void testPhoneValid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setPhone("+370123456789");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "phone");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testPhoneInvalid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setPhone("123456789");  // Invalid format
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "phone");
        assertEquals(1, violations.size());
    }

    @Test
    public void testAddressValid() {
        CustomerDTO customer = new CustomerDTO();
        customer.setAddress("123 Main St");
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "address");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testAddressInvalidBlank() {
        CustomerDTO customer = new CustomerDTO();
        customer.setAddress("");  // Invalid blank address
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validateProperty(customer, "address");
        assertEquals(1, violations.size());
    }
}
