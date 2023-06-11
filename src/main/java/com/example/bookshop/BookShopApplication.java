package com.example.bookshop;

import com.example.bookshop.service.LoadGenresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.PrintStream;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
public class BookShopApplication {


    @Autowired(required = false)
    LoadGenresService loadGenresService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BookShopApplication.class);
        springApplication.setBanner((environment, sourceClass, out) ->
                out.println("\n" +
                        " /$$$$$$$                      /$$                       /$$                          \n" +
                        "| $$__  $$                    | $$                      | $$                          \n" +
                        "| $$  \\ $$  /$$$$$$   /$$$$$$ | $$   /$$        /$$$$$$$| $$$$$$$   /$$$$$$   /$$$$$$ \n" +
                        "| $$$$$$$  /$$__  $$ /$$__  $$| $$  /$$/       /$$_____/| $$__  $$ /$$__  $$ /$$__  $$\n" +
                        "| $$__  $$| $$  \\ $$| $$  \\ $$| $$$$$$/       |  $$$$$$ | $$  \\ $$| $$  \\ $$| $$  \\ $$\n" +
                        "| $$  \\ $$| $$  | $$| $$  | $$| $$_  $$        \\____  $$| $$  | $$| $$  | $$| $$  | $$\n" +
                        "| $$$$$$$/|  $$$$$$/|  $$$$$$/| $$ \\  $$       /$$$$$$$/| $$  | $$|  $$$$$$/| $$$$$$$/\n" +
                        "|_______/  \\______/  \\______/ |__/  \\__/      |_______/ |__/  |__/ \\______/ | $$____/ \n" +
                        "                                                                            | $$      \n" +
                        "                                                                            | $$      \n" +
                        "                                                                            |__/      \n"));
        springApplication.run(args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void doSomethingAfterStartup() {
//        loadGenresService.loadGenres();
//    }
}
