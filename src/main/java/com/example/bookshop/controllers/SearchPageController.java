package com.example.bookshop.controllers;

import com.example.bookshop.data.BooksPageDto;
import com.example.bookshop.data.SearchWordDto;
import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.errs.EmptySearchException;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@NoArgsConstructor
public class SearchPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;
    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request);
    }

    @GetMapping(value = {"/search", "/search/{SearchWord}"})
    public String getSearchResultBooks(@PathVariable(value = "SearchWord", required = false) String searchWord,
                                       Model model) throws EmptySearchException {
        if (searchWord != null) {
            model.addAttribute("searchWordDto", new SearchWordDto(searchWord));
            model.addAttribute("searchResultBooks",
                    bookService.getPageOfGoogleBooksApiSearchResult(searchWord, 0, 20));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }
    }

    @GetMapping("/searchPage/{SearchWord}")
    @ResponseBody
    public BooksPageDto getSearchResultBooksPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                 @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                                 @PathVariable(value = "SearchWord", required = false) String searchWord) {
        return new BooksPageDto(bookService.getPageOfGoogleBooksApiSearchResult(searchWord, offset, limit));
    }
}
