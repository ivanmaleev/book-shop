package com.example.bookshop.errs;

import java.util.HashMap;
import java.util.Map;

public class ObjectParserException extends Exception {
    private Map<String, Object> extensions = new HashMap<>();

    public ObjectParserException(String msg){
        super("Parser JSON to object error. '" + msg + "'");
        extensions.put("code", "1");
    }
}
