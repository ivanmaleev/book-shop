package com.example.bookshop.service.impl;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.repository.GenreRepository;
import com.example.bookshop.service.GenreService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления статусами книг
 */
@NoArgsConstructor
public class GenreServiceImpl implements GenreService {

    private int max = 10000;
    private int min = 100;

    private Map<String, Map<Long, GenreDto>> langGenreMap = new HashMap<>();
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private EntityManager entityManager;

    /**
     * Возвращает список жанров
     *
     * @param lang - локаль
     * @return - список {@link GenreDto}
     */
    @Override
    public List<GenreDto> findGenres(String lang) {
        return new ArrayList<>(getGenreMap(lang).values());
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
        return getGenreMap(lang).get(id);
    }

    private Map<Long, GenreDto> getGenreMap(String lang) {
        Map<Long, GenreDto> genreMap = langGenreMap.get(lang);
        if (Objects.isNull(genreMap) || genreMap.isEmpty()) {
            loadLangGenreMap();
            genreMap = langGenreMap.get(lang);
        }
        return genreMap;
    }

    private void loadLangGenreMap() {
        List<String> langs = List.of(Langs.RU, Langs.EN);
        langs.forEach(lang -> {
            langGenreMap.putIfAbsent(lang, new HashMap<>());
            Map<Long, GenreDto> genresMap = langGenreMap.get(lang);
            genreRepository.findAllByLang(lang)
                    .forEach(genre ->
                            genresMap.put(genre.getId(),
                                    new GenreDto(genre.getId(), genre.getName(), genre.getOrder(), getBooksCount(genre.getId(), min, max)))
                    );
            //TODO баг хайбернэйта
            entityManager.clear();
        });
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
