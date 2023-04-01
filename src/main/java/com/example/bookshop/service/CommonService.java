package com.example.bookshop.service;

import com.example.bookshop.dto.CommonPageData;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Интерфейс сервиса управления общими данными страниц
 */
@Service
public interface CommonService {

    /**
     * Возвращает общие данные страниц
     *
     * @param request   Входящий Http запрос
     * @param usersPage Признак пользовательской страницы
     * @return Обюие данные страницы
     */
    CommonPageData getCommonPageData(HttpServletRequest request, boolean usersPage);
}
