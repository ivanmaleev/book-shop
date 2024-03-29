package com.example.bookshop.api.google.book;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Epub {
    @JsonProperty("isAvailable")
    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    boolean isAvailable;

    @JsonProperty("acsTokenLink")
    public String getAcsTokenLink() {
        return this.acsTokenLink;
    }

    public void setAcsTokenLink(String acsTokenLink) {
        this.acsTokenLink = acsTokenLink;
    }

    String acsTokenLink;
}
