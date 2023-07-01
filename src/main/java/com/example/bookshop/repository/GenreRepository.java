package com.example.bookshop.repository;

import com.example.bookshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Репозиторий жанров книг
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    /**
     * Возвращает список жанров по локали
     *
     * @param lang Локаль
     * @return Ссписок жанров
     */
    @Query(nativeQuery = true,
            value = "SELECT g.* \n" +
                    "FROM book_shop.genre(?1) AS g \n" +
                    "ORDER BY g.ord")
    Collection<Genre> findAllByLang(String lang);

    /**
     * Возвращает список жанров по локали
     *
     * @param lang Локаль
     * @return Ссписок жанров
     */
    @Query(nativeQuery = true,
            value = "SELECT g.* \n" +
                    "FROM book_shop.genre(?1) AS g \n" +
                    "WHERE g.id = (?2)")
    Optional<Genre> findByIdAndLang(String lang, Long id);
}
