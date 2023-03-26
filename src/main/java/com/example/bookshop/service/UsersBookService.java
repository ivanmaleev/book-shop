package com.example.bookshop.service;

import com.example.bookshop.constants.BookStatus;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersBookService {

    void addBooksToUser(List<String> bookSlugList, BookstoreUser user, BookStatus bookStatus);

    List<UsersBook> findUsersBooks(Long userId, List<String> bookSlugList, Boolean archived);

    BookStatus getBookStatus(Long userId, String bookSlug);

    int getCount(Long userId);
}
