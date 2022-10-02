package com.example.bookshop.repository;

import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.entity.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @Query(value = "SELECT new com.example.bookshop.dto.BookCommentDto(bc.id, bc.text, bc.user, bc.date, \n" +
            "sum(case when bcr.rating = 1 then 1 else 0 end), \n" +
            "sum(case when bcr.rating = -1 then 1 else 0 end)) \n" +
            "FROM BookComment AS bc \n" +
            "LEFT JOIN BookCommentRating AS bcr \n" +
            "ON bc = bcr.comment \n" +
            "WHERE bc.bookId = ?1 \n" +
            "GROUP BY bc.id, bc.user, bc.text, bc.date \n" +
            "ORDER BY bc.date"
    )
    List<BookCommentDto> findAllByBookId(String bookId);
}
