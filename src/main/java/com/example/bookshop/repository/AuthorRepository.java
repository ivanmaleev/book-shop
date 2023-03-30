package com.example.bookshop.repository;

import com.example.bookshop.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findById(long id);

    @Query(nativeQuery = true,
            value = "SELECT * \n" +
                    "FROM book_shop.author a \n" +
                    "WHERE lower(?1) LIKE concat('%', lower(a.last_name), '%') \n" +
                    "LIMIT 1")
    Author findTopByLastNameLike(String lastName);

}
