package com.example.bookshop.controllers;

import com.example.bookshop.data.BooksPageDto;
import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.dto.BookRatingDto;
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
import com.example.bookshop.service.UsersBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Контроллер страниц книг
 */
@Controller
@RequestMapping("/books")
@NoArgsConstructor
@Api(description = "Контроллер книг")
@Slf4j
public class BooksController extends CommonController {

    @Value("${requests.timout}")
    private Integer timeoutMillis;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Autowired
    private BookService<? extends Book> bookService;
    @Autowired
    private UsersBookService usersBookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookRatingService bookRatingService;
    @Autowired
    private BookCommentService bookCommentService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @ApiOperation("Метод получения страницы книги")
    @ApiResponse(responseCode = "200", description = "Страница книги")
    @GetMapping("/slug/{slug}")
    public String bookPage(
            @ModelAttribute BookRatingDto bookRating,
            @PathVariable("slug") String slug, Model model) throws Exception {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();

        CompletableFuture<Book> slugBookFuture = CompletableFuture.supplyAsync(() -> {
            Book book = null;
            try {
                book = bookService.getBook(slug);
                book.setStatus(Objects.toString(usersBookService.getBookStatus(currentUser.getId(), slug)));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return book;
        });
        CompletableFuture<BookRatingDto> bookRatingFuture = CompletableFuture.supplyAsync(() -> bookRatingService.getBookRating(slug));
        CompletableFuture<Collection<BookCommentDto>> bookCommentsFuture = CompletableFuture.supplyAsync(() -> bookCommentService.getBookComments(slug));
        model.addAttribute("slugBook", slugBookFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("bookRating", bookRatingFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("bookComments", bookCommentsFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));

        if (!currentUser.isAnonymousUser()) {
            return "/books/slugmy";
        }
        return "/books/slug";
    }

    @ApiOperation("Метод получения списка рекоммендованных книг с пагинацией")
    @ApiResponse(responseCode = "200",
            description = "Список книг")
    @GetMapping("/recommended")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getRecommendedBooksPage(@Min(value = 0) @Max(value = 10000) @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                                @Min(value = 1) @Max(value = 20) @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit)));
    }

    @ApiOperation("Метод получения списка недавних книг с пагинацией")
    @ApiResponse(responseCode = "200",
            description = "Список книг")
    @GetMapping("/recent")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getRecentBooksPage(@RequestParam(value = "from", required = false) String from,
                                                           @RequestParam(value = "to", required = false) String to,
                                                           @Min(value = 0) @Max(value = 10000) @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                           @Min(value = 1) @Max(value = 20) @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        LocalDate fromDate = StringUtils.isNotBlank(from) ? LocalDate.parse(from, dtf) : LocalDate.now().minus(3, ChronoUnit.YEARS);
        LocalDate toDate = StringUtils.isNotBlank(to) ? LocalDate.parse(to, dtf) : LocalDate.now();
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit, fromDate, toDate)));
    }

    @ApiOperation("Метод получения списка популярных книг с пагинацией")
    @ApiResponse(responseCode = "200",
            description = "Список книг")
    @GetMapping("/popular")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getPopularBooksPage(@Min(value = 0) @Max(value = 10000) @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                            @Min(value = 1) @Max(value = 20) @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit)));
    }

    @ApiOperation("Метод получения списка книг по жанру")
    @ApiResponse(responseCode = "200",
            description = "Список книг")
    @GetMapping("/genre/{id}")
    @ResponseBody
    public ResponseEntity<BooksPageDto> getGenreBooksPage(@Min(value = 1) @PathVariable("id") long genreId,
                                                          @Min(value = 0) @Max(value = 10000) @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                          @Min(value = 1) @Max(value = 20) @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(new BooksPageDto(bookService.getBooksByGenreId(genreId, offset, limit)));
    }

    @ApiOperation("Метод получения списка книг по автору")
    @ApiResponse(responseCode = "200",
            description = "Список книг")
    @GetMapping("/author/{id}")
    public String getAuthorsBooksPage(@Min(value = 1) @PathVariable("id") long authorId,
                                      @Min(value = 0) @Max(value = 10000) @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                      @Min(value = 1) @Max(value = 20) @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                                      Model model) throws Exception {
        CompletableFuture<Author> authorFuture = CompletableFuture.supplyAsync(() -> {
            Author author = null;
            try {
                author = authorService.findById(authorId);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return author;
        });
        CompletableFuture<Collection<? extends Book>> authorBooksFuture = authorFuture.
                thenApplyAsync(author -> bookService.getBooksByAuthor(author, offset, limit));
        model.addAttribute("author", authorFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("authorBooks", authorBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        return "books/author";
    }

    @ApiOperation("Сохранить рейтинг книги")
    @ApiResponse(responseCode = "200",
            description = "Результат изменения рейтинга книги")
    @PostMapping("/rateBook")
    @ResponseBody
    public ResponseEntity<BookRatingResponse> rateBook(@Valid @RequestBody BookRatingRequest bookRatingRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        boolean result = false;
        if (!currentUser.isAnonymousUser()) {
            CompletableFuture.runAsync(() -> bookRatingService.saveBookRating(currentUser, bookRatingRequest));
            result = true;
        }
        return ResponseEntity.ok(new BookRatingResponse(result));
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
