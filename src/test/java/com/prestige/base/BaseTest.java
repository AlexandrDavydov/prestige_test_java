package com.prestige.base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void setUpPlaywright() {
        log.info("🚀 Запуск Playwright...");
        playwright = Playwright.create();

        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setArgs(java.util.List.of("--disable-blink-features=AutomationControlled"))
        );
        log.info("✅ Браузер запущен (headless: {})", isHeadless);
    }

    @BeforeEach
    void setUpContext() {
        log.info("📄 Создание нового контекста для теста...");
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1024, 768)
                .setLocale("ru-RU")
                .setBaseURL(System.getProperty("base.url", "http://127.0.0.1:5000"))
        );
        page = context.newPage();
        log.info("✅ Страница создана");
    }

    @AfterEach
    void tearDownContext() {
        if (context != null) {
            context.close();
            log.info("📄 Контекст закрыт");
        }
    }

    @AfterAll
    static void tearDownPlaywright() {
        if (browser != null) {
            browser.close();
            log.info("🚀 Браузер закрыт");
        }
        if (playwright != null) {
            playwright.close();
            log.info("✅ Playwright остановлен");
        }
    }
}