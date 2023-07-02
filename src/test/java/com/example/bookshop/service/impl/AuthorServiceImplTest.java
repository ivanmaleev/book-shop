package com.example.bookshop.service.impl;

import com.example.bookshop.entity.Author;
import com.example.bookshop.mapper.AuthorMapper;
import com.example.bookshop.mapper.BookLocalMapper;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.service.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
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
@SpringJUnitConfig(AuthorServiceImplTest.TestConfig.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorMapper authorMapper;
    @MockBean
    private AuthorRepository authorsRepository;

    @Test
    void whenGetAuthorsMap() {
        Author author = new Author();
        author.setLastName("Abbbb");
        Mockito.when(authorsRepository.findAll()).thenReturn(List.of(author));
        Assertions.assertNotNull(authorService.getAuthorsMap().get("A"));
    }

    @Test
    void whenFindById() throws Exception {
        Author author = new Author();
        author.setLastName("Abbbb");
        Mockito.when(authorsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
        Assertions.assertNotNull(authorService.findById(1L));
    }

    @Test
    void whenFindByIdThrows() {
        Mockito.when(authorsRepository.findById(Mockito.anyLong())).thenReturn(null);
        Assertions.assertThrows(Exception.class, () -> authorService.findById(1L));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public AuthorService authorService() {
            return new AuthorServiceImpl();
        }

        @Bean
        public AuthorMapper authorMapper(){
            return Mappers.getMapper(AuthorMapper.class);
        };
    }
}