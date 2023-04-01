package com.example.bookshop.service.impl;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Genre;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.UsersBookService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Реализация сервиса книг (локальное хранение)
 */
@Slf4j
@NoArgsConstructor
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "false")
public class BookServiceLocalImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UsersBookService usersBookService;

    /**
     * Возвращает страницу книг автора
     *
     * @param author Автор
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    @Override
    public List<? extends Book> getBooksByAuthor(Author author, Integer offset, Integer limit) {
        return bookRepository.findAllByAuthor(author, PageRequest.of(offset, limit)).getContent();
    }

    /**
     * Возвращает страницу рекомендованных книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    @Override
    public List<? extends Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        return bookRepository.findAllByIsBestseller(1, PageRequest.of(offset, limit)).getContent();
    }

    /**
     * Возвращает страницу недавних книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @param from   Начало периода поиска
     * @param end    Конец периода поиска
     * @return Список книг
     */
    @Override
    public List<? extends Book> getPageOfRecentBooks(Integer offset, Integer limit, LocalDate from, LocalDate end) {
        return bookRepository.findAllByPubDateBetween(from, end, PageRequest.of(offset, limit)).getContent();
    }

    /**
     * Возвращает страницу популярных книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    @Override
    public List<? extends Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        return bookRepository.findAllByIsBestseller(1, PageRequest.of(offset, limit)).getContent();
    }

    /**
     * Возвращает страницу книг по поиску слова
     *
     * @param searchWord Слово для поиска
     * @param offset     Оффсет для страницы
     * @param limit      Лимит для страницы
     * @return Список книг
     */
    @Override
    public List<? extends Book> getPageOfSearchResult(String searchWord, Integer offset, Integer limit) {
        return Collections.emptyList();
    }

    /**
     * Возвращает книгу по идентификатору книги
     *
     * @param slug Идентификатор кинги
     * @return Книга
     * @throws Exception Если книга не найдена
     */
    @Override
    public Book getBook(String slug) throws Exception {
        return bookRepository.findTopBySlug(slug)
                .orElseThrow(() -> new Exception("Книга с id = " + Objects.toString(slug) + " не найдена"));
    }

    /**
     * Возвращает список книг по списку идентификаторов
     *
     * @param slugList Список идентификаторов
     * @return Список книг
     */
    @Override
    public List<? extends Book> getBooks(Collection<String> slugList) {
        return bookRepository.findAllBySlugIn(slugList);
    }

    /**
     * Возвращает страницу книг по жанру
     *
     * @param genreId id жанра
     * @param offset  Оффсет для страницы
     * @param limit   Лимит для страницы
     * @return Список книг
     */
    @Override
    public List<? extends Book> getBooksByGenreId(long genreId, Integer offset, Integer limit) {
        Genre genre = new Genre();
        genre.setId(genreId);
        return bookRepository.findAllByGenre(genre, PageRequest.of(offset, limit)).getContent();
    }

    /**
     * Вовзращает список книг пользователя
     *
     * @param userId   Пользователь
     * @param archived Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Список книг
     */
    @Override
    public List<? extends Book> findUsersBooks(Long userId, boolean archived) {
        List<String> bookIds = usersBookService.findUsersBooks(userId, Collections.emptyList(), archived)
                .stream()
                .map(UsersBook::getBookId)
                .collect(Collectors.toList());
        return bookRepository.findAllBySlugIn(bookIds);
    }
}
