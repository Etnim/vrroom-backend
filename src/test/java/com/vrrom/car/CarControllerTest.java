package com.vrrom.car;

import com.vrrom.vehicle.carInfoApi.controller.CarController;
import com.vrrom.vehicle.carInfoApi.service.CarService;
import com.vrrom.util.exceptions.GlobalExceptionHandler;
import com.vrrom.vehicle.exceptions.VehicleServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarController.class)
@Import(GlobalExceptionHandler.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;

    @Test
    public void testGetCarMakes_success() throws Exception {
        when(carService.getMakes()).thenReturn("Toyota, Ford, Honda");
        mockMvc.perform(get("/cars/makes"))
                .andExpect(status().isOk())
                .andExpect(content().string("Toyota, Ford, Honda"));
    }

    @Test
    public void testGetCarModels_success() throws Exception {
        String make = "Toyota";
        when(carService.getModels("Toyota")).thenReturn("Camry, Corolla, RAV4");
        mockMvc.perform(get("/cars/models/{make}", make))
                .andExpect(status().isOk())
                .andExpect(content().string("Camry, Corolla, RAV4"));
    }

    @Test
    public void testGetCarModels_ThrowsVehicleServiceException() throws Exception {
        String make = "Invalid-Make!";
        when(carService.getModels(make)).thenThrow(new VehicleServiceException("Invalid car make", new Throwable("Detailed cause")));
        mockMvc.perform(get("/cars/models/{make}", make))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid car make"));
    }
}
