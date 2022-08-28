package com.example.bookshop.service;

import com.example.bookshop.data.google.api.books.Root;
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

    public void loadGenres() {
        for (int i = 0; i < 1000; i++) {
            Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(getRandomSearchWord(), 0, 40), Root.class).getBody();
            root.getItems()
                    .forEach(item -> {
                        if (item.getVolumeInfo() != null && item.getVolumeInfo().getCategories() != null) {
                            genres.addAll(item.getVolumeInfo().getCategories());
                        }
                    });
        }

        int a = 0;
    }

    private String getGoogleBooksApiUrl(String searchWord, Integer offset, Integer limit) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(googleUrl);
        if (searchWord != null) {
            builder.addParameter("q", searchWord);
        }
        builder.addParameter("key", apiKey);
        builder.addParameter("filter", "paid-ebooks");
        builder.addParameter("startIndex", offset.toString());
        builder.addParameter("maxResults", limit.toString());
        try {
            URL url = builder.build().toURL();
            return url.toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomSearchWord() {
        //TODO переделать
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщыэюя";
        Random r = new Random();
        char c = alphabet.charAt(r.nextInt(alphabet.length()));
        return String.valueOf(c);
    }
}
