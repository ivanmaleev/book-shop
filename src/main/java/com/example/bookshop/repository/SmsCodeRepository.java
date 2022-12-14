package com.example.bookshop.repository;

import com.example.bookshop.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsCodeRepository extends JpaRepository<SmsCode,Long> {

    public SmsCode findByCode(String code);
}
