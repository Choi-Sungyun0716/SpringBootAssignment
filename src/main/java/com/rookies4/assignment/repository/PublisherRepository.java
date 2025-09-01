package com.rookies4.assignment.repository;

import com.rookies4.assignment.entity.Publisher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    // 이름으로 조회
    Optional<Publisher> findByName(String name);

    // 이름 존재 여부 확인
    boolean existsByName(String name);


    @Query("SELECT p FROM Publisher p LEFT JOIN FETCH p.books WHERE p.id = :id")
    Optional<Publisher> findByIdWithBooks(Long id);

}
