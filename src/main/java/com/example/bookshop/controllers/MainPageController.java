package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@NoArgsConstructor
public class MainPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @GetMapping({"/", "/index"})
    public String mainPage(Model model) {
        model.addAttribute("recommendedBooks", bookService.getPageofRecommendedBooks(0, 6));
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 6,
                LocalDate.now().minus(1068, ChronoUnit.DAYS), LocalDate.now()));
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 6));
        model.addAttribute("topbarActive", new TopBar().setMainActive());
        return "index";
    }
}
