package com.rookies4.assignment.controller;

import com.rookies4.assignment.controller.dto.BookDTO;
import com.rookies4.assignment.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookDetailController {

    private final BookService bookService;

    // 모든 도서 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // ID로 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        BookDTO.Response book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.Response book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    // 저자명으로 도서 조회
    //똑같은 이름도 타자로 입력하면 안되고 post의 바디의 이름을 복붙하면 됨 이유불명
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }


    // 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response savedBook = bookService.createBook(request);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED); // CREATED 201
    }

    // 도서 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id,
                                                           @Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build(); // body 없음, 204 status code
    }

}