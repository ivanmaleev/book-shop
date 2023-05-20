package com.example.bookshop.service.impl;

import com.example.bookshop.entity.Author;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.service.AuthorService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация сервиса авторов книг
 */
@Service
@NoArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorsRepository;

    /**
     * Возвращает мапу Первая буква фамилии - Автор
     *
     * @return мапа авторов
     */
    @Override
    @Cacheable("authorsMap")
    public Map<String, List<Author>> getAuthorsMap() {
        return authorsRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy((Author a) -> a.getLastName().substring(0, 1)));
    }

    /**
     * Ищет автора по id
     *
     * @param authorId id автора
     * @return Автор
     * @throws Exception Если не найден автор
     */
    @Override
    @Cacheable("author")
    public Author findById(long authorId) throws Exception {
        return authorsRepository.findById(authorId)
                .orElseThrow(() -> new Exception(String.format("%s %s", "Не найден автор с id = ", authorId)));
    }
}
