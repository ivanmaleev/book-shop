package com.example.bookshop.controllers;

import com.example.bookshop.data.ApiResponse;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import com.example.bookshop.errs.BookstoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BooksRestApiController {

    private final BookService bookService;


    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

//    @GetMapping("/books/by-author")
//    public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author")String authorName){
//        return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
//    }

//    @GetMapping("/books/by-title")
//    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title")String title) throws BookstoreApiWrongParameterException {
//        ApiResponse<Book> response = new ApiResponse<>();
//        List<Book> data = bookService.getBooksByTitle(title);
//        response.setDebugMessage("successful request");
//        response.setMessage("data size: "+data.size()+" elements");
//        response.setStatus(HttpStatus.OK);
//        response.setTimeStamp(LocalDateTime.now());
//        response.setData(data);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/books/by-price-range")
//    public ResponseEntity<List<Book>> priceRangeBookss(@RequestParam("min")Integer min, @RequestParam("max")Integer max){
//        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
//    }
//
//    @GetMapping("/books/with-max-discount")
//    public ResponseEntity<List<Book>> maxPriceBooks(){
//        return ResponseEntity.ok(bookService.getBooksWithMaxPrice());
//    }
//
//    @GetMapping("/books/bestsellers")
//    public ResponseEntity<List<Book>> bestSellerBooks(){
//        return ResponseEntity.ok(bookService.getBestsellers());
//    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception){
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Missing required parameters",
                exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception){
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Bad parameter value...",exception)
        ,HttpStatus.BAD_REQUEST);
    }
}
