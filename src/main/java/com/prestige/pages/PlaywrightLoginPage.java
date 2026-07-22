package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class PlaywrightLoginPage {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightLoginPage.class);

    private final Page page;
    private  Locator usernameInput;
    private  Locator passwordInput;
    private  Locator loginButton;
    private  Locator pageTitle;

    public PlaywrightLoginPage(Page page) {
        this.page = page;
    }

    @Step("Переход на страницу логина")
    public PlaywrightLoginPage navigateTo() {
        page.navigate("/");
        page.waitForLoadState();
        this.usernameInput = page.locator("input[name='username']");
        this.passwordInput = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
        this.pageTitle = page.locator("h1");
        return this;
    }

    @Step("Ввести имя пользователя: {username}")
    public PlaywrightLoginPage typeUsername(String username) {
        usernameInput.clear();
        usernameInput.fill(username);
        return this;
    }

    @Step("Ввести пароль")
    public PlaywrightLoginPage typePassword(String password) {
        passwordInput.clear();
        passwordInput.fill(password);
        return this;
    }

    @Step("Войти в систему")
    public DashboardPage clickLoginAndGoToDashboard() {
        loginButton.click();
        page.waitForLoadState();
        return new DashboardPage(page);
    }

    @Step("Нажать кнопку входа")
    public PlaywrightLoginPage clickLoginAndStay() {
        loginButton.click();
        page.waitForLoadState();
        return this;
    }

    @Step("Авторизация: {username}")
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

    @Step("Очистить поля логина")
    public PlaywrightLoginPage clearFields() {
        usernameInput.clear();
        passwordInput.clear();
        return this;
    }

    public boolean areFieldsEmpty() {
        return usernameInput.inputValue().isEmpty() &&
                passwordInput.inputValue().isEmpty();
    }
}