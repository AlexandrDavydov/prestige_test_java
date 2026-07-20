package com.prestige.base;

import com.microsoft.playwright.*;
import com.prestige.config.TestConfig;
import com.prestige.models.TestData;
import com.prestige.tests.UiTestFragments;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
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
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.isHeadless())
                .setArgs(java.util.List.of("--disable-blink-features=AutomationControlled"));
        switch (TestConfig.getBrowserType().toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            default:
                browser = playwright.chromium().launch(launchOptions);
        }
    }

    @BeforeEach
    void setUpContext() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(TestConfig.getViewportWidth(), TestConfig.getViewportHeight())
                .setLocale(TestConfig.getBrowserLocale())
                .setBaseURL(TestConfig.getBaseUrl())
        );
        page = context.newPage();
        testData = new TestData();
        uiTestFragments = new UiTestFragments(page);
    }

    @AfterEach
    void tearDownContext() {
        Allure.addAttachment("Page screenshot", "image/png",
            new ByteArrayInputStream(page.screenshot(new Page.ScreenshotOptions().setFullPage(true))), "png");
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