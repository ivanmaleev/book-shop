package com.example.bookshop.service.impl;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.data.book.SaleInfo;
import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.dto.AlphabetObject;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookGoogleApi;
import com.example.bookshop.entity.redis.BookRedis;
import com.example.bookshop.entity.redis.BookRequestRedis;
import com.example.bookshop.repository.BookRedisRepository;
import com.example.bookshop.repository.BookRequestRepository;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.GenreService;
import com.example.bookshop.service.UsersBookService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Реализация сервиса книг через Google API
 */
@NoArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "google.books.api.enable", havingValue = "true")
public class BookServiceGoogleApiImpl implements BookService<BookGoogleApi> {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
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
    @Autowired
    private UsersBookService usersBookService;

    /**
     * Возвращает страницу книг автора
     *
     * @param author Автор
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getBooksByAuthor(Author author, Integer offset, Integer limit) {
        String searchString = String.format("%s\"%s %s\"", "+inauthor:", author.getLastName(), author.getFirstName());
        return getBooks(searchString, null, null, offset, limit);
    }

    /**
     * Возвращает страницу рекомендованных книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        return getBooks(null, null, null, offset, limit);
    }

    /**
     * Возвращает страницу недавних книг
     *
     * @param offset   Оффсет для страницы
     * @param limit    Лимит для страницы
     * @param fromDate Начало периода поиска
     * @param endDate  Конец периода поиска
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getPageOfRecentBooks(Integer offset, Integer limit, LocalDate fromDate, LocalDate endDate) {
        return getBooks(null, fromDate.format(dtf), endDate.format(dtf), offset, limit);
    }

    /**
     * Возвращает страницу популярных книг
     *
     * @param offset Оффсет для страницы
     * @param limit  Лимит для страницы
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getPageOfPopularBooks(Integer offset, Integer limit) {
        return getBooks(null, null, null, offset, limit);
    }

    /**
     * Возвращает страницу книг по поиску слова
     *
     * @param searchWord Слово для поиска
     * @param offset     Оффсет для страницы
     * @param limit      Лимит для страницы
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getPageOfSearchResult(String searchWord, Integer offset, Integer limit) {
        return getBooks(searchWord, null, null, offset, limit);
    }

    /**
     * Возвращает книгу по идентификатору книги
     *
     * @param slug Идентификатор кинги
     * @return Книга
     * @throws Exception Если книга не найдена
     */
    @Override
    public BookGoogleApi getBook(String slug) {
        return bookRedisRepository.findById(slug)
                .map(BookGoogleApi::new)
                .orElseGet(() -> {
                    com.example.bookshop.data.book.Root root =
                            restTemplate.getForEntity(getGoogleBookApiUrl(slug), com.example.bookshop.data.book.Root.class).getBody();
                    return (BookGoogleApi) getBookFromGoogleRoot(root);
                });
    }

    /**
     * Возвращает список книг по списку идентификаторов
     *
     * @param slugList Список идентификаторов
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getBooks(Collection<String> slugList) {
        if (Objects.isNull(slugList)) {
            return Collections.emptyList();
        }
        return slugList
                .parallelStream()
                .map(this::getBook)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает страницу книг по жанру
     *
     * @param genreId id жанра
     * @param offset  Оффсет для страницы
     * @param limit   Лимит для страницы
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> getBooksByGenreId(long genreId, Integer offset, Integer limit) {
        GenreDto genreDto = genreService.findGenreById(genreId, Langs.EN);
        String searchString = String.format("%s\"%s\"", "+subject:", genreDto.getName());
        return getBooks(searchString, null, null, offset, limit);
    }

    /**
     * Вовзращает список книг пользователя
     *
     * @param userId   Пользователь
     * @param archived Флаг указания на то, что кнгиа находится в архиве пользователя
     * @return Список книг
     */
    @Override
    public Collection<BookGoogleApi> findUsersBooks(Long userId, boolean archived) {
        return usersBookService.findUsersBooks(userId, Collections.emptyList(), archived)
                .parallelStream()
                .map(usersBook -> getBook(usersBook.getBookId()))
                .collect(Collectors.toList());
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
            if (Objects.nonNull(searchWord)) {
                urlString = urlString + "&q=" + searchWord;
            }
            return urlString;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<BookGoogleApi> getBooksFromGoogleRoot(Root root) {
        ArrayList<BookGoogleApi> books = new ArrayList<>();
        List<String> statuses = List.of("PAID", "CART", "KEPT", "");
        final Random random = new Random();
        Optional.ofNullable(root)
                .map(Root::getItems)
                .ifPresent(itemsNotNull ->
                        itemsNotNull.forEach(item -> {
                            BookGoogleApi book = new BookGoogleApi();

                            book.setSlug(item.getId());
                            book.setId(item.getId());
                            Optional.ofNullable(item.getVolumeInfo())
                                    .ifPresent(volumeInfoNotNull -> {
                                        book.setAuthor(new Author(volumeInfoNotNull.getAuthors()));
                                        book.setTitle(volumeInfoNotNull.getTitle());
                                        book.setImage(volumeInfoNotNull.getImageLinks().getThumbnail());
                                    });
                            Optional<com.example.bookshop.data.google.api.books.SaleInfo> saleInfo = Optional.ofNullable(item.getSaleInfo());
                            book.setPrice(saleInfo.map(com.example.bookshop.data.google.api.books.SaleInfo::getRetailPrice)
                                    .map(retailPrice -> (int) retailPrice.getAmount()).orElse(0));
                            book.setPriceOld(saleInfo.map(com.example.bookshop.data.google.api.books.SaleInfo::getListPrice)
                                    .map(retailPrice -> (int) retailPrice.getAmount()).orElse(0));
                            //TODO бестселлер и стутус книги
                            int i = random.nextInt(10);
                            if (i > 7) {
                                book.setIsBestseller(1);
                            }
                            book.setStatus(statuses.get(random.nextInt(4)));
                            log.debug("Load book " + book);
                            books.add(book);
                        }));
        return books;
    }

    private Book getBookFromGoogleRoot(com.example.bookshop.data.book.Root root) {
        Book book = new BookGoogleApi();

        Optional.ofNullable(root)
                .ifPresent(rootNotNull -> {
                    book.setSlug(rootNotNull.getId());
                    book.setId(rootNotNull.getId());
                    Optional.ofNullable(rootNotNull.getVolumeInfo())
                            .ifPresent(volumeInfoNotNull -> {
                                book.setAuthor(new Author(volumeInfoNotNull.getAuthors()));
                                book.setTitle(volumeInfoNotNull.getTitle());
                                book.setImage(volumeInfoNotNull.getImageLinks().getThumbnail());
                            });
                    Optional<SaleInfo> saleInfo = Optional.ofNullable(rootNotNull.getSaleInfo());
                    book.setPrice(saleInfo.map(SaleInfo::getRetailPrice).map(retailPrice -> (int) retailPrice.getAmount()).orElse(0));
                    book.setPriceOld(saleInfo.map(SaleInfo::getListPrice).map(retailPrice -> (int) retailPrice.getAmount()).orElse(0));
                });
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

    private Collection<BookGoogleApi> getBooks(String searchString, String from, String to, Integer offset, Integer limit) {
        Root root = null;
        List<AlphabetObject> alphabetObjects = AlphabetService.getAlphabet(defaultLocale);
        String requestUrl = "";
        for (int i = 0; i < attempts; i++) {
            String searchWord = "";
            if (Objects.nonNull(searchString)) {
                searchWord = getRandomSearchWord(alphabetObjects) + searchString;
            } else {
                searchWord = getRandomSearchWord(alphabetObjects);
            }
            requestUrl = getGoogleBooksApiUrl(searchWord, from, to, offset, limit);
            Optional<BookRequestRedis> bookRequestRedisOptional = bookRequestRepository.findById(requestUrl);
            if (bookRequestRedisOptional.isPresent()) {
                return bookRequestRedisOptional.get().getBooks()
                        .stream()
                        .map(BookGoogleApi::new)
                        .collect(Collectors.toList());
            }
            root = restTemplate.getForEntity(requestUrl, Root.class).getBody();
            if (Objects.nonNull(root) && Objects.nonNull(root.getItems())) {
                break;
            }
        }
        final Collection<BookGoogleApi> books = getBooksFromGoogleRoot(root);
        if (StringUtils.isNotBlank(requestUrl)) {
            bookRequestRepository.save(new BookRequestRedis(requestUrl, books
                    .stream()
                    .map(BookRedis::new)
                    .collect(Collectors.toList())));
        }
        return books;
    }
}
