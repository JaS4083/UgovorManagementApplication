package com.omega.software.management.integrationtests;

import com.jayway.jsonpath.JsonPath;
import com.omega.software.management.configs.Dmls;
import com.omega.software.management.data.dto.LoginRequest;
import com.omega.software.management.data.dto.UserInfoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ArtiklIntegrationTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("artem.java.sirobaba@omegasoft.com");
        loginRequest.setPassword("Art456###");

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserInfoResponse userInfoResponse = objectMapper.readValue(contentAsString, UserInfoResponse.class);
        this.jwtToken = userInfoResponse.getJwt();
    }

    //get
    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void getArtiklByUgovorId() throws Exception {

        final var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/artikli//ugovori/1")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")).andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$[0].id");

        Assertions.assertEquals(101, id);
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void getArtiklById() throws Exception {

        final var contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/artikli/101")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")).andReturn().getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$.id");

        Assertions.assertEquals(101, id);
    }

    // add
    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void addArtikl() throws Exception {

        String artikl = "{\n" +
                "    \"naziv\": \"Artikl 1\",\n" +
                "    \"dobavljac\": \"Dobavljac 1\",\n" +
                "    \"kolicina\": 10,\n" +
                "    \"status\": \"KREIRANO\"\n" +
                "}";

        final var contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/api/artikli/ugovori/1")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .content(artikl)).andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori/1" )
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artiklis").isArray())
                .andExpect(jsonPath("$.artiklis[0]").isNotEmpty())
                .andExpect(jsonPath("$.artiklis[1]").isNotEmpty());

    }

    //update
    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void updateArtikl() throws Exception {

        String artikl = "{\n" +
                "    \"naziv\": \"Artikl 1\",\n" +
                "    \"dobavljac\": \"Dobavljac 1\",\n" +
                "    \"kolicina\": 10,\n" +
                "    \"status\": \"NARUCENO\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/artikli/101")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .content(artikl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naziv").value("Artikl 1"))
                .andExpect(jsonPath("$.dobavljac").value("Dobavljac 1"))
                .andExpect(jsonPath("$.kolicina").value(10))
                .andExpect(jsonPath("$.status").value("NARUCENO"));

    }

    //delete
    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void deleteArtikl() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/artikli/101")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType("application/json"))
                    .andExpect(status().isAccepted());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori/1" )
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artiklis").isArray())
                .andExpect(jsonPath("$.artiklis").isEmpty());

    }
}
