package com.example.bookshop.repository;

import com.example.bookshop.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(nativeQuery = true,
            value = "SELECT g.* \n" +
                    "FROM book_shop.genre(?1) AS g \n" +
                    "ORDER BY g.ord")
    List<Genre> findAllByLang(String lang);
}
