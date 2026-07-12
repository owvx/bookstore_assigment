package com.example.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.controller.BookController;
import com.example.bookstore.exception.GlobalExceptionHandler;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;

@SpringBootTest
class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private Validator validator;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldCreateBook() throws Exception {
        String requestBody = """
                {
                  "title": "Spring in Action",
                  "author": "Craig Walls",
                  "isbn": "9781617294945",
                  "price": 49.99
                }
                """;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController)
          .setControllerAdvice(globalExceptionHandler)
          .setValidator(validator)
          .build();

        mockMvc.perform(post("/api/books")
            .contentType("application/json")
            .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.title").value("Spring in Action"));
    }

    @Test
    void shouldRejectNegativePrice() throws Exception {
        String requestBody = """
                {
                  "title": "Bad Price",
                  "author": "Test Author",
                  "isbn": "1111111111",
                  "price": -10
                }
                """;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController)
          .setControllerAdvice(globalExceptionHandler)
          .setValidator(validator)
          .build();

        mockMvc.perform(post("/api/books")
            .contentType("application/json")
            .content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateBookWithAuthor() throws Exception {
        Author author = new Author();
        author.setFirstName("Jane");
        author.setLastName("Austen");
        author.setBiography("English novelist.");
        author = authorRepository.save(author);

        String requestBody = """
                {
                  "title": "Pride and Prejudice",
                  "author": "Jane Austen",
                  "isbn": "9780141439518",
                  "price": 12.99,
                  "authorId": %d
                }
                """.formatted(author.getId());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController)
          .setControllerAdvice(globalExceptionHandler)
          .setValidator(validator)
          .build();

        mockMvc.perform(post("/api/books")
            .contentType("application/json")
            .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.title").value("Pride and Prejudice"))
          .andExpect(jsonPath("$.authorEntity.id").value(author.getId()));
    }

    @Test
    void shouldFailToCreateBookWithNonExistentAuthor() throws Exception {
        String requestBody = """
                {
                  "title": "Unknown Author Book",
                  "author": "Some Author",
                  "isbn": "0000000000",
                  "price": 15.00,
                  "authorId": 9999
                }
                """;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController)
          .setControllerAdvice(globalExceptionHandler)
          .setValidator(validator)
          .build();

        mockMvc.perform(post("/api/books")
            .contentType("application/json")
            .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("Author not found with id 9999"));
    }
}

