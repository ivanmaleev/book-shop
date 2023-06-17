package com.example.bookshop.service.impl;

import com.example.bookshop.constants.BookStatus;
import com.example.bookshop.entity.UsersBook;
import com.example.bookshop.repository.UsersBookRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.UsersBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringJUnitConfig(UsersBookServiceImplTest.TestConfig.class)
class UsersBookServiceImplTest {

    @Autowired
    private UsersBookService service;
    @MockBean
    private UsersBookRepository usersBookRepository;

    @Test
    void addBooksToUser() {
        List<String> slugList = List.of("slug");
        BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setId(1L);
        Assertions.assertDoesNotThrow(() -> service.addBooksToUser(slugList, bookstoreUser, BookStatus.ARCHIVED));
    }

    @Test
    void findUsersBooks() {
        List<String> slugs = List.of("slug");
        Long userId = 1L;
        Mockito.when(usersBookRepository.findAllByUserIdAndBookIdInAndArchived(userId, slugs, true)).thenReturn(List.of(new UsersBook()));
        Assertions.assertFalse(service.findUsersBooks(userId, slugs, true).isEmpty());
    }

    @Test
    void getBookStatus() {
        Long userId = 1L;
        String slug = "slug";
        Mockito.when(usersBookRepository.findTopByUserIdAndBookId(userId, slug)).thenReturn(Optional.of(new UsersBook()));
        Assertions.assertNotNull(service.getBookStatus(userId, slug));
    }

    @Test
    void getCount() {
        Mockito.when(usersBookRepository.getCount(Mockito.any())).thenReturn(1);
        Assertions.assertEquals(1, service.getCount(1L));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public UsersBookService usersBookService() {
            return new UsersBookServiceImpl();
        }
    }
}