package com.example.bookshop.service;

import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.repository.BookRepository;
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
    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addBooksToUser(List<String> bookSlugList, BookstoreUser user, boolean archived) {
        bookSlugList = validateSlugs(bookSlugList, user, archived);
        if (!bookSlugList.isEmpty()) {
            usersBookRepository.saveAll(bookSlugList.stream()
                    .map(slug -> new UsersBook(user, slug, archived))
                    .collect(Collectors.toList()));
        }
    }

    private List<String> validateSlugs(List<String> bookSlugList, BookstoreUser user, boolean archived) {
        return bookRepository.findAllBySlugIn(bookSlugList)
                .stream()
                .filter(book -> !usersBookRepository.existsByBookIdAndUserAndArchived(book.getSlug(), user, archived))
                .map(BookLocal::getSlug)
                .collect(Collectors.toList());
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
