package com.vrrom.api;

import com.vrrom.carInfoApi.service.CarService;
import com.vrrom.email.service.EmailService;
import com.vrrom.euribor.service.EuriborService;
import com.vrrom.exception.VehicleServiceException;
import com.vrrom.util.ApiHealthChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApiHealthCheckerTest {
    @Mock
    private CarService carService;
    @Mock
    private EmailService emailService;
    @Mock
    private EuriborService euriborService;
    private ApiHealthChecker apiHealthChecker;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        apiHealthChecker = new ApiHealthChecker(carService, emailService, euriborService);
        when(euriborService.fetchEuriborRates(anyString())).thenAnswer(invocation -> Mono.just("Mocked rate for " + invocation.getArgument(0)));
    }

    @Test
    public void testCheckCarMakesHealth_Successful() {
        when(carService.getMakes()).thenReturn("Successful response");
        apiHealthChecker.checkApiHealth();
        verify(carService).getMakes();
        verifyNoInteractions(emailService);
    }

    @Test
    public void testCheckCarMakesHealth_Failure() {
        doThrow(new RuntimeException("Service Unavailable")).when(carService).getMakes();
        apiHealthChecker.checkApiHealth();
        verify(carService).getMakes();
        verify(emailService).sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "API Downtime Alert", "Health check failed for vehicle models API: Service Unavailable");
    }

    @Test
    public void testCheckCarModelsForMakeHealth_Successful() throws VehicleServiceException {
        when(carService.getModels("Toyota")).thenReturn("Model data");
        apiHealthChecker.checkApiHealth();
        verify(carService).getModels("Toyota");
        verifyNoInteractions(emailService);
    }

    @Test
    public void testCheckCarModelsForMakeHealth_Failure() throws VehicleServiceException {
        doThrow(new RuntimeException("Service Unavailable")).when(carService).getModels("Toyota");
        apiHealthChecker.checkApiHealth();
        verify(carService).getModels("Toyota");
        verify(emailService).sendEmail("vrroom.leasing@gmail.com", "vrroom.leasing@gmail.com", "API Downtime Alert", "Health check failed for vehicle models API: Service Unavailable");
    }

    @Test
    public void testCheckEuriborRatesHealth_Successful() {
        String term = "6m";
        when(euriborService.fetchEuriborRates(term)).thenReturn(Mono.just("Rate: 0.5%"));
        apiHealthChecker.checkApiHealth();
        verify(euriborService).fetchEuriborRates(term);
        verifyNoInteractions(emailService);
    }

    @Test
    public void testCheckEuriborRatesHealth_Failure() {
        String term = "3m";
        when(euriborService.fetchEuriborRates(term)).thenReturn(Mono.error(new RuntimeException("Service Unavailable")));
        apiHealthChecker.checkApiHealth();
        verify(euriborService).fetchEuriborRates(term);
        verify(emailService).sendEmail(eq("vrroom.leasing@gmail.com"), eq("vrroom.leasing@gmail.com"), eq("API Downtime Alert"), contains("Health check failed for Euribor rates API for term " + term + ": Service Unavailable"));
    }
}
