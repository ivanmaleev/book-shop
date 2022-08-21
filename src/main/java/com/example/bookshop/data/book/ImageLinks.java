package com.example.bookshop.data.book;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageLinks {
    @JsonProperty("smallThumbnail")
    public String getSmallThumbnail() {
        return this.smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    String smallThumbnail;

    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;

    @JsonProperty("small")
    public String getSmall() {
        return this.small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    String small;

    @JsonProperty("medium")
    public String getMedium() {
        return this.medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    String medium;

    @JsonProperty("large")
    public String getLarge() {
        return this.large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    String large;

    @JsonProperty("extraLarge")
    public String getExtraLarge() {
        return this.extraLarge;
    }

    public void setExtraLarge(String extraLarge) {
        this.extraLarge = extraLarge;
    }

    String extraLarge;
}
