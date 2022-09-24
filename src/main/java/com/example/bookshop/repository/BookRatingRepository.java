package com.example.bookshop.repository;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {

    @Query(value = "SELECT new com.example.bookshop.dto.BookRatingDto(max(br.bookId), cast(avg(br.rating) as integer), count(br.id)) \n" +
            "FROM BookRating AS br \n" +
            "WHERE br.bookId = ?1 \n" +
            "GROUP BY br.bookId"
    )
    Optional<BookRatingDto> findBookRating(String bookId);

    @Query(value = "SELECT new com.example.bookshop.dto.BookRatingDto(max(br.bookId), cast(avg(br.rating) as integer), count(br.id)) \n" +
            "FROM BookRating AS br \n" +
            "WHERE br.bookId IN (?1) \n" +
            "GROUP BY br.bookId"
    )
    List<BookRatingDto> findBooksRating(List<String> bookIds);

    List<BookRating> findAllByUserAndAndBookId(BookstoreUser user, String bookId);
}
