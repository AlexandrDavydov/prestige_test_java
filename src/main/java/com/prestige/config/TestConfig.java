package com.prestige.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            // ignore
        }
    }

    private static String resolve(String sysProp, String envVar, String propKey, String fallback) {
        String value = System.getProperty(sysProp);
        if (value != null && !value.isEmpty()) return value;

        value = System.getenv(envVar);
        if (value != null && !value.isEmpty()) return value;

        value = props.getProperty(propKey);
        if (value != null && !value.isEmpty()) return value;

        return fallback;
    }

    public static String getDbUrl() {
        return resolve("db.url", "DB_URL", "db.url", "jdbc:sqlite:prestige/instance/db.db");
    }

    public static String getAppUsername() {
        return resolve("app.username", "APP_USERNAME", "app.username", "admin");
    }

    public static String getAppPassword() {
        return resolve("app.password", "APP_PASSWORD", "app.password", "");
    }

    public static String getBaseUrl() {
        return resolve("base.url", "APP_URL", "base.url", "http://127.0.0.1:5000");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(resolve("headless", "HEADLESS", "headless", "false"));
    }

    public static int getViewportWidth() {
        return Integer.parseInt(resolve("viewport.width", "VIEWPORT_WIDTH", "viewport.width", "1024"));
    }

    public static int getViewportHeight() {
        return Integer.parseInt(resolve("viewport.height", "VIEWPORT_HEIGHT", "viewport.height", "768"));
    }

    public static String getBrowserLocale() {
        return resolve("browser.locale", "BROWSER_LOCALE", "browser.locale", "ru-RU");
    }

    public static String getBrowserType() {
        return resolve("browser.type", "BROWSER_TYPE", "browser.type", "chromium");
    }

    public static String getScreenshotDir() {
        return resolve("screenshot.dir", "SCREENSHOT_DIR", "screenshot.dir", "screenshots");
    }
}
