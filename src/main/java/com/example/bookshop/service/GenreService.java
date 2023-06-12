package com.example.bookshop.service;

import com.example.bookshop.dto.GenreDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Интерфейс сервиса жанров
 */
@Service
public interface GenreService {

    /**
     * Возвращает список жанров
     *
     * @param lang - локаль
     * @return - список {@link GenreDto}
     */
    Collection<GenreDto> findGenres(String lang);

    /**
     * Возвращает жанр по id
     *
     * @param id   id жанра
     * @param lang Локаль
     * @return Жанр
     */
    GenreDto findGenreById(long id, String lang);
}
