package com.example.bookshop.service.impl;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.dto.AlphabetObject;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.data.google.api.books.Item;
import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.errs.BookstoreApiWrongParameterException;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.GenreService;
import com.example.bookshop.service.LoadGenresService;
import lombok.NoArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class BookServiceImpl implements BookService {

    @Value("${google.books.api.attempts}")
    private Integer attempts;

    @Value("${google.books.api.key}")
    private String apiKey;

    @Value("${google.books.api.url}")
    private String googleUrl;

    @Value("${locale.default}")
    private String defaultLocale;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreService genreService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Book> getBooksByAuthor(Author author, Integer offset, Integer limit) {
        String searchString = "+inauthor:" + author.getLastName() + " " + author.getFirstName();
        return getBooks(searchString, offset, limit);
    }

    @Override
    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    @Override
    public List<Book> getPageofRecommendedBooks(Integer offset, Integer limit) {

//        LoadGenresService loadGenresService = new LoadGenresService();
//        loadGenresService.loadGenres();

        return getBooks(null, offset, limit);
    }

    @Override
    public List<Book> getPageOfRecentBooks(Integer offset, Integer limit, Date from, Date end) {
        return getBooks(null, offset, limit);
    }

    @Override
    public List<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        return getBooks(null, offset, limit);
    }

    @Override
    public List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit) {
        return getBooks(searchWord, offset, limit);
    }

    @Override
    public Book getBook(String slug) {
        com.example.bookshop.data.book.Root root = restTemplate.getForEntity(getGoogleBookApiUrl(slug), com.example.bookshop.data.book.Root.class).getBody();
        return getBookFromGoogleRoot(root);
    }

    @Override
    public List<Book> getBooksByGenreId(long genreId, Integer offset, Integer limit) {
        GenreDto genreDto = genreService.findGenreById(genreId, Langs.EN);
        String searchString = "+subject:" + genreDto.getName();
        return getBooks(searchString, offset, limit);
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

    private List<Book> getBooksFromGoogleRoot(Root root) {
        ArrayList<Book> books = new ArrayList<>();
        if (root != null) {
            for (Item item : root.getItems()) {
                Book book = new Book();
                if (item.getVolumeInfo() != null) {
                    book.setAuthor(new Author(item.getVolumeInfo().getAuthors()));
                    book.setTitle(item.getVolumeInfo().getTitle());
                    book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
                    book.setSlug(item.getId());
                }
                if (item.getSaleInfo() != null) {
                    book.setPrice(item.getSaleInfo().getRetailPrice().getAmount());
                    Double oldPrice = item.getSaleInfo().getListPrice().getAmount();
                    book.setPriceOld(oldPrice.intValue());
                }
                books.add(book);
            }
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
            }
            if (root.getSaleInfo() != null) {
                book.setPrice(root.getSaleInfo().getRetailPrice().getAmount());
                Double oldPrice = root.getSaleInfo().getListPrice().getAmount();
                book.setPriceOld(oldPrice.intValue());
            }
        }
        return book;
    }

    private String getRandomSearchWord(String lang) {
        Random r = new Random();
        List<AlphabetObject> alphabetObjects = AlphabetService.alphabetLangMap.get(lang);
        return String.valueOf(alphabetObjects.get(r.nextInt(alphabetObjects.size())).getLetter());
    }

    private List<Book> getBooks(String searchString, Integer offset, Integer limit) {
        Root root = null;
        for (int i = 0; i < attempts; i++) {
            String searchWord = "";
            if (searchString != null) {
                searchWord = getRandomSearchWord(defaultLocale) + searchString;
            } else {
                searchWord = getRandomSearchWord(defaultLocale);
            }

            root = restTemplate.getForEntity(getGoogleBooksApiUrl(searchWord, offset, limit), Root.class).getBody();
            if (root != null && root.getItems() != null) {
                break;
            }
        }
        return getBooksFromGoogleRoot(root);
    }

}
