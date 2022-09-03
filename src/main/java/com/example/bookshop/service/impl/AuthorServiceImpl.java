package com.example.bookshop.service.impl;

import com.example.bookshop.entity.Author;
import com.example.bookshop.repository.AuthorsRepository;
import com.example.bookshop.service.AuthorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorsRepository authorsRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authorList = jdbcTemplate.query("SELECT * FROM book_shop.author", (ResultSet rs, int rownum) -> {
            Author author = new Author();
            author.setId(rs.getLong("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });
        return authorList
                .stream()
                .collect(Collectors.groupingBy((Author a) -> a.getLastName().substring(0, 1)));
    }

    @Override
    public Author findById(long id) {
        return authorsRepository.findById(id);
    }
}
