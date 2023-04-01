package com.example.bookshop.repository;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий рейтингов книг
 */
@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {

    /**
     * Возвращает рейтинг книги по идентификатору книги
     *
     * @param bookId Идентификатор книги
     * @return Рейтинг книги, если найден
     */
    @Query(value = "SELECT new com.example.bookshop.dto.BookRatingDto(max(br.bookId), cast(avg(br.rating) as integer), count(br.id)) \n" +
            "FROM BookRating AS br \n" +
            "WHERE br.bookId = ?1 \n" +
            "GROUP BY br.bookId"
    )
    Optional<BookRatingDto> findBookRating(String bookId);

    /**
     * Возвращает список рейтингов книг по идентификаторам книг
     *
     * @param bookIds Список идентификаторов книг
     * @return Список рейтингов книг
     */
    @Query(value = "SELECT new com.example.bookshop.dto.BookRatingDto(max(br.bookId), cast(avg(br.rating) as integer), count(br.id)) \n" +
            "FROM BookRating AS br \n" +
            "WHERE br.bookId IN (?1) \n" +
            "GROUP BY br.bookId"
    )
    List<BookRatingDto> findBooksRating(List<String> bookIds);

    /**
     * Возвращает список рейтингов кинг пользователя
     *
     * @param user   Пользователь
     * @param bookId Идентификатор книги
     * @return Список рейтингов книг
     */
    List<BookRating> findAllByUserAndAndBookId(BookstoreUser user, String bookId);
}
