package com.example.bookshop.controllers;

import com.example.bookshop.data.BooksPageDto;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.data.ResourceStorage;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.GenreService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
@NoArgsConstructor
public class BooksController {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    @Autowired
    private ResourceStorage storage;
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;


    @GetMapping("/slug/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        Book book = bookService.getBook(slug);
        model.addAttribute("slugBook", book);
        return "/books/slug";
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        return new BooksPageDto(bookService.getPageofRecommendedBooks(offset, limit));
    }

    @GetMapping("/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam(value = "from", required = false) String from,
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
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit, fromDate, toDate));
    }

    @GetMapping("/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                            @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit));
    }

    @GetMapping("/genre/{id}")
    @ResponseBody
    public BooksPageDto getGenreBooksPage(@PathVariable("id") long id,
                                          @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return new BooksPageDto(bookService.getBooksByGenreId(id, offset, limit));
    }

//    @GetMapping("/author/{id}")
//    @ResponseBody
//    public BooksPageDto getAuthorBooksPage(@PathVariable("id") long id,
//                                          @RequestParam(value = "offset", defaultValue = "0") Integer offset,
//                                          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
//        Author author = authorService.findById(id);
//        return new BooksPageDto(bookService.getBooksByAuthor(author, offset, limit));
//    }

    @GetMapping("/author/{id}")
    public String getGenresPage(@PathVariable("id") long id,
                                @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                                Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        model.addAttribute("authorBooks", bookService.getBooksByAuthor(author, offset, limit));
        return "books/author";
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

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
