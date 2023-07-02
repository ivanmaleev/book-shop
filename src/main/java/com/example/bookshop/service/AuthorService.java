package com.example.bookshop.service;

import com.example.bookshop.dto.AuthorDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс сервиса авторов книг
 */
@Service
public interface AuthorService {

    /**
     * Возвращает мапу Первая буква фамилии - Автор
     *
     * @return мапа авторов
     */
    Map<String, List<AuthorDto>> getAuthorsMap();

    /**
     * Ищет автора по id
     *
     * @param id id автора
     * @return Автор
     * @throws Exception Если не найден автор
     */
    AuthorDto findById(long id) throws Exception;
}
