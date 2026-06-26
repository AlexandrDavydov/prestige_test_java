package com.prestige;

import com.microsoft.playwright.*;
import com.prestige.models.User;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LoginPage;

public class LoginTestRunner {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
            );
            Page page = browser.newPage();

            // Тест
            LoginPage loginPage = new LoginPage(page).navigateTo();
            DashboardPage dashboard = loginPage.login(
                    "standard_user",
                    "secret_sauce"
            );

            System.out.println("📄 Заголовок страницы логина: " + loginPage.getPageTitle());

            browser.close();
        }
    }
}