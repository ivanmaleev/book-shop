package com.example.bookshop.repository;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.entity.Genre;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

/**
 * Репозиторий книг
 */
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "false")
@Repository
public interface BookRepository extends JpaRepository<BookLocal, Long> {

    Page<BookLocal> findAllByAuthorId(Long authorId, Pageable pageable);

    Page<BookLocal> findAllByGenre(Genre genre, Pageable pageable);

    Page<BookLocal> findAllByIsBestseller(Integer isBestseller, Pageable pageable);

    Page<BookLocal> findAllByPubDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Optional<BookLocal> findTopBySlug(String slug);

    Collection<BookLocal> findAllBySlugIn(Collection<String> slugList);

    Page<BookLocal> findAllByTitleLikeIgnoreCase(String titleLike, Pageable pageable);
}
