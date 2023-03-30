package com.example.bookshop.service.impl;

import com.example.bookshop.constants.BookStatus;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.UsersBookRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.UsersBookService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class UsersBookServiceImpl implements UsersBookService {

    @Autowired
    private UsersBookRepository usersBookRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    @Override
    public void addBooksToUser(List<String> bookSlugList, BookstoreUser user, BookStatus bookStatus) {
        Map<String, UsersBook> usersBooksMap = findUsersBooks(user.getId(), bookSlugList, null)
                .stream()
                .collect(Collectors.toMap(UsersBook::getBookId, Function.identity(), BinaryOperator.maxBy(Comparator.comparingInt(UsersBook::hashCode))));
        if (Objects.equals(BookStatus.ARCHIVED, bookStatus)) {
            List<UsersBook> usersBooks = bookSlugList
                    .stream()
                    .map(bookSlug -> {
                        UsersBook usersBook = usersBooksMap.get(bookSlug);
                        if (Objects.isNull(usersBook)) {
                            usersBook = new UsersBook();
                            usersBook.setArchived(true);
                            usersBook.setBookId(bookSlug);
                            usersBook.setUser(user);
                        } else {
                            usersBook.setArchived(!usersBook.isArchived());
                        }
                        return usersBook;
                    }).collect(Collectors.toList());
            usersBookRepository.saveAll(usersBooks);
        }
    }

    @Override
    public List<UsersBook> findUsersBooks(Long userId, List<String> bookSlugList, Boolean archived) {
        if (Objects.nonNull(bookSlugList) && !bookSlugList.isEmpty()) {
            if (Objects.nonNull(archived)) {
                return usersBookRepository.findAllByUserIdAndBookIdInAndArchived(userId, bookSlugList, archived);
            }
            return usersBookRepository.findAllByUserIdAndBookIdIn(userId, bookSlugList);
        }
        if (Objects.nonNull(archived)) {
            return usersBookRepository.findAllByUserIdAndAndArchived(userId, archived);
        }
        return usersBookRepository.findAllByUserId(userId);
    }

    @Override
    public BookStatus getBookStatus(Long userId, String bookSlug) {
        return usersBookRepository.findTopByUserIdAndBookId(userId, bookSlug)
                .map(usersBook -> {
                    if (usersBook.isArchived()) {
                        return BookStatus.ARCHIVED;
                    }
                    return BookStatus.KEPT;
                }).orElse(null);
    }

    @Override
    public int getCount(Long userId) {
        return usersBookRepository.getCount(userId);
    }
}
