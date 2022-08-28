package com.example.bookshop.service.impl;

import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Genre;
import com.example.bookshop.repository.GenreRepository;
import com.example.bookshop.service.GenreService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GenreServiceImpl implements GenreService {

    private int max = 10000;
    private int min = 100;
    @Autowired
    private GenreRepository genreRepository;

    @Override
    public List<GenreDto> findGenres(String lang) {


        return genreRepository.findAllByLang(lang)
                .stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName(), genre.getOrder(), rnd(min, max)))
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto findGenreById(long id, String lang) {
        Genre genre = genreRepository.findByLang(id, lang);
        return new GenreDto(genre.getId(), genre.getName(), genre.getOrder(), rnd(min, max));
    }

    //TODO заглушка
    public int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
