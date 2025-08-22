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

    public List<BookDTO.BookResponse> getAllBooks(){
        return bookRepository.findAll() //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.BookResponse::from) //Stream<BookDTO.BookResponse>
                .toList(); //Stream<BookDTO.BookResponse>
    }

    public BookDTO.BookResponse getBookById(Long id){
        Book book = getBookExist(id);
        return BookDTO.BookResponse.from(book);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn){
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));

        return BookDTO.BookResponse.from(book);
    }

    public List<BookDTO.BookResponse> getBooksByAuthor(String author){
        return bookRepository.findByAuthor(author) //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.BookResponse::from) //Stream<BookDTO.BookResponse>
                .toList(); //Stream<BookDTO.BookResponse>
    }

    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request){
        Book entity = request.toEntity();

        Book savedEntity = bookRepository.save(entity);

        return BookDTO.BookResponse.from(savedEntity);
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request){

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

        return BookDTO.BookResponse.from(book);
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
