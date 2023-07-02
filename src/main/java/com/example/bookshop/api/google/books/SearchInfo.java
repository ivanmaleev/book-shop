package com.example.bookshop.api.google.books;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchInfo {
    @JsonProperty("textSnippet")
    public String getTextSnippet() {
        return this.textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }

    String textSnippet;
}
