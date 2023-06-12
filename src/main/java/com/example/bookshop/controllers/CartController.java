package com.example.bookshop.controllers;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

/**
 * Контроллер корзины
 */
@Controller
@NoArgsConstructor
@Api(description = "Контроллер корзины")
public class CartController extends CommonController {

    @Autowired
    private CartService cartService;

    @ApiOperation("Получение страницы корзины")
    @ApiResponse(responseCode = "200", description = "Страница корзины")
    @GetMapping("/books/cart")
    public String cartPage(@CookieValue(value = "cartContents", required = false) String cartContents,
                           Model model) {
        if (StringUtils.isBlank(cartContents)) {
            model.addAttribute("isCartEmpty", true);
            model.addAttribute("cartData", new CartData(0, 0));
        } else {
            Collection<BookDto> bookDtos = cartService.getCartBooks(cartContents);
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", bookDtos);
            model.addAttribute("cartData", cartService.getCartData(bookDtos));
        }
        return "cart";
    }

//    @GetMapping("/pay")
//    public RedirectView handlePay(@CookieValue(value = "cartContents", required = false) String cartContents) throws NoSuchAlgorithmException {
//
//        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
//        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) :
//                cartContents;
//        String[] cookieSlugs = cartContents.split("/");
//        List<Book> booksFromCookieSlugs = new ArrayList<>(); //bookRepository.findBooksBySlugIn(cookieSlugs);
//        String paymentUrl = paymentService.getPaymentUrl(booksFromCookieSlugs);
//        return new RedirectView(paymentUrl);
//    }
}
