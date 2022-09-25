package com.example.bookshop.repository;

import com.example.bookshop.entity.redis.BookRequestRedis;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface BookRequestRepository extends KeyValueRepository<BookRequestRedis, String> {
}
