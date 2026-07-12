package com.example.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.controller.AuthorController;
import com.example.bookstore.exception.GlobalExceptionHandler;

@SpringBootTest
class AuthorControllerTest {

    @Autowired
    private AuthorController authorController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private Validator validator;

    @Test
    void shouldCreateAuthor() throws Exception {
        String requestBody = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "biography": "A prolific writer."
                }
                """;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setControllerAdvice(globalExceptionHandler)
                .setValidator(validator)
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.biography").value("A prolific writer."));
    }

    @Test
    void shouldRejectInvalidAuthor() throws Exception {
        String requestBody = """
                {
                  "firstName": "",
                  "lastName": "",
                  "biography": "No name author."
                }
                """;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setControllerAdvice(globalExceptionHandler)
                .setValidator(validator)
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
