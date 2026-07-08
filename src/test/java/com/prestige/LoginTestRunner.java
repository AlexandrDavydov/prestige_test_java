package com.prestige;

import com.microsoft.playwright.*;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.PlaywrightLoginPage;

public class LoginTestRunner {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );
            Page page = browser.newPage();

            // Тест
            PlaywrightLoginPage playwrightLoginPage = new PlaywrightLoginPage(page).navigateTo();
            DashboardPage dashboard = playwrightLoginPage.login(
                    "standard_user",
                    "secret_sauce"
            );

            System.out.println("📄 Заголовок страницы логина: " + playwrightLoginPage.getPageTitle());

            browser.close();
        }
    }
}