package com.example.bookshop.service.impl;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.dto.AlphabetObject;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.entity.redis.BookRedis;
import com.example.bookshop.entity.redis.BookRequestRedis;
import com.example.bookshop.repository.BookRedisRepository;
import com.example.bookshop.repository.BookRequestRepository;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.GenreService;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@NoArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    @Value("${google.books.api.attempts}")
    private Integer attempts;
    @Value("${google.books.api.key}")
    private String apiKey;
    @Value("${google.books.api.url}")
    private String googleUrl;
    @Value("${locale.default}")
    private String defaultLocale;
    @Autowired
    private GenreService genreService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BookRedisRepository bookRedisRepository;
    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Override
    public List<Book> getBooksByAuthor(Author author, Integer offset, Integer limit) {
        String searchString = "+inauthor:" + author.getLastName() + " " + author.getFirstName();
        return getBooks(searchString, null, null, offset, limit);
    }

    @Override
    public List<Book> getPageofRecommendedBooks(Integer offset, Integer limit) {
        return getBooks(null, null, null, offset, limit);
    }

    @Override
    public List<Book> getPageOfRecentBooks(Integer offset, Integer limit, Date fromDate, Date endDate) {
        String from = sdf.format(fromDate);
        String to = sdf.format(endDate);
        return getBooks(null, from, to, offset, limit);
    }

    @Override
    public List<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        return getBooks(null, null, null, offset, limit);
    }

    @Override
    public List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit) {
        return getBooks(searchWord, null, null, offset, limit);
    }

    @Override
    public Book getBook(String slug) {
        Optional<BookRedis> bookRedisOptional = bookRedisRepository.findById(slug);
        if (bookRedisOptional.isPresent()) {
            return new Book(bookRedisOptional.get());
        }
        com.example.bookshop.data.book.Root root = restTemplate.getForEntity(getGoogleBookApiUrl(slug), com.example.bookshop.data.book.Root.class).getBody();
        return getBookFromGoogleRoot(root);
    }

    @Override
    public List<Book> getBooksByGenreId(long genreId, Integer offset, Integer limit) {
        GenreDto genreDto = genreService.findGenreById(genreId, Langs.EN);
        String searchString = "+subject:\"" + genreDto.getName() + "\"";
        return getBooks(searchString, null, null, offset, limit);
    }

    private String getGoogleBookApiUrl(String slug) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(googleUrl + "/" + slug);
        builder.addParameter("key", apiKey);
        try {
            URL url = builder.build().toURL();
            return url.toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String getGoogleBooksApiUrl(String searchWord, String from, String to, Integer offset, Integer limit) {
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
        builder.addParameter("projection", "lite");
        //TODO
        builder.addParameter("langRestrict", Langs.RU);
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            builder.addParameter("tbs", "cdr:1,cd_min:" + from + ",cd_max:" + to);
        }
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

    private List<Book> getBooksFromGoogleRoot(Root root) {
        ArrayList<Book> books = new ArrayList<>();
        List<String> statuses = List.of("PAID", "CART", "KEPT", "");
        final Random random = new Random();
        if (root != null && root.getItems() != null) {
            root.getItems()
                    .forEach(item -> {
                        Book book = new Book();
                        if (item.getVolumeInfo() != null) {
                            if (item.getVolumeInfo().getAuthors() != null) {
                                book.setAuthor(new Author(item.getVolumeInfo().getAuthors()));
                            }
                            book.setTitle(item.getVolumeInfo().getTitle());
                            book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
                            book.setSlug(item.getId());
                            book.setId(item.getId());
                        }
                        if (item.getSaleInfo() != null && item.getSaleInfo().getRetailPrice() != null) {
                            book.setPrice((int) item.getSaleInfo().getRetailPrice().getAmount());
                            Double oldPrice = item.getSaleInfo().getListPrice().getAmount();
                            book.setPriceOld(oldPrice.intValue());
                        }
                        //TODO бестселлер и стутус книги
                        int i = random.nextInt(10);
                        if (i > 7) {
                            book.setIsBestseller(1);
                        }
                        book.setStatus(statuses.get(random.nextInt(4)));
                        log.debug("Load book " + book);
                        books.add(book);
                    });
        }
        return books;
    }

    private Book getBookFromGoogleRoot(com.example.bookshop.data.book.Root root) {
        Book book = new Book();
        if (root != null) {
            if (root.getVolumeInfo() != null) {
                book.setAuthor(new Author(root.getVolumeInfo().getAuthors()));
                book.setTitle(root.getVolumeInfo().getTitle());
                book.setImage(root.getVolumeInfo().getImageLinks().getThumbnail());
                book.setSlug(root.getId());
                book.setId(root.getId());
            }
            if (root.getSaleInfo() != null) {
                book.setPrice((int) root.getSaleInfo().getRetailPrice().getAmount());
                Double oldPrice = root.getSaleInfo().getListPrice().getAmount();
                book.setPriceOld(oldPrice.intValue());
            }
        }
        bookRedisRepository.save(new BookRedis(book));
        log.debug("Load book " + book);
        return book;
    }

    private String getRandomSearchWord(List<AlphabetObject> alphabetObjects) {
        Random r = new Random();
        int i = r.nextInt(alphabetObjects.size());
        final char letter = alphabetObjects.get(i).getLetter();
        alphabetObjects.remove(i);
        return String.valueOf(letter);
    }

    private List<Book> getBooks(String searchString, String from, String to, Integer offset, Integer limit) {
        Root root = null;
        List<AlphabetObject> alphabetObjects = AlphabetService.getAlphabet(defaultLocale);
        String requestUrl = "";
        for (int i = 0; i < attempts; i++) {
            String searchWord = "";
            if (searchString != null) {
                searchWord = getRandomSearchWord(alphabetObjects) + searchString;
            } else {
                searchWord = getRandomSearchWord(alphabetObjects);
            }
            requestUrl = getGoogleBooksApiUrl(searchWord, from, to, offset, limit);
            Optional<BookRequestRedis> bookRequestRedisOptional = bookRequestRepository.findById(requestUrl);
            if (bookRequestRedisOptional.isPresent()) {
                return bookRequestRedisOptional.get().getBooks()
                        .stream()
                        .map(Book::new)
                        .collect(Collectors.toList());
            }
            root = restTemplate.getForEntity(requestUrl, Root.class).getBody();
            if (root != null && root.getItems() != null) {
                break;
            }
        }
        List<Book> books = getBooksFromGoogleRoot(root);
        if (!"".equals(requestUrl)) {
            bookRequestRepository.save(new BookRequestRedis(requestUrl, books
                    .stream()
                    .map(BookRedis::new)
                    .collect(Collectors.toList())));
        }
        return books;
    }

}
