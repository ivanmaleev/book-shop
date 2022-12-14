package com.example.bookshop.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("bookRequest")
public class BookRequestRedis {

    @Id
    private String id;
    private List<BookRedis> books;
}
