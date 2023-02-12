package com.example.bookshop.controllers;

import com.example.bookshop.data.BooksPageDto;
import com.example.bookshop.data.SearchWordDto;
import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.errs.EmptySearchException;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return commonService.getCommonPageData(request, false);
    }

    @GetMapping(value = {"/search", "/search/{SearchWord}"})
    public String getSearchResultBooks(@PathVariable(value = "SearchWord", required = false) String searchWord,
                                       Model model) throws EmptySearchException {
        if (StringUtils.isNotBlank(searchWord)) {
            model.addAttribute("searchWordDto", new SearchWordDto(searchWord));
            model.addAttribute("searchResultBooks",
                    bookService.getPageOfSearchResult(searchWord, 0, 20));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по пустой строке невозможен");
        }
    }

    @GetMapping("/searchPage/{SearchWord}")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getSearchResultBooksPage(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @PathVariable(value = "SearchWord", required = false) String searchWord) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfSearchResult(searchWord, offset, limit)));
    }
}
