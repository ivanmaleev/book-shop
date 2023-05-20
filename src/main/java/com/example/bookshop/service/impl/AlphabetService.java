package com.example.bookshop.service.impl;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.dto.AlphabetObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AlphabetService {

    private AlphabetService() {
    }

    protected static final Map<String, List<AlphabetObject>> alphabetLangMap;

    static {
        alphabetLangMap = new HashMap<>();
        List<String> langs = List.of(Langs.RU, Langs.EN);
        langs.forEach(lang -> {
            List<AlphabetObject> alphabet;
            String alphabetString = "";
            if (Langs.RU.equals(lang)) {
                //alphabetString = "абвгдеёжзийклмнопрстуфхцчшщэюя";
                alphabetString = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЭЮЯ";
            } else {
                alphabetString = "abcdefghijklmnopqrstuvwxwz";
            }
            AtomicInteger idAtomic = new AtomicInteger(0);
            alphabet = alphabetString
                    .chars()
                    .mapToObj(ch -> new AlphabetObject(idAtomic.incrementAndGet(), (char) ch, String.valueOf((char) ch)))
                    .collect(Collectors.toList());
            alphabetLangMap.putIfAbsent(lang, alphabet);
        });
    }

    public static List<AlphabetObject> getAlphabet(String lang) {
        return new ArrayList<>(alphabetLangMap.get(lang));
    }
}
