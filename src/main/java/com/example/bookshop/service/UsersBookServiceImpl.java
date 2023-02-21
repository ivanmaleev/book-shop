package com.example.bookshop.service;

import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.repository.UsersBookRepository;
import com.example.bookshop.security.BookstoreUser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class UsersBookServiceImpl implements UsersBookService {

    @Autowired
    private UsersBookRepository usersBookRepository;

    @Override
    public void addBooksToUser(List<? extends Book> books, BookstoreUser user, boolean archived) {
        usersBookRepository.saveAll(books.stream()
                .map(book -> new UsersBook(user, book.getSlug(), archived))
                .collect(Collectors.toList()));
    }

    @Override
    public List<UsersBook> findUsersBooks(Long userId, boolean archived) {
        return usersBookRepository.findAllByUserIdAndAndArchived(userId, archived);
    }

    @Override
    public int getCount(Long userId) {
        return usersBookRepository.getCount(userId);
    }
}
