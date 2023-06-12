package com.example.bookshop.service;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Интерфейс сервиса управления корзиной
 */
@Service
public interface CartService {

    /**
     * Возвращает список книг из корзины
     *
     * @param cartContents Содерживое корзины куки
     * @return Список книг
     */
    Collection<BookDto> getCartBooks(String cartContents);

    /**
     * Возвращает данных по суммам в карзине
     *
     * @param bookDtos Список книг
     * @return Суммовые данные для корзины
     */
    CartData getCartData(Collection<BookDto> bookDtos);

    /**
     * Добавляет книгу в корзину
     *
     * @param bookCartRequest Книга для добавления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    void addBookToCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                       HttpServletResponse response, Model model);

    /**
     * Удаляет книгу из корзины
     *
     * @param bookCartRequest Книга для удаления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    void removeBookFromCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                            HttpServletResponse response, Model model);
}
