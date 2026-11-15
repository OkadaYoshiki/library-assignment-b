package com.example.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.entity.Book;
import com.example.library.entity.BookStatus;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByIdAsc();

    /** タイトルまたは著者名にキーワードを含む書籍を検索（部分一致） */
    List<Book> findByTitleContainingOrAuthorContainingOrderByIdAsc(String title, String author);

    List<Book> findByStatusOrderByIdAsc(BookStatus status);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
