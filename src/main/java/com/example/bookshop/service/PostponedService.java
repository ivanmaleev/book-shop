package com.example.bookshop.service;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Интерфейс сервиса отложенных книг
 */
@Service
public interface PostponedService {

    /**
     * Возвращает списк отложенных кнги
     *
     * @param postponedContents Данные куки
     * @return Список книг
     */
    Collection<BookDto> getPostponedBooks(String postponedContents);

    /**
     * Добавляает книгу в отложенные
     *
     * @param bookCartRequest Книга для добавления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    void addBookToPostponed(BookCartRequest bookCartRequest, Cookie[] cookies,
                            HttpServletResponse response, Model model);

    /**
     * Удаляет книгу из отложенных
     *
     * @param bookCartRequest Книга для удаления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    void removeBookFromPostponed(BookCartRequest bookCartRequest, Cookie[] cookies,
                                 HttpServletResponse response, Model model);
}
