package com.example.bookshop.service;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Интерфейс сервиса книг
 */
@Service
public interface BookService<T extends Book> {

    /**
     * Возвращает страницу книг автора
     *
     * @param author Автор
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    Collection<T> getBooksByAuthor(Author author, Integer offset, Integer limit);

    /**
     * Возвращает страницу рекомендованных книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    Collection<T> getPageOfRecommendedBooks(Integer offset, Integer limit);

    /**
     * Возвращает страницу недавних книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @param from   Начало периода поиска
     * @param end    Конец периода поиска
     * @return Список книг
     */
    Collection<T> getPageOfRecentBooks(Integer offset, Integer limit, LocalDate from, LocalDate end);

    /**
     * Возвращает страницу популярных книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    Collection<T> getPageOfPopularBooks(Integer offset, Integer limit);

    /**
     * Возвращает страницу книг по поиску слова
     *
     * @param searchWord Слово для поиска
     * @param offset     Оффсет для страницы
     * @param limit      Лимит для страницы
     * @return Список книг
     */
    Collection<T> getPageOfSearchResult(String searchWord, Integer offset, Integer limit);

    /**
     * Возвращает книгу по идентификатору книги
     *
     * @param slug Идентификатор кинги
     * @return Книга
     * @throws Exception Если книга не найдена
     */
    @Cacheable(value = "BookService::getBook", key = "#slug")
    T getBook(String slug) throws Exception;

    /**
     * Возвращает список книг по списку идентификаторов
     *
     * @param slugList Список идентификаторов
     * @return Список книг
     */
    Collection<T> getBooks(Collection<String> slugList);

    /**
     * Возвращает страницу книг по жанру
     *
     * @param genreId id жанра
     * @param offset  Оффсет для страницы
     * @param limit   Лимит для страницы
     * @return Список книг
     */
    Collection<T> getBooksByGenreId(long genreId, Integer offset, Integer limit);

    /**
     * Вовзращает список книг пользователя
     *
     * @param userId   Пользователь
     * @param archived Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Список книг
     */
    Collection<T> findUsersBooks(Long userId, boolean archived);
}
