package com.example.bookshop.repository;

import com.example.bookshop.entity.redis.BookRedis;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

/**
 * Репозиторий кэша книг
 */
public interface BookRedisRepository extends KeyValueRepository<BookRedis, String> {
}
