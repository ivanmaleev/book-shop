package com.example.bookshop.service.impl;

import com.example.bookshop.data.Author;
import com.example.bookshop.data.Book;
import com.example.bookshop.data.BookRepository;
import com.example.bookshop.data.google.api.books.Item;
import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.errs.BookstoreApiWrongParameterException;
import com.example.bookshop.service.BookService;
import lombok.NoArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class BookServiceImpl implements BookService {

    @Value("${google.books.api.key}")
    private String apiKey;

    @Value("${google.books.api.url}")
    private String googleUrl;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
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
    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    @Override
    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    @Override
    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    @Override
    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    @Override
    public List<Book> getPageofRecommendedBooks(Integer offset, Integer limit) {

        Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(getRandomSearchWord(), offset, limit), Root.class).getBody();
        return getBooksFromGoogleRoot(root);
    }

    @Override
    public List<Book> getPageOfRecentBooks(Integer offset, Integer limit, Date from, Date end) {
        Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(getRandomSearchWord(), offset, limit), Root.class).getBody();
        return getBooksFromGoogleRoot(root);
    }

    @Override
    public List<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(getRandomSearchWord(), offset, limit), Root.class).getBody();
        return getBooksFromGoogleRoot(root);
    }

    @Override
    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    @Override
    public List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit) {
        Root root = restTemplate.getForEntity(getGoogleBooksApiUrl(searchWord, offset, limit), Root.class).getBody();
        return getBooksFromGoogleRoot(root);
    }

    @Override
    public Book getBook(String slug) {
        com.example.bookshop.data.book.Root root = restTemplate.getForEntity(getGoogleBookApiUrl(slug), com.example.bookshop.data.book.Root.class).getBody();
        return getBookFromGoogleRoot(root);
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

    private String getRandomSearchWord() {
        //TODO переделать
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщыэюя";
        Random r = new Random();
        char c = alphabet.charAt(r.nextInt(alphabet.length()));
        return String.valueOf(c);
    }

}
