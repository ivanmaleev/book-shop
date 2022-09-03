package com.example.bookshop.service;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.dto.AlphabetObject;
import com.example.bookshop.service.impl.AlphabetService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@NoArgsConstructor
@Component
public class LoadGenresService {

    @Value("${google.books.api.key}")
    private String apiKey = "AIzaSyAZ65eejGDeyrCYYm-NLk8vUD-orgqKy4w";

    @Value("${google.books.api.url}")
    private String googleUrl = "www.googleapis.com/books/v1/volumes";

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    private Set<String> genres = new HashSet<>();
    private Set<String> authors = new HashSet<>();

    public void loadGenres() {
        for (int i = 0; i < 1000; i++) {
            Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(getRandomSearchWord(), 0, 40), Root.class).getBody();
            root.getItems()
                    .forEach(item -> {
                        if (item.getVolumeInfo() != null
                                && item.getVolumeInfo().getCategories() != null
                                && item.getVolumeInfo().getAuthors() != null
                                && item.getVolumeInfo().getAuthors().size() > 0) {
                            //genres.addAll(item.getVolumeInfo().getCategories());
                            authors.addAll(item.getVolumeInfo().getAuthors());
                        }
                    });
        }

        int a = 0;
    }

    private String getGoogleBooksApiUrl(String searchWord, Integer offset, Integer limit) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(googleUrl);
        //TODo
//        if (searchWord != null) {
//            builder.addParameter("q", searchWord);
//        }
        builder.addParameter("key", apiKey);
        builder.addParameter("filter", "paid-ebooks");
        builder.addParameter("startIndex", offset.toString());
        builder.addParameter("maxResults", limit.toString());
        try {
            URL url = builder.build().toURL();
            String urlString = url.toString();
            //TODo
            if (searchWord != null) {
                urlString = urlString + "&q=" + searchWord;
            }
            return urlString;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomSearchWord() {
        Random r = new Random();
        List<AlphabetObject> alphabetObjects = AlphabetService.alphabetLangMap.get(Langs.RU);
        return String.valueOf(alphabetObjects.get(r.nextInt(alphabetObjects.size())).getLetter());
    }
}
