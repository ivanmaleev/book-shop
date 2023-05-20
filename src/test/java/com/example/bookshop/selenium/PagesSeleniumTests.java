package com.example.bookshop.selenium;

import com.example.bookshop.selenium.page.GenrePage;
import com.example.bookshop.selenium.page.MainPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PagesSeleniumTests {

    private static ChromeDriver driver;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\projects\\book-shop\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    void testMainPageAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    void testGenrePageAccess() throws InterruptedException {
        GenrePage genrePage = new GenrePage(driver);
        genrePage
                .callPage()
                .pause()
                .setUpSearchToken("Детективы")
                .pause()
                .submit()
                .pause();
        assertTrue(driver.getPageSource().contains("Детективы"));
    }
}
