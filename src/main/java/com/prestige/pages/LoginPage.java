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
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator pageTitle;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameInput = page.locator("input[name='username']");
        this.passwordInput = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
        this.pageTitle = page.locator("h1");
    }

    /**
     * Открывает страницу логина
     */
    public LoginPage navigateTo() {
        log.info("🌐 Открываем страницу логина");
        page.navigate("/");
        page.waitForLoadState();
        return this;
    }

    /**
     * Вводит логин в поле username
     */
    public LoginPage typeUsername(String username) {
        log.info("👤 Ввод логина: {}", username);
        usernameInput.clear();
        usernameInput.fill(username);
        return this;
    }

    /**
     * Вводит пароль в поле password
     */
    public LoginPage typePassword(String password) {
        log.info("🔑 Ввод пароля: ***");
        passwordInput.clear();
        passwordInput.fill(password);
        return this;
    }

    /**
     * Нажимает кнопку Login и возвращает следующую страницу
     * (предполагаем, что после логина попадаем на Dashboard)
     */
    public DashboardPage clickLoginAndGoToDashboard() {
        log.info("🖱️ Нажатие кнопки Login");
        loginButton.click();
        page.waitForLoadState();
        return new DashboardPage(page);
    }

    /**
     * Нажимает кнопку Login, но остаётся на странице логина
     * (используется для проверки ошибок)
     */
    public LoginPage clickLoginAndStay() {
        log.info("🖱️ Нажатие кнопки Login (ожидание ошибки)");
        loginButton.click();
        page.waitForLoadState();
        return this;
    }

    /**
     * Полный метод для авторизации в один вызов
     */
    public DashboardPage login(String username, String password) {
        log.info("🔐 Авторизация пользователя: {}", username);
        return typeUsername(username)
                .typePassword(password)
                .clickLoginAndGoToDashboard();
    }

    /**
     * Проверяет, что страница логина загружена
     */
    public boolean isLoginPageLoaded() {
        return pageTitle.isVisible() &&
                pageTitle.textContent().contains("Login") &&
                loginButton.isVisible();
    }

    /**
     * Получает заголовок страницы
     */
    public String getPageTitle() {
        return pageTitle.textContent();
    }

    /**
     * Очищает все поля
     */
    public LoginPage clearFields() {
        usernameInput.clear();
        passwordInput.clear();
        return this;
    }

    /**
     * Проверяет, что поля пустые
     */
    public boolean areFieldsEmpty() {
        return usernameInput.inputValue().isEmpty() &&
                passwordInput.inputValue().isEmpty();
    }
}