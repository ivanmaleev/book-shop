package com.example.bookshop.service;

import com.example.bookshop.entity.Book;
import com.example.bookshop.errs.BookstoreApiWrongParameterException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BookService {

    List<Book> getBooksByAuthor(String authorName);

    List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException;

    List<Book> getPageofRecommendedBooks(Integer offset, Integer limit);

    List<Book> getPageOfRecentBooks(Integer offset, Integer limit, Date from, Date end);

    List<Book> getPageOfPopularBooks(Integer offset, Integer limit);

    List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit);

    Book getBook(String slug);

    List<Book> getBooksByGenre(String genreName, Integer offset, Integer limit);
}
