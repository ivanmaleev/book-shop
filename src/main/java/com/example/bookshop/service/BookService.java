package com.example.bookshop.service;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BookService {

    List<? extends Book> getBooksByAuthor(Author author, Integer offset, Integer limit);

    List<? extends Book> getPageofRecommendedBooks(Integer offset, Integer limit);

    List<? extends Book> getPageOfRecentBooks(Integer offset, Integer limit, Date from, Date end);

    List<? extends Book> getPageOfPopularBooks(Integer offset, Integer limit);

    List<? extends Book> getPageOfSearchResult(String searchWord, Integer offset, Integer limit);

    Book getBook(String slug);

    List<? extends Book> getBooksByGenreId(long genreId, Integer offset, Integer limit);

    void addBooksToUser(List<? extends Book> books, BookstoreUser user, boolean archived);

    List<? extends Book> findUsersBooks(Long userId, boolean archived);
}
