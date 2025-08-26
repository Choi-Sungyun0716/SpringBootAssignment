package com.rookies4.assignment.repository;

import com.rookies4.assignment.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    // bookId로 BookDetail 조회
    @Query("SELECT bd FROM BookDetail bd WHERE bd.book.id = :bookId")
    Optional<BookDetail> findByBookId(@Param("bookId") Long bookId);

    // BookDetail + Book 함께 조회
    @Query("SELECT bd FROM BookDetail bd JOIN FETCH bd.book WHERE bd.id = :id")
    Optional<BookDetail> findByIdWithBook(@Param("id") Long id);

    // 출판사명으로 BookDetail 리스트 조회
    List<BookDetail> findByPublisherContainingIgnoreCase(String publisher);
}
