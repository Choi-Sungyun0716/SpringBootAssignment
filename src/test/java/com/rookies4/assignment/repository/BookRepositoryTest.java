package com.rookies4.assignment.repository;

import com.rookies4.assignment.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ✅ MariaDB 설정 그대로 사용

class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;


    private Book createSampleBook() {

        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("123-456-789");
        book.setPrice(20000);
        return book;
    }

    @Test
    @Rollback(false)
    @DisplayName("도서 등록 테스트")
    void testCreateBook() {
        Book book = createSampleBook();
        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문");
        assertThat(savedBook.getPublishDate()).isNotNull(); // @CreationTimestamp 적용 확인
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    void testFindByIsbn() {
        Book book = createSampleBook();
        bookRepository.save(book);

        Optional<Book> found = bookRepository.findByIsbn("123-456-789");

        assertThat(found).isPresent();
        assertThat(found.get().getAuthor()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    void testFindByAuthor() {
        Book book1 = createSampleBook();
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("JPA 활용");
        book2.setAuthor("홍길동"); // 같은 저자
        book2.setIsbn("987-654-321");
        book2.setPrice(30000);
        bookRepository.save(book2);

        List<Book> found = bookRepository.findByAuthor("홍길동");

        assertThat(found).hasSize(2);
        assertThat(found.get(0).getAuthor()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("도서 정보 수정 테스트")
    void testUpdateBook() {
        Book book = createSampleBook();
        Book savedBook = bookRepository.save(book);

        savedBook.setPrice(25000); // 가격 수정
        Book updatedBook = bookRepository.save(savedBook);

        assertThat(updatedBook.getPrice()).isEqualTo(25000);
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void testDeleteBook() {
        Book book = createSampleBook();
        Book savedBook = bookRepository.save(book);

        bookRepository.delete(savedBook);

        Optional<Book> deleted = bookRepository.findById(savedBook.getId());
        assertThat(deleted).isNotPresent();
    }
}
