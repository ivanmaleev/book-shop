package com.example.bookshop.repository;

import com.example.bookshop.entity.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    List<BookComment> findAllByBookId(String bookId);
}
