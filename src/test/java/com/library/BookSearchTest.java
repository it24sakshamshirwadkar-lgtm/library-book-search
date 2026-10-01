package com.library;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookSearchTest {

    static WebDriver driver;
    static WebDriverWait wait;
    static final String BASE_URL = "http://localhost:8081/library-book-search/";

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    void searchReturnsMatchingBook() {
        driver.get(BASE_URL);
        WebElement input = driver.findElement(By.id("searchInput"));
        input.clear();
        input.sendKeys("Orwell");
        driver.findElement(By.id("searchBtn")).click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.className("book-card")),
            ExpectedConditions.textToBePresentInElementLocated(By.id("status"), "No books")
        ));

        List<WebElement> cards = driver.findElements(By.className("book-card"));
        assertFalse(cards.isEmpty(), "Expected at least one result for Orwell");
        assertTrue(driver.getPageSource().contains("1984"));
    }

    @Test
    void searchWithNoMatchShowsStatusMessage() {
        driver.get(BASE_URL);
        WebElement input = driver.findElement(By.id("searchInput"));
        input.clear();
        input.sendKeys("zzznonexistent");
        driver.findElement(By.id("searchBtn")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("status"), "No books"));

        WebElement status = driver.findElement(By.id("status"));
        assertTrue(status.getText().toLowerCase().contains("no books found"));
    }

    @Test
    void searchByAuthorWorks() {
        driver.get(BASE_URL);
        WebElement input = driver.findElement(By.id("searchInput"));
        input.clear();
        input.sendKeys("Clear");
        driver.findElement(By.id("searchBtn")).click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.className("book-card")),
            ExpectedConditions.textToBePresentInElementLocated(By.id("status"), "No books")
        ));

        assertTrue(driver.getPageSource().contains("Atomic Habits"));
    }
}
