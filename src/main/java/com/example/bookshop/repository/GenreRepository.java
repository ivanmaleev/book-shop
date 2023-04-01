package com.example.bookshop.repository;

import com.example.bookshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    List<Genre> findAllByLang(String lang);

    /**
     * Возвращает жанр по наименованию
     *
     * @param name Наименование жанра
     * @param lang Локаль
     * @return Жанр, если найдено
     */
    @Query(nativeQuery = true,
            value = "SELECT g.* \n" +
                    "FROM book_shop.genre(?2) AS g \n" +
                    "WHERE lower(?1) LIKE concat('%', lower(g.name), '%') \n" +
                    "LIMIT 1")
    Optional<Genre> findByName(String name, String lang);
}
