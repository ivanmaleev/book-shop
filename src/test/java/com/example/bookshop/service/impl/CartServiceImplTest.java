package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ActiveProfiles("test")
@SpringJUnitConfig(CartServiceImplTest.TestConfig.class)
class CartServiceImplTest {

    @Autowired
    private CartService cartService;
    @MockBean
    private BookStatusService bookStatusService;
    @MockBean
    private BookRatingService bookRatingService;
    @MockBean
    private BookService<? extends Book> bookService;
    private HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    private Model model = new ExtendedModelMap();

    @Test
    void getCartData() {
        int price = 100;
        int priceOld = 150;
        final BookDto bookDto = new BookDto();
        bookDto.setPrice(price);
        bookDto.setPriceOld(priceOld);
        final CartData cartData = cartService.getCartData(List.of(bookDto));
        Assertions.assertEquals(price, cartData.getSumPrice());
        Assertions.assertEquals(priceOld, cartData.getSumOldPrice());
    }

    @Test
    void getCartBooksEmpty() {
        Collection<? extends Book> booksLocal = List.of(new BookLocal());
        Mockito.when(bookService.getBooks(Mockito.anyList())).thenReturn(Collections.emptyList());
        Assertions.assertTrue(cartService.getCartBooks("/content/").isEmpty());
    }

    @Test
    void addBookToCart() {
        Assertions.assertDoesNotThrow(() -> cartService.addBookToCart(new BookCartRequest(), new Cookie[1], httpServletResponse, model));
    }

    @Test
    void removeBookFromCart() {
        Assertions.assertDoesNotThrow(() -> cartService.removeBookFromCart(new BookCartRequest(), new Cookie[1], httpServletResponse, model));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public CartService cartService() {
            return new CartServiceImpl();
        }
    }
}