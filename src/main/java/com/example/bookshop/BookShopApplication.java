package com.example.bookshop;

import com.example.bookshop.data.google.api.books.Root;
import com.example.bookshop.service.LoadGenresService;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BookShopApplication {


    @Autowired(required = false)
    LoadGenresService loadGenresService;
    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void doSomethingAfterStartup() {
//        loadGenresService.loadGenres();
//    }
}
