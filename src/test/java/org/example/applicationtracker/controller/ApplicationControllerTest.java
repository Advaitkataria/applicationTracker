package org.example.applicationtracker.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setup() throws Throwable{
        mockMvc.perform(
                post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(
                        """ 
                            { "name": "Test user",
                            "email": "test@gmail.com",
                            "password": "password123" }   
                        """
                )
        );

        String loginResponse = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                "email" : "test@gmail.com",
                "password": "password123"
                }        
                """
        )).andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(loginResponse).get("token").asText();
    }

    @Nested
    @DisplayName("Auth endpoint tests")
    class AuthTests {

        @Test
        @DisplayName("POST /auth/register returns 201 with valid data")
        void registerReturns201() throws Exception {
            mockMvc.perform(
                            post("/auth/register")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                        {
                            "name": "New User",
                            "email": "newuser@gmail.com",
                            "password": "password123"
                        }
                    """)
                    )
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("POST /auth/login returns 401 with wrong credentials")
        void loginReturns401WithWrongCredentials() throws Exception {
            mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                        {
                            "email": "wrong@gmail.com",
                            "password": "wrongpassword"
                        }
                    """)
                    )
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Security tests")
    class SecurityTests {

        @Test
        @DisplayName("GET /applications returns 401 without token")
        void getApplicationsReturns401WithoutToken() throws Exception {
            mockMvc.perform(get("/applications"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("GET /applications returns 401 with invalid token")
        void getApplicationsReturns401WithInvalidToken() throws Exception {
            mockMvc.perform(
                            get("/applications")
                                    .header("Authorization", "Bearer invalidtoken")
                    )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("GET /applications returns 200 with valid token")
        void getApplicationsReturns200WithValidToken() throws Exception {
            mockMvc.perform(
                            get("/applications")
                                    .param("page", "0")
                                    .param("size", "10")
                                    .header("Authorization", "Bearer " + jwtToken)
                    )
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Application CRUD tests")
    class ApplicationTests {

        @Test
        @DisplayName("POST /applications returns 201 with valid data")
        void addApplicationReturns201() throws Exception {
            mockMvc.perform(
                            post("/applications")
                                    .header("Authorization", "Bearer " + jwtToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                        {
                            "companyName": "Boeing",
                            "jobTitle": "Java Developer Intern",
                            "status": "Applied",
                            "appliedDate": "2026-09-01",
                            "notes": "Applied through LinkedIn",
                            "salaryExpectation": 25000
                        }
                    """)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.companyName").value("Boeing"));
        }

        @Test
        @DisplayName("POST /applications returns 401 without token")
        void addApplicationReturns401WithoutToken() throws Exception {
            mockMvc.perform(
                            post("/applications")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                        {
                            "companyName": "Boeing"
                        }
                    """)
                    )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("DELETE /applications/999 returns 404 when not found")
        void deleteReturns404WhenNotFound() throws Exception {
            mockMvc.perform(
                            delete("/applications/999")
                                    .header("Authorization", "Bearer " + jwtToken)
                    )
                    .andExpect(status().isNotFound());
        }
    }
}
