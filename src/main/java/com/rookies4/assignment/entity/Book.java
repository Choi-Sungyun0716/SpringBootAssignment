package com.rookies4.assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_detail_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false, updatable = false)
    private LocalDate publishDate;

    @Column(nullable = false)
    private Integer price;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL)
    private BookDetail bookDetail;
}
