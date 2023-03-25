package com.example.bookshop.service;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

public class BookStatusServiceImpl implements BookStatusService {

    static final private String CART_DELIMITER = "/";
    @Autowired
    private CartService cartService;
    @Autowired
    private UsersBookService usersBookService;
    @Autowired
    private PostponedService postponedService;
    @Autowired
    private BookstoreUserRegister userRegister;

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
            usersBookService.addBooksToUser(List.of(bookCartRequest.getBookId()), currentUser, true);
        }


    }

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
        if (StringUtils.isNotBlank(cookieValue)) {
            Cookie cookie = new Cookie(cookieNameContent, cookieValue);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        model.addAttribute(getCookieNameEmpty(cookieName), StringUtils.isBlank(cookieValue));
    }

    private String getCookieNameEmpty(String cookieName) {
        return "is".concat(cookieName).concat("Empty");
    }

    private String getCookieNameContent(String cookieName) {
        return cookieName.toLowerCase().concat("Contents");
    }

    private String getCookieContentValue(Cookie[] cookies, String cookieNameContent) {
        Optional<Cookie> cookieContentOptional = getCookieContentOptional(cookies, cookieNameContent);
        String cartValueCurrent = "";
        if (cookieContentOptional.isPresent() &&
                StringUtils.isNotBlank(cookieContentOptional.get().getValue())) {
            cartValueCurrent = cookieContentOptional.get().getValue();
        }
        return cartValueCurrent;
    }

    private Optional<Cookie> getCookieContentOptional(Cookie[] cookies, String cookieNameContent) {
        Optional<Cookie> cartContentsOptional = Optional.empty();
        if (Objects.nonNull(cookies)) {
            cartContentsOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieNameContent))
                    .findFirst();
        }
        return cartContentsOptional;
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
