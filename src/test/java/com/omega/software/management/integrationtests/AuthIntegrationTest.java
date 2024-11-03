package com.omega.software.management.integrationtests;

import com.omega.software.management.configs.Dmls;
import com.omega.software.management.data.dto.LoginRequest;
import com.omega.software.management.data.dto.UserInfoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationTest extends TestBase {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    void signingOkWithCorrectCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("artem.java.sirobaba@omegasoft.com");
        loginRequest.setPassword("Art456###");

        var result = mockMvc.perform(post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserInfoResponse userInfoResponse = objectMapper.readValue(contentAsString, UserInfoResponse.class);
        Assertions.assertNotNull(userInfoResponse.getJwt());
    }
}
