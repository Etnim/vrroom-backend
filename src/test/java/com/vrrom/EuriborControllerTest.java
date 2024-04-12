package com.vrrom;

import com.vrrom.controller.EuriborController;
import com.vrrom.handlers.GlobalExceptionHandler;
import com.vrrom.service.EuriborService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EuriborController.class)
@Import(GlobalExceptionHandler.class)
public class EuriborControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EuriborService euriborService;

    @Test
    public void whenInvalidTerm_thenBadRequest() throws Exception {
        String invalidTerm = "invalidTerm";
        Mockito.when(euriborService.fetchEuriborRates(invalidTerm))
                .thenReturn(Mono.error(new IllegalArgumentException("Invalid term. Use '3m' or '6m' only.")));

        mockMvc.perform(get("/euribor/{term}", invalidTerm))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Invalid term. Use '3m' or '6m' only."));
    }
}
