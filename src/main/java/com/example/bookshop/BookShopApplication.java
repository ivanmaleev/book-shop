package com.example.bookshop;

import com.example.bookshop.service.LoadGenresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
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
