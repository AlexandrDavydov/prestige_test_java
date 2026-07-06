package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class LoginPage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    private final Page page;
    private  Locator usernameInput;
    private  Locator passwordInput;
    private  Locator loginButton;
    private  Locator pageTitle;

    public LoginPage(Page page) {
        this.page = page;
    }

    public LoginPage navigateTo() {
        page.navigate("/");
        page.waitForLoadState();
        this.usernameInput = page.locator("input[name='username']");
        this.passwordInput = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
        this.pageTitle = page.locator("h1");
        return this;
    }

    public LoginPage typeUsername(String username) {
        usernameInput.clear();
        usernameInput.fill(username);
        return this;
    }

    public LoginPage typePassword(String password) {
        passwordInput.clear();
        passwordInput.fill(password);
        return this;
    }

    public DashboardPage clickLoginAndGoToDashboard() {
        loginButton.click();
        page.waitForLoadState();
        return new DashboardPage(page);
    }

    public LoginPage clickLoginAndStay() {
        loginButton.click();
        page.waitForLoadState();
        return this;
    }

    public DashboardPage login(String username, String password) {
        return typeUsername(username)
                .typePassword(password)
                .clickLoginAndGoToDashboard();
    }

    public boolean isLoginPageLoaded() {
        return pageTitle.isVisible() &&
                pageTitle.textContent().contains("Login") &&
                loginButton.isVisible();
    }

    public String getPageTitle() {
        return pageTitle.textContent();
    }

    public LoginPage clearFields() {
        usernameInput.clear();
        passwordInput.clear();
        return this;
    }

    public boolean areFieldsEmpty() {
        return usernameInput.inputValue().isEmpty() &&
                passwordInput.inputValue().isEmpty();
    }
}