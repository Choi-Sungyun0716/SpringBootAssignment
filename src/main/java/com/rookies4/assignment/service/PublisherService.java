package com.rookies4.assignment.service;

import com.rookies4.assignment.controller.dto.PublisherDTO;
import com.rookies4.assignment.entity.Publisher;
import com.rookies4.assignment.exception.BusinessException;
import com.rookies4.assignment.exception.ErrorCode;
import com.rookies4.assignment.repository.BookRepository;
import com.rookies4.assignment.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    // 모든 출판사를 조회하며, 각 출판사의 도서 수를 포함합니다.
    @Transactional(readOnly = true)
    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(PublisherDTO.SimpleResponse::fromEntity)
                .toList();
    }

    // ID로 특정 출판사를 조회하며, 해당 출판사의 모든 도서 정보를 포함합니다.
    @Transactional(readOnly = true)
    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id));
        return PublisherDTO.Response.fromEntity(publisher);
    }

    // 이름으로 특정 출판사를 조회합니다.
    @Transactional(readOnly = true)
    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "name", name));
        return PublisherDTO.Response.fromEntity(publisher);
    }

    // 새로운 출판사를 생성합니다. 이름 중복을 검증합니다.
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request) {
        if (publisherRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE, request.getName());
        }
        Publisher publisher = Publisher.builder()
                .name(request.getName())
                .build();
        Publisher saved = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(saved);
    }

    // 기존 출판사 정보를 수정합니다. 이름 중복(자신 제외)을 검증합니다.
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));

        if (publisherRepository.existsByName(request.getName())
                && !publisher.getName().equals(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE, request.getName());
        }

        publisher.setName(request.getName());
        Publisher updated = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(updated);
    }

    // 출판사를 삭제합니다. 해당 출판사에 도서가 있는 경우 삭제를 거부합니다.
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id));

        // 출판사에 도서가 있으면 삭제 거부
        if (publisher.getBooks() != null && !publisher.getBooks().isEmpty()) {
            throw new BusinessException(ErrorCode.PUBLISHER_HAS_BOOKS, "id", bookRepository.countByPublisherId(id));
        }

        publisherRepository.delete(publisher);
    }

}
