package com.vrrom.dtos;

import com.vrrom.vehicle.model.FuelType;
import com.vrrom.vehicle.model.VehicleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testBrandValid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setBrand("Toyota");
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "brand");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testBrandInvalid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setBrand("");  // Blank brand
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "brand");
        assertEquals(1, violations.size());
    }

    @Test
    public void testModelValid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setModel("Corolla");
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "model");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testModelInvalid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setModel("");  // Blank model
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "model");
        assertEquals(1, violations.size());
    }

    @Test
    public void testYearValid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setYear(2020);
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "year");
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testYearInvalid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setYear(1884);
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "year");
        assertEquals(1, violations.size());
    }

    @Test
    public void testFuelInvalid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setFuel(null);
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "fuel");
        assertEquals(1, violations.size());
    }

    @Test
    public void testFuelValid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setFuel(FuelType.ELECTRIC);
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "fuel");
        assertEquals(0, violations.size());
    }

    @Test
    public void testEmissionStartInvalid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setEmissionStart(-2);
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "emissionStart");
        assertEquals(1, violations.size());
    }

    @Test
    public void testEmissionEndInvalid() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setEmissionEnd(131);
        Set<ConstraintViolation<VehicleDTO>> violations = validator.validateProperty(vehicle, "emissionEnd");
        assertEquals(1, violations.size());
    }
}
