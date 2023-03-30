package com.example.bookshop.repository;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.entity.Genre;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "false")
@Repository
public interface BookRepository extends JpaRepository<BookLocal, Long> {

    Page<BookLocal> findAllByAuthor(Author author, Pageable pageable);

    Page<BookLocal> findAllByGenre(Genre genre, Pageable pageable);

    Page<BookLocal> findAllByIsBestseller(Integer isBestseller, Pageable pageable);

    Page<BookLocal> findAllByPubDateBetween(Date fromDate, Date toDate, Pageable pageable);

    Optional<BookLocal> findTopBySlug(String slug);

    List<BookLocal> findAllBySlugIn(Collection<String> slugList);
}
