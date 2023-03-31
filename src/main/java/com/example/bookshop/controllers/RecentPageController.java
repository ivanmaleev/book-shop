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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@NoArgsConstructor
@RequestMapping("/recent")
public class RecentPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @GetMapping({"", "/"})
    public String recentBooksPage(Model model) {
        LocalDate fromDate = LocalDate.now().minus(1068, ChronoUnit.DAYS);
        LocalDate toDate = LocalDate.now();
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 20, fromDate, toDate));
        model.addAttribute("topbarActive", new TopBar().setRecentActive());
        return "/books/recent";
    }
}
