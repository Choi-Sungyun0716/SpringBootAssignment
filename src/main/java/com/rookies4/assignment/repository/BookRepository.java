package com.rookies4.assignment.repository;

import com.rookies4.assignment.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // ISBN으로 Book 조회
    Optional<Book> findByIsbn(String isbn);

    // 저자 검색 (대소문자 무시 + 부분 검색)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // 제목 검색 (대소문자 무시 + 부분 검색)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // ID로 Book + BookDetail 함께 조회
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    // ISBN으로 Book + BookDetail 함께 조회
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);

    // ISBN 존재 여부 확인
    boolean existsByIsbn(String isbn);
}
