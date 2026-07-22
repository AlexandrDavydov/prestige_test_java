package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import static com.prestige.utils.StepHelper.step;
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

    public PlaywrightLoginPage navigateTo() {
        return step("Переход на страницу логина", () -> {
            page.navigate("/");
            page.waitForLoadState();
            this.usernameInput = page.locator("input[name='username']");
            this.passwordInput = page.locator("input[name='password']");
            this.loginButton = page.locator("button[type='submit']");
            this.pageTitle = page.locator("h1");
            return this;
        });
    }

    public PlaywrightLoginPage typeUsername(String username) {
        return step("Ввести имя пользователя: " + username, () -> {
            usernameInput.clear();
            usernameInput.fill(username);
            return this;
        });
    }

    public PlaywrightLoginPage typePassword(String password) {
        return step("Ввести пароль", () -> {
            passwordInput.clear();
            passwordInput.fill(password);
            return this;
        });
    }

    public DashboardPage clickLoginAndGoToDashboard() {
        return step("Войти в систему", () -> {
            loginButton.click();
            page.waitForLoadState();
            return new DashboardPage(page);
        });
    }

    public PlaywrightLoginPage clickLoginAndStay() {
        return step("Нажать кнопку входа", () -> {
            loginButton.click();
            page.waitForLoadState();
            return this;
        });
    }

    public DashboardPage login(String username, String password) {
        return step("Авторизация: " + username, () ->
            typeUsername(username)
                    .typePassword(password)
                    .clickLoginAndGoToDashboard()
        );
    }

    public boolean isLoginPageLoaded() {
        return pageTitle.isVisible() &&
                pageTitle.textContent().contains("Login") &&
                loginButton.isVisible();
    }

    public String getPageTitle() {
        return pageTitle.textContent();
    }

    public PlaywrightLoginPage clearFields() {
        return step("Очистить поля логина", () -> {
            usernameInput.clear();
            passwordInput.clear();
            return this;
        });
    }

    public boolean areFieldsEmpty() {
        return usernameInput.inputValue().isEmpty() &&
                passwordInput.inputValue().isEmpty();
    }
}
