package com.example.bookshop.service;

import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface CartService {

    void changeBookStatus(BookCartRequest bookCartRequest,
                          HttpServletRequest request,
                          HttpServletResponse response, Model model);
}
