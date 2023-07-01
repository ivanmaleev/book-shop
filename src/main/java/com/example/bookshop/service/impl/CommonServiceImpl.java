package com.example.bookshop.service.impl;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.UsersBookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

/**
 * Реализация сервиса управления статусами книг
 */
public class CommonServiceImpl implements CommonService {

    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private UsersBookService usersBookService;

    /**
     * Возвращает общие данные страниц
     *
     * @param request   Входящий Http запрос
     * @param usersPage Признак пользовательской страницы
     * @return Обюие данные страницы
     */
    @Override
    public CommonPageData getCommonPageData(HttpServletRequest request, boolean usersPage) {
        CommonPageData commonPageData = new CommonPageData(0, 0, 0);
        if (usersPage) {
            final BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
            commonPageData.setUsersBooksCounter(usersBookService.getCount(currentUser.getId()));
        }

        if (Objects.nonNull(request.getCookies())) {
            setCartBooksCounter(request, commonPageData);
            setPostponedBooksCounter(request, commonPageData);
        }
        return commonPageData;
    }

    private void setCartBooksCounter(HttpServletRequest request, CommonPageData commonPageData) {
        commonPageData.setCartBooksCounter(getContentBooksCounter(request, "cartContents"));
    }

    private void setPostponedBooksCounter(HttpServletRequest request, CommonPageData commonPageData) {
        commonPageData.setPostponedBooksCounter(getContentBooksCounter(request, "postponedContents"));
    }

    private Integer getContentBooksCounter(HttpServletRequest request, String contentName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(contentName))
                .findFirst()
                .map(Cookie::getValue)
                .filter(StringUtils::isNotBlank)
                .map(contentValue -> contentValue.split("/").length)
                .orElse(0);
    }
}
