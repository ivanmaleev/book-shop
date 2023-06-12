package com.example.bookshop.service.impl;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.PostponedService;
import com.example.bookshop.service.UsersBookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

import static com.example.bookshop.constants.BookStatus.ARCHIVED;

/**
 * Реализация сервиса управления статусами книг
 */
public class BookStatusServiceImpl implements BookStatusService {

    private static final String CART_DELIMITER = "/";
    @Lazy
    @Autowired
    private CartService cartService;
    @Autowired
    private UsersBookService usersBookService;
    @Lazy
    @Autowired
    private PostponedService postponedService;
    @Autowired
    private BookstoreUserRegister userRegister;

    /**
     * Меняет статус книги
     *
     * @param bookCartRequest Статус книги для изменения
     * @param request         Входящий Http запрос
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    @Override
    public void changeBookStatus(BookCartRequest bookCartRequest, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        if (Objects.nonNull(bookCartRequest) && Objects.nonNull(bookCartRequest.getStatus())) {
            switch (bookCartRequest.getStatus()) {
                case KEPT:
                    postponedService.addBookToPostponed(bookCartRequest, request.getCookies(), response, model);
                    break;
                case CART:
                    cartService.addBookToCart(bookCartRequest, request.getCookies(), response, model);
                    break;
                case ARCHIVED:
                    addBookToArchive(bookCartRequest);
                    break;
                case UNLINK:
                    cartService.removeBookFromCart(bookCartRequest, request.getCookies(), response, model);
                    break;
                case UNLINK_POSTPONED:
                    postponedService.removeBookFromPostponed(bookCartRequest, request.getCookies(), response, model);
                    break;
            }
        }
    }

    private void addBookToArchive(BookCartRequest bookCartRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        if (!currentUser.isAnonymousUser() &&
                Objects.nonNull(bookCartRequest) && StringUtils.isNotBlank(bookCartRequest.getBookId())) {
            usersBookService.addBooksToUser(List.of(bookCartRequest.getBookId()), currentUser, ARCHIVED);
        }
    }

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
    @Override
    public void acceptRequestBookIdsToCookie(String cookieName, BookCartRequest bookCartRequest, Cookie[] cookies,
                                             HttpServletResponse response, Model model,
                                             BiConsumer<Set<String>, List<String>> resultBookIdsConsumer) {
        String cookieNameContent = getCookieNameContent(cookieName);

        List<String> requestBookIds = getRequestBookIds(bookCartRequest);

        String cartValueCurrent = getCookieContentValue(cookies, cookieNameContent);
        Set<String> cookieBookIds = StringUtils.isNotBlank(cartValueCurrent) ? Set.of(cartValueCurrent.split(CART_DELIMITER)) : Collections.emptySet();
        Set<String> resultBookIds = new HashSet<>();
        resultBookIds.addAll(cookieBookIds);
        resultBookIdsConsumer.accept(resultBookIds, requestBookIds);

        StringJoiner bookIdsJoiner = new StringJoiner(CART_DELIMITER);
        resultBookIds.forEach(bookIdsJoiner::add);
        String cookieValue = bookIdsJoiner.toString();
        Cookie cookie = new Cookie(cookieNameContent, cookieValue);
        cookie.setPath("/");
        response.addCookie(cookie);
        model.addAttribute(getCookieNameEmpty(cookieName), StringUtils.isBlank(cookieValue));
    }

    private String getCookieNameEmpty(String cookieName) {
        return String.format("%s%s%s", "is", cookieName, "Empty");
    }

    private String getCookieNameContent(String cookieName) {
        return cookieName.toLowerCase().concat("Contents");
    }

    private String getCookieContentValue(Cookie[] cookies, String cookieNameContent) {
        return getCookieContentOptional(cookies, cookieNameContent)
                .map(Cookie::getValue)
                .orElse("");
    }

    private Optional<Cookie> getCookieContentOptional(Cookie[] cookies, String cookieNameContent) {
        return Optional.ofNullable(cookies)
                .map(cookiesNotNull ->
                        Arrays.stream(cookiesNotNull)
                                .filter(cookie -> Objects.nonNull(cookie) && Objects.equals(cookie.getName(), cookieNameContent))
                                .findAny()
                ).flatMap(cookieNotNull -> cookieNotNull);
    }

    private List<String> getRequestBookIds(BookCartRequest bookCartRequest) {
        if (Objects.isNull(bookCartRequest) || StringUtils.isBlank(bookCartRequest.getBookId())) {
            return Collections.emptyList();
        }
        return Arrays.asList(bookCartRequest.getBookId()
                .replace("[", "")
                .replace("]", "")
                .split(", "));
    }
}
