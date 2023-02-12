package com.example.bookshop.service.impl;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.UsersBookService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CommonServiceImpl implements CommonService {

    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private UsersBookService usersBookService;

    @Override
    public CommonPageData getCommonPageData(HttpServletRequest request, boolean usersPage) {
        CommonPageData commonPageData = new CommonPageData(0, 0, 0);
        if (usersPage) {
            final BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
            commonPageData.setUsersBooksCounter(usersBookService.getCount(currentUser.getId()));
        }

        if (request.getCookies() != null) {
            Optional<Cookie> cartContents = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("cartContents"))
                    .findFirst();
            if (cartContents.isPresent() && !cartContents.get().getValue().equals("")) {
                commonPageData.setCartBooksCounter(Arrays.asList(cartContents.get().getValue().split("/")).size());
            }
            Optional<Cookie> postponedContents = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("postponedContents"))
                    .findFirst();
            if (postponedContents.isPresent() && !postponedContents.get().getValue().equals("")) {
                commonPageData.setPostponedBooksCounter(Arrays.asList(postponedContents.get().getValue().split("/")).size());
            }
        }
        return commonPageData;
    }
}
