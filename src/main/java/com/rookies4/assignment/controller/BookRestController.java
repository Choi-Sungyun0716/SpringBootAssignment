package com.rookies4.assignment.controller;

import com.rookies4.assignment.entity.Book;
import com.rookies4.assignment.exception.BusinessException;
import com.rookies4.assignment.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookRepository bookRepository;



    @PostMapping
    public Book getAllBooks(@RequestBody Book book){
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> createBook(){
        return bookRepository.findAll();

    }


    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id){
        Book existBook = getExistBook(id);
        return existBook;

    }

    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn){
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        Book existBook = optionalBook.orElseThrow(()->new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return existBook;

    }
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }


    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id,@RequestBody Book book){
        Book existBook = getExistBook(id);

        existBook.setTitle(book.getTitle());
        existBook.setAuthor(book.getAuthor());
        existBook.setIsbn(book.getIsbn());
        existBook.setPublishDate(book.getPublishDate());
        existBook.setPrice(book.getPrice());

        Book updatedBook = bookRepository.save(existBook);
        return updatedBook;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        Book existUser = getExistBook(id);

        bookRepository.delete(existUser);

        return ResponseEntity.ok("Book이 삭제 되었습니다.");//build를 붙이면 응답 코드만 나감
    }




    private Book getExistBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book existBook = optionalBook.orElseThrow(()->new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return existBook;
    }

    //asdf


}
