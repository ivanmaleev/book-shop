package com.example.bookshop.security;

import com.example.bookshop.entity.SmsCode;
import com.example.bookshop.repository.SmsCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public class SmsService {

    private String ACCOUNT_SID;

    private String AUTH_TOKEN;

    private String TWILIO_NUMBER;

    private final SmsCodeRepository smsCodeRepository;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository) {
        this.smsCodeRepository = smsCodeRepository;
    }

    public String sendSecretCodeSms(String contact) {
        String generatedCode = generateCode();
        return generatedCode;
    }

    public String generateCode() {
        //nnn nnn - pattern
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

    public void saveNewCode(SmsCode smsCode) {
        if (Objects.isNull(smsCodeRepository.findByCode(smsCode.getCode()))) {
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        return (Objects.nonNull(smsCode) && !smsCode.isExpired());
    }
}
