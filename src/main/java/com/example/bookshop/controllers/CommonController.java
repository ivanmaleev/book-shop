package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
