package com.example.bookshop.service;

import com.example.bookshop.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public interface AuthorService {

    Map<String, List<Author>> getAuthorsMap();

    Author findById(long id) throws Exception;
}
