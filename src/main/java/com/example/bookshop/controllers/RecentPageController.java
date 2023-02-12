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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
        Date fromDate = Date.from(Instant.now().minus(1068, ChronoUnit.DAYS));
        Date toDate = Date.from(Instant.now());
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 20, fromDate, toDate));
        model.addAttribute("topbarActive", new TopBar(false, false, true, false, false));
        return "/books/recent";
    }
}
