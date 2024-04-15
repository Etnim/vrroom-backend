package com.vrrom.euribor;

import com.vrrom.euribor.controller.EuriborController;
import com.vrrom.euribor.service.EuriborService;
import com.vrrom.util.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = EuriborController.class)
@Import(GlobalExceptionHandler.class)
public class EuriborControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EuriborService euriborService;

    @Test
    public void whenInvalidTerm_thenBadRequest() {
        String invalidTerm = "invalidTerm";
        when(euriborService.fetchEuriborRates(invalidTerm))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid term. Use '3m' or '6m' only.")));

        webTestClient.get().uri("/euribor/{term}", invalidTerm)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Error: Invalid term. Use '3m' or '6m' only.");
    }

    @Test
    public void getEuriborRates_WhenTermIsValid_ReturnsData() {
        String term = "3m";
        String expectedData = "\"0.5%\"";
        when(euriborService.fetchEuriborRates(term)).thenReturn(Mono.just(expectedData));

        webTestClient.get().uri("/euribor/{term}", term)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(expectedData);
    }
}

