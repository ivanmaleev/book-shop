package com.example.bookshop.service;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.data.google.api.books.Item;
import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.dto.AlphabetObject;
import com.example.bookshop.entity.AbstractEntity;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.entity.Genre;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.GenreRepository;
import com.example.bookshop.service.impl.AlphabetService;
import lombok.NoArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.bookshop.constants.Langs.EN;

@NoArgsConstructor
@Component
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "false")
public class LoadGenresService {

    @Value("${google.books.api.key}")
    private String apiKey = "AIzaSyAZ65eejGDeyrCYYm-NLk8vUD-orgqKy4w";

    @Value("${google.books.api.url}")
    private String googleUrl = "www.googleapis.com/books/v1/volumes";

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    private Set<String> genres = new HashSet<>();
    private Set<String> authors = new HashSet<>();
    private Map<Long, List<Author>> authorsMap = new HashMap<>();

    private Set<BookLocal> books = new HashSet<>();

    public void loadGenres() {

        authorsMap = authorRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(AbstractEntity::getId));

        for (int i = 0; i < 1000; i++) {
            Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(getRandomSearchWord(), 0, 40), Root.class).getBody();
            root.getItems()
                    .forEach(item -> {
                        if (Objects.nonNull(item.getVolumeInfo())
                                && Objects.nonNull(item.getVolumeInfo().getCategories())
                                && Objects.nonNull(item.getVolumeInfo().getAuthors())
                                && item.getVolumeInfo().getAuthors().size() > 0) {
                            //genres.addAll(item.getVolumeInfo().getCategories());
                            //authors.addAll(item.getVolumeInfo().getAuthors());
                            BookLocal book = getBook(item);
                            if (Objects.nonNull(book)) {
                                bookRepository.save(book);
                                System.out.println(book);
                            }
                            //books.add(getBook(item));
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
            if (Objects.nonNull(searchWord)) {
                urlString = urlString + "&q=" + searchWord;
            }
            return urlString;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomSearchWord() {
        Random r = new Random();
        List<AlphabetObject> alphabetObjects = AlphabetService.getAlphabet(Langs.RU);
        return String.valueOf(alphabetObjects.get(r.nextInt(alphabetObjects.size())).getLetter());
    }

    private BookLocal getBook(Item item) {
        final Random random = new Random();
        BookLocal book = new BookLocal();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (Objects.nonNull(item.getVolumeInfo())) {

            if (Objects.nonNull(bookRepository.findTopBySlug(item.getId()).get())) {
                return null;
            }
            if (item.getVolumeInfo().getAuthors() != null) {

                List<String> authors1 = item.getVolumeInfo().getAuthors();
                if (Objects.nonNull(authors1) && !authors1.isEmpty()) {
                    Author author = authorRepository.findTopByLastNameLike(authors1.get(0));
                    book.setAuthor(author);
                }
                if (Objects.isNull(book.getAuthor())) {
                    long i = random.nextInt(500);
                    final List<Author> authors2 = authorsMap.get(i);
                    if (authors2 != null) {
                        book.setAuthor(authors2.get(0));
                    }

                    if (Objects.isNull(book.getAuthor())) {
                        return null;
                    }
                }
            }

            if (item.getVolumeInfo().getCategories() != null
                    && !item.getVolumeInfo().getCategories().isEmpty()) {
                String genreName = item.getVolumeInfo().getCategories().get(0);
                Optional<Genre> optGenre = genreRepository.findByName(genreName, EN);
                if (optGenre.isPresent()) {
                    book.setGenre(optGenre.get());
                } else {
                    return null;
                }

            } else {
                return null;
            }

            book.setTitle(item.getVolumeInfo().getTitle());
            book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
            book.setSlug(item.getId());
            book.setId(item.getId());

            try {
                String publishedDate = item.getVolumeInfo().getPublishedDate();
                if (publishedDate != null) {
                    book.setPubDate(LocalDate.parse(publishedDate, dtf));
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            book.setDescription(item.getVolumeInfo().getDescription());
        }
        if (item.getSaleInfo() != null && item.getSaleInfo().

                getRetailPrice() != null) {
            book.setPrice((int) item.getSaleInfo().getRetailPrice().getAmount());
            Double oldPrice = item.getSaleInfo().getListPrice().getAmount();
            book.setPriceOld(oldPrice.intValue());
        }

        int i = random.nextInt(10);
        if (i > 7) {
            book.setIsBestseller(1);
        }
        //book.setId(UUID.randomUUID().toString());
        return book;
    }
}
