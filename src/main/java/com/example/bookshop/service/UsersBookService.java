package com.example.bookshop.service;

import com.example.bookshop.constants.BookStatus;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Интерфейс сервиса пользовательских книг
 */
@Service
public interface UsersBookService {

    /**
     * Добавляет книги пользовтелю
     *
     * @param bookSlugList Список идентификаторов книг для добавления
     * @param user         Пользователь
     * @param bookStatus   Статус книг
     */
    void addBooksToUser(List<String> bookSlugList, BookstoreUser user, BookStatus bookStatus);

    /**
     * Найти список книг пользователя
     *
     * @param userId       id пользовтеля
     * @param bookSlugList Список идентификаторов
     * @param archived     Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Список пользовтальских книг
     */
    List<UsersBook> findUsersBooks(Long userId, List<String> bookSlugList, Boolean archived);

    /**
     * Возвращает статус книги пользователя
     *
     * @param userId   id пользователя
     * @param bookSlug Идентификатор книги
     * @return Статус книги
     */
    BookStatus getBookStatus(Long userId, String bookSlug);

    /**
     * Возвращает количество книг пользователя
     *
     * @param userId id пользователя
     * @return Количество книг пользователя
     */
    int getCount(Long userId);
}
