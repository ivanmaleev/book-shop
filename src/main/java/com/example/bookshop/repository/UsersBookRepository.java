package com.example.bookshop.repository;

import com.example.bookshop.entity.UsersBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersBookRepository extends JpaRepository<UsersBook, Long> {

    List<UsersBook> findAllByUserIdAndAndArchived(Long userId, boolean archived);

    @Query(nativeQuery = true,
            value = "SELECT count(*) \n" +
                    "FROM book_shop.users_book AS ub \n" +
                    "WHERE ub.user_id = ?1")
    Integer getCount(Long userId);
}
