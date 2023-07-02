package com.example.bookshop.controller;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Базовый котроллер с общими данными
 */
@Controller
public class CommonController {

    @Autowired
    private CommonService commonService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, isUsersPage());
    }

    public boolean isUsersPage() {
        return false;
    }
}
