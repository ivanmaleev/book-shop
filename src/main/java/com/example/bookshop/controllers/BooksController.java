package com.example.bookshop.controllers;

import com.example.bookshop.data.BooksPageDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.dto.request.BookRatingRequest;
import com.example.bookshop.dto.response.BookRatingResponse;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.service.BookCommentService;
import com.example.bookshop.service.BookRatingService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Controller
@RequestMapping("/books")
@NoArgsConstructor
public class BooksController {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    //    @Autowired
//    private ResourceStorage storage;
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BookRatingService bookRatingService;
    @Autowired
    private BookCommentService bookCommentService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @GetMapping("/slug/{slug}")
    //public String bookPage(@ModelAttribute Book slugBook,
    public String bookPage(
            @ModelAttribute BookRatingDto bookRating,
            @PathVariable("slug") String slug, Model model) {
        Book book = bookService.getBook(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("bookRating", bookRatingService.getBookRating(slug));
        model.addAttribute("bookComments", bookCommentService.getBookComments(slug));

        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        if (!currentUser.isAnonymousUser()) {
            return "/books/slugmy";
        }
        return "/books/slug";
    }

    @GetMapping("/recommended")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getRecommendedBooksPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                                @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageofRecommendedBooks(offset, limit)));
    }

    @GetMapping("/recent")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getRecentBooksPage(@RequestParam(value = "from", required = false) String from,
                                                           @RequestParam(value = "to", required = false) String to,
                                                           @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                           @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        Date fromDate = null;
        Date toDate = null;
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            try {
                fromDate = sdf.parse(from);
                toDate = sdf.parse(to);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (fromDate == null) {
            fromDate = Date.from(Instant.now().minus(1068, ChronoUnit.DAYS));
        }
        if (toDate == null) {
            toDate = Date.from(Instant.now());
        }
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit, fromDate, toDate)));
    }

    @GetMapping("/popular")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                            @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit)));
    }

    @GetMapping("/genre/{id}")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getGenreBooksPage(@PathVariable("id") long id,
                                                          @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getBooksByGenreId(id, offset, limit)));
    }

    @GetMapping("/author/{id}")
    public String getAuthorsBooksPage(@PathVariable("id") long id,
                                      @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                      @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                                      Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        model.addAttribute("authorBooks", bookService.getBooksByAuthor(author, offset, limit));
        return "books/author";
    }

    @PostMapping("/rateBook")
    @ResponseBody
    public ResponseEntity<BookRatingResponse> rateBook(@RequestBody BookRatingRequest bookRatingRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        if (!currentUser.isAnonymousUser()) {
            bookRatingService.saveBookRating(currentUser, bookRatingRequest);
            return ResponseEntity.ok(new BookRatingResponse(true));
        }
        return ResponseEntity.ok(new BookRatingResponse(false));
    }

//    @PostMapping("/{slug}/img/save")
//    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
//        String savePath = storage.saveNewBookImage(file, slug);
//        Book bookToUpdate = bookRepository.findBookBySlug(slug);
//        bookToUpdate.setImage(savePath);
//        bookRepository.save(bookToUpdate); //save new path in db here
//
//        return "redirect:/books/" + slug;
//    }

//    @GetMapping("/download/{hash}")
//    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
//        Path path = storage.getBookFilePath(hash);
//        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);
//
//        MediaType mediaType = storage.getBookFileMime(hash);
//        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);
//
//        byte[] data = storage.getBookFileByteArray(hash);
//        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
//                .contentType(mediaType)
//                .contentLength(data.length)
//                .body(new ByteArrayResource(data));
//    }


}
