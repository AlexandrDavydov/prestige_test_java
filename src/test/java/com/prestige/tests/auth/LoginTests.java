package com.prestige.tests.auth;

import com.prestige.base.BaseTest;
import com.prestige.models.User;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты авторизации")
public class LoginTests extends BaseTest {

    @Test
    @DisplayName("Успешная авторизация с валидными данными")
    void successfulLogin() {
        // Arrange
        User user = User.admin();

        // Act
        LoginPage loginPage = new LoginPage(page)
                .navigateTo();

        DashboardPage dashboard = loginPage
                .typeUsername(user.getUsername())
                .typePassword(user.getPassword())
                .clickLoginAndGoToDashboard();

        // Assert
        assertThat(dashboard.isPageLoaded())
                .as("Дашборд должен быть загружен после успешного логина")
                .isTrue();
    }

    @Test
    @DisplayName("Авторизация с невалидными данными должна показывать ошибку")
    void failedLoginWithInvalidCredentials() {
        // Arrange
        User user = User.invalidUser();

        // Act
        LoginPage loginPage = new LoginPage(page)
                .navigateTo();

        loginPage
                .typeUsername(user.getUsername())
                .typePassword(user.getPassword())
                .clickLoginAndStay();

        // Assert
        // Здесь нужно добавить проверку на сообщение об ошибке
        // (если оно есть на вашей странице)
        assertThat(loginPage.isLoginPageLoaded())
                .as("Должны остаться на странице логина при ошибке")
                .isTrue();

        System.out.println("❌ Авторизация отклонена (как и ожидалось)");
    }

    @Test
    @DisplayName("Проверка загрузки страницы логина")
    void loginPageIsLoaded() {
        // Act
        LoginPage loginPage = new LoginPage(page).navigateTo();

        // Assert
        assertThat(loginPage.isLoginPageLoaded())
                .as("Страница логина должна быть загружена")
                .isTrue();

        assertThat(loginPage.getPageTitle())
                .as("Заголовок страницы должен содержать 'Login'")
                .contains("Login");
    }
}