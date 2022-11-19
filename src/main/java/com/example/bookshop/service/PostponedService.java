package com.example.bookshop.service;

import com.example.bookshop.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostponedService {

    List<BookDto> getPostponedBooks(String postponedContents);
}
