package com.example.bookshop.repository;

import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий пользовательских книг
 */
@Repository
public interface UsersBookRepository extends JpaRepository<UsersBook, Long> {

    /**
     * Возвращвет список пользовательских книг
     *
     * @param userId   id пользователя
     * @param archived Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Список пользовательских книг
     */
    List<UsersBook> findAllByUserIdAndAndArchived(Long userId, boolean archived);

    /**
     * Возвращает список пользовательских книг
     *
     * @param userId id пользователя
     * @return Список пользовательских книг
     */
    List<UsersBook> findAllByUserId(Long userId);

    /**
     * Возвращает список пользовательских книг
     *
     * @param userId       id пользователя
     * @param bookSlugList Список идентификаторов книг
     * @return Список пользовательских книг
     */
    List<UsersBook> findAllByUserIdAndBookIdIn(Long userId, List<String> bookSlugList);

    /**
     * Возвращает список пользовательских книг
     *
     * @param userId       id пользователя
     * @param bookSlugList Список идентификаторов книг
     * @param archived     Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Список пользовательских книг
     */
    List<UsersBook> findAllByUserIdAndBookIdInAndArchived(Long userId, List<String> bookSlugList, boolean archived);

    /**
     * Возвращает количество пользовательских книг
     *
     * @param userId id пользователя
     * @return Количество пользовательских книг
     */
    @Query(nativeQuery = true,
            value = "SELECT count(*) \n" +
                    "FROM book_shop.users_book AS ub \n" +
                    "WHERE ub.user_id = ?1")
    Integer getCount(Long userId);

    /**
     * Возвращает наличие у пользователя книги
     *
     * @param bookId   Идентификатор книги
     * @param user     Пользователь
     * @param archived Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Наличие или отсутствие
     */
    boolean existsByBookIdAndUserAndArchived(String bookId, BookstoreUser user, boolean archived);

    /**
     * @param userId id пользователя
     * @param bookId Идентификатор книги
     * @return ользовательская книга, если найдено
     */
    Optional<UsersBook> findTopByUserIdAndBookId(Long userId, String bookId);
}
