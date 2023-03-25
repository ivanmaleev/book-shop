package com.example.bookshop.service;

import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersBookService {

    void addBooksToUser(List<String> bookSlugList, BookstoreUser user, boolean archived);

    List<UsersBook> findUsersBooks(Long userId, boolean archived);

    int getCount(Long userId);
}
