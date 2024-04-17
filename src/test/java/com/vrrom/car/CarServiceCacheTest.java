package com.vrrom.car;

import com.vrrom.vehicle.carInfoApi.service.CarService;
import com.vrrom.vehicle.exceptions.VehicleServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;

import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableCaching
public class CarServiceCacheTest {
    @Autowired
    private CarService carService;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("makesCache")).clear();
        Objects.requireNonNull(cacheManager.getCache("modelsCache")).clear();
    }

    @Test
    void testGetMakesCaching() throws VehicleServiceException {
        URI uri = URI.create("https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForVehicleType/car?format=json");
        String firstCallResult = carService.getMakes();
        String secondCallResult = carService.getMakes();
        assertEquals(firstCallResult, secondCallResult, "Second result should be fetched from cache and match the first result");
        String cachedResult = Objects.requireNonNull(cacheManager.getCache("makesCache")).get(SimpleKey.EMPTY, String.class);
        assertEquals(firstCallResult, cachedResult, "Cached data should match the first result");
    }

    @Test
    void testGetModelsCaching() throws VehicleServiceException {
        String make = "Toyota";
        URI uri = URI.create("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/car?make=Toyota&format=json");
        String firstCallResult = carService.getModels(make);
        String secondCallResult = carService.getModels(make);
        assertEquals(firstCallResult, secondCallResult, "Second result should be fetched from cache and match the first result");
        String cachedResult = Objects.requireNonNull(cacheManager.getCache("modelsCache")).get(make, String.class);
        assertEquals(firstCallResult, cachedResult, "Cached data should match the first result");
    }
}
