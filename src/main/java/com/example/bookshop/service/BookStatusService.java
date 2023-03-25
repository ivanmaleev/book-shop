package com.example.bookshop.service;

import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
public interface BookStatusService {

    void changeBookStatus(BookCartRequest bookCartRequest, HttpServletRequest request,
                          HttpServletResponse response, Model model);

    void acceptRequestBookIdsToCookie(String cookieName, BookCartRequest bookCartRequest, Cookie[] cookies,
                                      HttpServletResponse response, Model model,
                                      BiConsumer<Set<String>, List<String>> resultBookIdsConsumer);
}
