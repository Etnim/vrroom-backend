package com.vrrom.dtos;

import com.vrrom.application.model.ApplicationDTO;
import com.vrrom.customer.CustomerDTO;
import com.vrrom.financialInfo.model.FinancialInfoDTO;
import com.vrrom.financialInfo.model.MaritalStatus;
import com.vrrom.vehicle.model.FuelType;
import com.vrrom.vehicle.model.VehicleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testPriceValid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setPrice(new BigDecimal("100.00"));
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "price");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testPriceInvalid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setPrice(BigDecimal.ZERO);  // Invalid as it must be at least 0.01
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "price");
        assertEquals(1, violations.size());
    }

    @Test
    public void testDownPaymentValid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setDownPayment(0);  // Valid as it cannot be negative
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "downPayment");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testDownPaymentInvalid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setDownPayment(-1);  // Invalid negative value
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "downPayment");
        assertEquals(1, violations.size());
    }

    @Test
    public void testResidualValueValid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setResidualValue(0);  // Valid as it cannot be negative
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "residualValue");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testYearPeriodValid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setYearPeriod(1);  // Minimum valid value
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "yearPeriod");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testYearPeriodInvalid() {
        ApplicationDTO application = new ApplicationDTO();
        application.setYearPeriod(0);  // Invalid as it must be at least 1
        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validateProperty(application, "yearPeriod");
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidNestedObjects() {
        ApplicationDTO application = new ApplicationDTO();

        CustomerDTO customer = new CustomerDTO();
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setEmail("john.doe@gmail.com");
        customer.setBirthDate(new Date(90, Calendar.FEBRUARY, 1));
        customer.setPhone("+370123456789");
        customer.setAddress("123 Main St");
        application.setCustomer(customer);

        FinancialInfoDTO financialInfo = new FinancialInfoDTO();
        financialInfo.setMonthlyIncome(new BigDecimal("5000"));
        financialInfo.setMonthlyObligations(new BigDecimal("1500"));
        financialInfo.setMaritalStatus(MaritalStatus.SINGLE);
        financialInfo.setDependants(0);
        application.setFinancialInfo(financialInfo);

        List<VehicleDTO> vehicles = new ArrayList<>();
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2020);
        vehicle.setFuel(FuelType.HYBRID);
        vehicle.setEmission(10);
        vehicles.add(vehicle);

        application.setVehicleDetails(vehicles);

        application.setPrice(new BigDecimal(200));
        application.setDownPayment(30);
        application.setResidualValue(30);
        application.setYearPeriod(2);

        Set<ConstraintViolation<ApplicationDTO>> violations = validator.validate(application);
        assertTrue(violations.isEmpty(), "Expected no violations, but found some in nested objects.");
    }
}
