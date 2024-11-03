package com.omega.software.management.integrationtests;


import com.jayway.jsonpath.JsonPath;
import com.omega.software.management.configs.Dmls;
import com.omega.software.management.data.dto.LoginRequest;
import com.omega.software.management.data.dto.UserInfoResponse;
import com.omega.software.management.data.repository.KupoprodajniUgovorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class KupoprodajniUgovorIntegrationTest extends TestBase {

    @Autowired
    private KupoprodajniUgovorRepository repository;
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

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    public void testCreateUgovor() throws Exception {
        String newUgovor = """
                {
                    "kupac": "Ivan Horvat",
                    "broj_ugovora": "5/2024",
                    "datum_akontacije": "2024-10-15",
                    "rok_isporuke": "2025-01-01",
                    "status": "KREIRANO"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/ugovori")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(newUgovor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kupac").value("Ivan Horvat"))
                .andExpect(jsonPath("$.broj_ugovora").value("5/2024"))
                .andExpect(jsonPath("$.status").value("KREIRANO"))
                .andExpect(jsonPath("$.datum_akontacije").value("2024-10-15"))
                .andExpect(jsonPath("$.rok_isporuke").value("2025-01-01"));
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    public void testGetUgovorById() throws Exception {
        String newUgovor = """
                {
                    "kupac": "Ivan Horvat",
                    "broj_ugovora": "5/2024",
                    "datum_akontacije": "2024-10-15",
                    "rok_isporuke": "2025-01-01",
                    "status": "KREIRANO"
                }
                """;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/ugovori")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(newUgovor))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$.id");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori/" + id)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kupac").value("Ivan Horvat"))
                .andExpect(jsonPath("$.broj_ugovora").value("5/2024"))
                .andExpect(jsonPath("$.status").value("KREIRANO"));
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    public void testGetByUgovorAndStatusOk() throws Exception {
        String newUgovor = """
                {
                    "kupac": "Ivan Horvat",
                    "broj_ugovora": "5/2024",
                    "datum_akontacije": "2024-10-15",
                    "rok_isporuke": "2025-01-01",
                    "status": "KREIRANO"
                }
                """;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/ugovori")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(newUgovor))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$.id");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori"  )
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].kupac").value("Ivan Horvat"))
                .andExpect(jsonPath("$.[0].broj_ugovora").value("5/2024"))
                .andExpect(jsonPath("$.[0].status").value("KREIRANO"));
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    public void testUpdatesUgovorOk() throws Exception {
        String newUgovor = """
                {
                    "kupac": "Ivan Horvat",
                    "broj_ugovora": "5/2024",
                    "datum_akontacije": "2024-10-15",
                    "rok_isporuke": "2025-01-01",
                    "status": "KREIRANO"
                }
                """;

        MvcResult postResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/ugovori")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(newUgovor))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = postResult.getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$.id");

        String updatedUgovor = """
                {
                    "kupac": "Ivan Horvatin",
                    "broj_ugovora": "6/2024",
                    "datum_akontacije": "2024-11-16",
                    "rok_isporuke": "2025-02-02",
                    "status": "NARUCENO"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/ugovori/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(updatedUgovor))
                .andExpect(status().isOk()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori/" + id  )
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.kupac").value("Ivan Horvatin"))
                .andExpect(jsonPath("$.broj_ugovora").value("6/2024"))
                .andExpect(jsonPath("$.datum_akontacije").value("2024-11-16"))
                .andExpect(jsonPath("$.rok_isporuke").value("2025-02-02"))
                .andExpect(jsonPath("$.status").value("NARUCENO"));
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    public void testSoftDeleteUgovorOk() throws Exception {
        String newUgovor = """
                {
                    "kupac": "Ivan Horvat",
                    "broj_ugovora": "5/2024",
                    "datum_akontacije": "2024-10-15",
                    "rok_isporuke": "2025-01-01",
                    "status": "KREIRANO"
                }
                """;

        MvcResult postResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/ugovori")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(newUgovor))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = postResult.getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$.id");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/ugovori/" + id)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json"))
                .andExpect(status().isAccepted());

        var byId = repository.findById(id.longValue()).orElseThrow();

        // assert that is_active False
        Assertions.assertTrue(byId.getIsDeleted());
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA})
    public void testSoftDeleteUgovorTrhowsException() throws Exception {
        String newUgovor = """
                {
                    "kupac": "Ivan Horvat",
                    "broj_ugovora": "5/2024",
                    "datum_akontacije": "2024-10-15",
                    "rok_isporuke": "2025-01-01",
                    "status": "KREIRANO"
                }
                """;

        MvcResult postResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/ugovori")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(newUgovor))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = postResult.getResponse().getContentAsString();

        Integer id = JsonPath.read(contentAsString, "$.id");


        String updatedUgovor = """
                {
                    "kupac": "Ivan Horvatin",
                    "broj_ugovora": "6/2024",
                    "datum_akontacije": "2024-11-16",
                    "rok_isporuke": "2025-02-02",
                    "status": "NARUCENO"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/ugovori/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json")
                        .content(updatedUgovor))
                .andExpect(status().isOk()).andReturn();


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/ugovori/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void testFiltersDataByStatusAndKupac() throws Exception {
        final String kupac = "Petra Kranjčar";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori?kupac=" + kupac + "&status=KREIRANO")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].kupac").value("Petra Kranjčar"))
                .andExpect(jsonPath("$.[0].broj_ugovora").value("1/2024"))
                .andExpect(jsonPath("$.[0].datum_akontacije").value("2024-01-04"))
                .andExpect(jsonPath("$.[0].rok_isporuke").value("2024-04-20"))
                .andExpect(jsonPath("$.[0].status").value("KREIRANO"))
                .andExpect(jsonPath("$.[0].artiklis").isNotEmpty());
    }

    @Test
    @Sql(scripts = {Dmls.REMOVE_ALL_DATA, Dmls.ADD_TEST_USER_DATA, Dmls.ADD_TEST_UGOVOR_ARTIKL_DATA})
    public void testFiltersDataByStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ugovori?status=NARUCENO")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(3))
                .andExpect(jsonPath("$.[0].kupac").value("Stjepan Babić"))
                .andExpect(jsonPath("$.[0].broj_ugovora").value("3/2024"))
                .andExpect(jsonPath("$.[0].datum_akontacije").value("2024-03-03"))
                .andExpect(jsonPath("$.[0].rok_isporuke").value("2024-04-15"))
                .andExpect(jsonPath("$.[0].status").value("NARUCENO"))
                .andExpect(jsonPath("$.[0].artiklis").isNotEmpty());
    }
}
