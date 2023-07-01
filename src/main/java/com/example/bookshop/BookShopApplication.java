package com.example.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableCaching
public class BookShopApplication {

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
}
