package com.example.bookshop.service.impl;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.PostponedService;
import com.example.bookshop.service.UsersBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static com.example.bookshop.constants.BookStatus.ARCHIVED;
import static com.example.bookshop.constants.BookStatus.CART;
import static com.example.bookshop.constants.BookStatus.KEPT;
import static com.example.bookshop.constants.BookStatus.UNLINK;
import static com.example.bookshop.constants.BookStatus.UNLINK_POSTPONED;

@ActiveProfiles("test")
@SpringJUnitConfig(BookStatusServiceImplTest.TestConfig.class)
class BookStatusServiceImplTest {

    @Autowired
    private BookStatusService bookStatusService;
    @MockBean
    private CartService cartService;
    @MockBean
    private UsersBookService usersBookService;
    @MockBean
    private PostponedService postponedService;
    @MockBean
    private BookstoreUserRegister userRegister;

    private BookCartRequest bookCartRequest = new BookCartRequest();
    private HttpServletRequest httpServletRequest = new MockHttpServletRequest();
    private HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    private Model model = new ExtendedModelMap();

    @Test
    void changeBookStatusKept() {
        bookCartRequest.setStatus(KEPT);
        Assertions.assertDoesNotThrow(() -> bookStatusService.changeBookStatus(bookCartRequest, httpServletRequest, httpServletResponse, model));
    }

    @Test
    void changeBookStatusCart() {
        bookCartRequest.setStatus(CART);
        Assertions.assertDoesNotThrow(() -> bookStatusService.changeBookStatus(bookCartRequest, httpServletRequest, httpServletResponse, model));
    }

    @Test
    void changeBookStatusArchived() {
        final BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setId(1L);
        bookCartRequest.setStatus(ARCHIVED);
        bookCartRequest.setBookId("id");
        Mockito.when(userRegister.getCurrentUser()).thenReturn(new BookstoreUser());
        Assertions.assertDoesNotThrow(() -> bookStatusService.changeBookStatus(bookCartRequest, httpServletRequest, httpServletResponse, model));
    }

    @Test
    void changeBookStatusUnlink() {
        bookCartRequest.setStatus(UNLINK);
        Assertions.assertDoesNotThrow(() -> bookStatusService.changeBookStatus(bookCartRequest, httpServletRequest, httpServletResponse, model));
    }

    @Test
    void changeBookStatusUnlinkPostponed() {
        bookCartRequest.setStatus(UNLINK_POSTPONED);
        Assertions.assertDoesNotThrow(() -> bookStatusService.changeBookStatus(bookCartRequest, httpServletRequest, httpServletResponse, model));
    }

    @Test
    void acceptRequestBookIdsToCookie() {
        Cookie[] cookies = new Cookie[1];
        Assertions.assertDoesNotThrow(() -> bookStatusService.acceptRequestBookIdsToCookie("cookie", bookCartRequest,
                cookies, httpServletResponse, model, Set::addAll));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public BookStatusService bookStatusService() {
            return new BookStatusServiceImpl();
        }
    }
}