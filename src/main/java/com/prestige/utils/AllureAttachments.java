package com.prestige.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;

import java.nio.charset.StandardCharsets;

public class AllureAttachments {

    @Attachment(value = "Page screenshot", type = "image/png", fileExtension = "png")
    public static byte[] takeScreenshot(Page page) {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    @Attachment(value = "Page HTML source", type = "text/html", fileExtension = "html")
    public static String pageSource(Page page) {
        return page.content();
    }

    @Attachment(value = "Page URL", type = "text/plain")
    public static String pageUrl(String url) {
        return url;
    }

    @Attachment(value = "Test data", type = "application/json", fileExtension = "json")
    public static String testData(String json) {
        return json;
    }

    @Attachment(value = "Browser console errors", type = "text/plain")
    public static String consoleErrors(String errors) {
        return errors;
    }
}
