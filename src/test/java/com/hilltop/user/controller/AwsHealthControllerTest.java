package com.hilltop.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Aws health controller test
 * Unit tests for {@link  AwsHealthController}
 */
class AwsHealthControllerTest {

    /**
     * Unit tests for ping() method.
     */
    @Test
    void Should_ReturnPingMessage_When_RequestIsSuccessful() throws Exception {
        openMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AwsHealthController()).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("This is hill-top-user service !!!"));
    }

}