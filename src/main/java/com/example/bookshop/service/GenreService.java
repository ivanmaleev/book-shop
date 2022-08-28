package com.example.bookshop.service;

import com.example.bookshop.dto.GenreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GenreService {

    /**
     * Возвращает список жанров
     * @param lang - локаль
     * @return - список {@link GenreDto}
     */
    List<GenreDto> findGenres(String lang);

    GenreDto findGenreById(long id, String lang);
}
