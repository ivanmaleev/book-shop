package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.PostponedService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PostponedPageController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private PostponedService postponedService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @GetMapping("/books/postponed")
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                Model model) {
        if (StringUtils.isBlank(postponedContents)) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            model.addAttribute("bookPostponed", postponedService.getPostponedBooks(postponedContents));
        }
        return "/postponed";
    }
}
