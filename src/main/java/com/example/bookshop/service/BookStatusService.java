package com.example.bookshop.service;

import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Интерфейс сервиса управления статусами книг
 */
@Service
public interface BookStatusService {

    /**
     * Меняет статус книги
     *
     * @param bookCartRequest Статус книги для изменения
     * @param request         Входящий Http запрос
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    void changeBookStatus(BookCartRequest bookCartRequest, HttpServletRequest request,
                          HttpServletResponse response, Model model);

    /**
     * Шаблонный метод для изменения списка книг в пользовательских куки
     *
     * @param cookieName            Название куки
     * @param bookCartRequest       Статус книги для изменения
     * @param cookies               Куки
     * @param response              Исходящий http ответ
     * @param model                 Модель страницы
     * @param resultBookIdsConsumer Консьюмер для изменения результата
     */
    void acceptRequestBookIdsToCookie(String cookieName, BookCartRequest bookCartRequest, Cookie[] cookies,
                                      HttpServletResponse response, Model model,
                                      BiConsumer<Set<String>, List<String>> resultBookIdsConsumer);
}
