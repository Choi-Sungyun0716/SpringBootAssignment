package com.rookies4.assignment.service;

import com.rookies4.assignment.controller.dto.BookDTO;
import com.rookies4.assignment.entity.Book;
import com.rookies4.assignment.entity.BookDetail;
import com.rookies4.assignment.entity.Publisher;
import com.rookies4.assignment.exception.BusinessException;
import com.rookies4.assignment.exception.ErrorCode;
import com.rookies4.assignment.repository.BookRepository;
import com.rookies4.assignment.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    // 모든 도서를 조회하며, 각 도서의 출판사 정보에 도서 수를 포함합니다.
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    // ID로 특정 도서를 조회하며, 모든 관련 정보(출판사, 상세정보)를 포함합니다.
    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    // ISBN으로 특정 도서를 조회합니다.
    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ISBN", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    // 작가명으로 도서를 검색합니다.
    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    // 제목으로 도서를 검색합니다.
    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    // 특정 출판사의 모든 도서를 조회합니다.
    public List<BookDTO.Response> getBooksByPublisherId(Long publisherId) {
        if (!publisherRepository.existsById(publisherId)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", publisherId);
        }
        return bookRepository.findByPublisherId(publisherId)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    // 새로운 도서를 생성합니다. 출판사 존재 여부와 ISBN 중복을 검증합니다.
    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", request.getPublisherId()));

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .publisher(publisher)
                .build();

        if (request.getDetail() != null) {
            BookDetail bookDetail = BookDetail.builder()
                    .description(request.getDetail().getDescription())
                    .language(request.getDetail().getLanguage())
                    .pageCount(request.getDetail().getPageCount())
                    .publisher(request.getDetail().getPublisher())
                    .coverImageUrl(request.getDetail().getCoverImageUrl())
                    .edition(request.getDetail().getEdition())
                    .book(book)
                    .build();
            book.setBookDetail(bookDetail);
        }

        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    // 기존 도서 정보를 수정합니다. 출판사와 ISBN 유효성을 검증합니다.
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));

        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", request.getPublisherId()));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());
        book.setPublisher(publisher);

        if (request.getDetail() != null) {
            BookDetail bookDetail = book.getBookDetail();
            if (bookDetail == null) {
                bookDetail = new BookDetail();
                bookDetail.setBook(book);
                book.setBookDetail(bookDetail);
            }
            bookDetail.setDescription(request.getDetail().getDescription());
            bookDetail.setLanguage(request.getDetail().getLanguage());
            bookDetail.setPageCount(request.getDetail().getPageCount());
            bookDetail.setPublisher(request.getDetail().getPublisher());
            bookDetail.setCoverImageUrl(request.getDetail().getCoverImageUrl());
            bookDetail.setEdition(request.getDetail().getEdition());
        }

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    // 도서를 삭제합니다. BookDetail도 Cascade로 함께 삭제됩니다.
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        bookRepository.delete(book);
    }
}
