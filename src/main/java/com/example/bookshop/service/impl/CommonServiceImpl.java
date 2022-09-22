package com.example.bookshop.service.impl;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.service.CommonService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CommonServiceImpl implements CommonService {

    @Override
    public CommonPageData getCommonPageData(HttpServletRequest request) {
        CommonPageData commonPageData = new CommonPageData(0, 0);
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
        return commonPageData;
    }
}
