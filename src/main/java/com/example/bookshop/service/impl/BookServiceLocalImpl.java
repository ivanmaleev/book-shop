package com.example.bookshop.service.impl;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Genre;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.UsersBookService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "false")
public class BookServiceLocalImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UsersBookService usersBookService;

    @Override
    public List<? extends Book> getBooksByAuthor(Author author, Integer offset, Integer limit) {
        return bookRepository.findAllByAuthor(author, PageRequest.of(offset, limit)).getContent();
    }

    @Override
    public List<? extends Book> getPageofRecommendedBooks(Integer offset, Integer limit) {
        return bookRepository.findAllByIsBestseller(1, PageRequest.of(offset, limit)).getContent();
    }

    @Override
    public List<? extends Book> getPageOfRecentBooks(Integer offset, Integer limit, Date from, Date end) {
        return bookRepository.findAllByPubDateBetween(from, end, PageRequest.of(offset, limit)).getContent();
    }

    @Override
    public List<? extends Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        return bookRepository.findAllByIsBestseller(1, PageRequest.of(offset, limit)).getContent();
    }

    @Override
    public List<? extends Book> getPageOfSearchResult(String searchWord, Integer offset, Integer limit) {
        return Collections.emptyList();
    }

    @Override
    public Book getBook(String slug) {
        return bookRepository.findTopBySlug(slug);
    }

    @Override
    public List<? extends Book> getBooksByGenreId(long genreId, Integer offset, Integer limit) {
        Genre genre = new Genre();
        genre.setId(genreId);
        return bookRepository.findAllByGenre(genre, PageRequest.of(offset, limit)).getContent();
    }

    @Override
    public void addBooksToUser(List<? extends Book> books, BookstoreUser user, boolean archived) {
        usersBookService.addBooksToUser(books, user, archived);
    }

    @Override
    public List<? extends Book> findUsersBooks(Long userId, boolean archived) {
        List<String> bookIds = usersBookService.findUsersBooks(userId, archived)
                .stream()
                .map(UsersBook::getBookId)
                .collect(Collectors.toList());
        return bookRepository.findAllBySlugIn(bookIds);
    }
}
