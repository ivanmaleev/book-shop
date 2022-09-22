package com.example.bookshop.controllers;

import com.example.bookshop.dto.BookCartRequest;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CartService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@NoArgsConstructor
public class CartController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CartService cartService;

    @GetMapping("/books/cart")
    public String cartPage(@CookieValue(value = "cartContents", required = false) String cartContents,
                           Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
            model.addAttribute("cartData", new CartData(0, 0));
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) :
                    cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<Book> books = Arrays.stream(cookieSlugs)
                    .map(slug -> bookService.getBook(slug))
                    .collect(Collectors.toList());
            model.addAttribute("bookCart", books);
            model.addAttribute("cartData", getCartData(books));
        }
        return "cart";
    }

    @PostMapping("/books/changeBookStatus")
    public void handleChangeBookStatus(@RequestBody BookCartRequest bookCartRequest,
                                       HttpServletRequest request,
                                       HttpServletResponse response, Model model) {
        cartService.changeBookStatus(bookCartRequest, request, response, model);
    }

    private CartData getCartData(List<Book> books) {
        return new CartData(books
                .stream()
                .map(Book::getPrice)
                .reduce(Integer::sum)
                .orElse(0),
                books
                        .stream()
                        .map(Book::getPriceOld)
                        .reduce(Integer::sum)
                        .orElse(0));
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
