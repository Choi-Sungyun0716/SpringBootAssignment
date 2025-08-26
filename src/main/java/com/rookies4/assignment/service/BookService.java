package com.rookies4.assignment.service;

import com.rookies4.assignment.controller.dto.BookDTO;
import com.rookies4.assignment.entity.Book;
import com.rookies4.assignment.exception.BusinessException;
import com.rookies4.assignment.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public List<BookDTO.Response> getAllBooks(){
        return bookRepository.findAll() //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.Response::fromEntity) //Stream<BookDTO.BookResponse>
                .toList(); //Stream<BookDTO.BookResponse>
    }

    public BookDTO.Response getBookById(Long id){
        Book book = getBookExist(id);
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn){
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));

        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author){
        return bookRepository.findByAuthorContainingIgnoreCase(author) //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.Response::fromEntity) //Stream<BookDTO.BookResponse>
                .toList(); //Stream<BookDTO.BookResponse>
    }

    public List<BookDTO.Response> getBookByTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream() //Stream<Book>
                .map(BookDTO.Response::fromEntity) //Stream<BookDTO.BookResponse>
                .toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request){
        Book entity = request.toEntity();

        Book savedEntity = bookRepository.save(entity);

        return BookDTO.Response.fromEntity(savedEntity);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request){

        Book book = getBookExist(id);

        if(request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }

        if(request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }

        if(request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }

        if(request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }

        return BookDTO.Response.fromEntity(book);
    }

    @Transactional
    public void deleteBook(Long id){
        Book book = getBookExist(id);
        bookRepository.delete(book);
    }

    //내부 Helpper Method
    private Book getBookExist(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }
}