package com.example.bookshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TopBar {
    private boolean mainActive = false;
    private boolean genresActive = false;
    private boolean recentActive = false;
    private boolean popularActive = false;
    private boolean authorsActive = false;

    public TopBar setMainActive() {
        this.mainActive = true;
        this.genresActive = false;
        this.recentActive = false;
        this.popularActive = false;
        this.authorsActive = false;
        return this;
    }

    public TopBar setGenresActive() {
        this.mainActive = false;
        this.genresActive = true;
        this.recentActive = false;
        this.popularActive = false;
        this.authorsActive = false;
        return this;
    }

    public TopBar setRecentActive() {
        this.mainActive = false;
        this.genresActive = false;
        this.recentActive = true;
        this.popularActive = false;
        this.authorsActive = false;
        return this;
    }

    public TopBar setPopularActive() {
        this.mainActive = false;
        this.genresActive = false;
        this.recentActive = false;
        this.popularActive = true;
        this.authorsActive = false;
        return this;
    }

    public TopBar setAuthorsActive() {
        this.mainActive = false;
        this.genresActive = false;
        this.recentActive = false;
        this.popularActive = false;
        this.authorsActive = true;
        return this;
    }
}
