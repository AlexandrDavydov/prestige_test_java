package com.prestige.base;

import com.microsoft.playwright.*;
import com.prestige.models.TestData;
import com.prestige.tests.UiTestFragments;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected APIRequestContext request;
    protected TestData testData;
    protected UiTestFragments uiTestFragments;

    @BeforeAll
    void setUpPlaywright() {
        playwright = Playwright.create();
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setArgs(java.util.List.of("--disable-blink-features=AutomationControlled"))
        );
    }

    @BeforeEach
    void setUpContext() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1024, 768)
                .setLocale("ru-RU")
                .setBaseURL(System.getProperty("base.url", "http://127.0.0.1:5000"))
        );
        page = context.newPage();
        testData = new TestData();
        uiTestFragments = new UiTestFragments(page);
    }

    @AfterEach
    void tearDownContext() {
        testData.deleteTestData();
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    void tearDownPlaywright() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}