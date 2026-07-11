package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthorRequest;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody AuthorRequest request) {
        Author author = new Author();
        author.setFirstName(request.firstName());
        author.setLastName(request.lastName());
        author.setBiography(request.biography());
        return ResponseEntity.status(HttpStatus.CREATED).body(authorRepository.save(author));
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        existingAuthor.setFirstName(request.firstName());
        existingAuthor.setLastName(request.lastName());
        existingAuthor.setBiography(request.biography());
        return authorRepository.save(existingAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        authorRepository.delete(existingAuthor);
        return ResponseEntity.noContent().build();
    }
}
