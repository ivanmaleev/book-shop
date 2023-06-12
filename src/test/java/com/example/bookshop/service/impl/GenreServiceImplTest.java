package com.example.bookshop.service.impl;

import com.example.bookshop.repository.GenreRepository;
import com.example.bookshop.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.persistence.EntityManager;

@ActiveProfiles("test")
@SpringJUnitConfig(GenreServiceImplTest.TestConfig.class)
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private EntityManager entityManager;


    @Test
    void findGenres() {

    }

    @Test
    void findGenreById() {
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public GenreService genreService() {
            return new GenreServiceImpl();
        }
    }
}