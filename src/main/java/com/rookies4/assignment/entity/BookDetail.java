package com.rookies4.assignment.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Entity
@Table(name = "book_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_detail_id")
    private Long id;

    private String description;

    private String language;

    private Integer pageCount;

    private String publisher;

    private String coverImageUrl;

    private String edition;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;




}
