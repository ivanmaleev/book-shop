package com.example.bookshop.service.impl;

import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.mapper.GenreMapper;
import com.example.bookshop.repository.GenreRepository;
import com.example.bookshop.service.GenreService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления статусами книг
 */
@NoArgsConstructor
public class GenreServiceImpl implements GenreService {

    private int max = 10000;
    private int min = 100;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    /**
     * Возвращает список жанров
     *
     * @param lang - локаль
     * @return - список {@link GenreDto}
     */
    @Override
    public List<GenreDto> findGenres(String lang) {
        return genreRepository.findAllByLang(lang)
                .stream()
                .map(genre -> {
                    GenreDto genreDto = genreMapper.toDto(genre);
                    genreDto.setCount(getBooksCount(genre.getId(), min, max));
                    return genreDto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Возвращает жанр по id
     *
     * @param id   id жанра
     * @param lang Локаль
     * @return Жанр
     */
    @Override
    public GenreDto findGenreById(long id, String lang) {
        return genreRepository.findByIdAndLang(lang, id)
                .map(genre -> {
                    GenreDto genreDto = genreMapper.toDto(genre);
                    genreDto.setCount(getBooksCount(genre.getId(), min, max));
                    return genreDto;
                })
                .orElse(new GenreDto());
    }

    private int getBooksCount(long id, int min, int max) {
        return rnd(min, max);
    }

    //TODO заглушка
    public int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
