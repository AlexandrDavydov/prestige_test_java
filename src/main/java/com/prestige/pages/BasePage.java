package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.util.List;

public abstract class BasePage {

    protected final Page page;

    // Общие селекторы для всех страниц
    private final String logoutLink = "a[href*='logout']";
    private final String flashMessages = ".flash";
    private final String flashSuccess = ".flash.success";
    private final String flashError = ".flash.error";
    private final String flashWarning = ".flash.warning";
    private final String flashInfo = ".flash.info";
    private final String closeFlashButton = ".flash .close-btn";
    private final String mainNav = ".main-nav";
    private final String navLinks = ".main-nav-links";

    // Селекторы для модального окна удаления
    private final String deleteModal = "#deleteModal";
    private final String deleteModalContent = ".modal-content";
    private final String deleteConfirmButton = ".modal-content button:nth-child(1)";
    private final String deleteCancelButton = ".modal-content button:nth-child(2)";

    public BasePage(Page page) {
        this.page = page;
    }


    public EditStudentPage waitForPageLoad() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return null;
    }

    public DashboardPage goToDashboard() {
        clickNavLink("Главная");
        return new DashboardPage(page);
    }

    public LessonsPage goToLessons() {
        clickNavLink("Занятия");
        return new LessonsPage(page);
    }

    public StudentsPage goToStudents() {
        clickNavLink("Ученики");
        return new StudentsPage(page);
    }

    public CoachesPage goToCoaches() {
        clickNavLink("Тренеры");
        return new CoachesPage(page);
    }

    public CardsPage goToCards() {
        clickNavLink("Абонементы");
        return new CardsPage(page);
    }

    public LessonTemplatesPage goToLessonTemplates() {
        clickNavLink("Шаблоны занятий");
        return new LessonTemplatesPage(page);
    }

//    public ReportsPage goToReports() {
//        clickNavLink("Отчеты");
//        return new ReportsPage(page);
//    }

    protected void clickNavLink(String linkText) {
        Locator link = page.locator(navLinks).filter(new Locator.FilterOptions()
                .setHasText(linkText));
        link.click();
        waitForPageLoad();
    }

    /**
     * Получить все ссылки навигации
     */
    public List<String> getNavLinks() {
        return page.locator(navLinks).allTextContents();
    }

    /**
     * Проверить, активна ли ссылка навигации
     */
    public boolean isNavLinkActive(String linkText) {
        Locator link = page.locator(navLinks).filter(new Locator.FilterOptions()
                .setHasText(linkText));
        return link.getAttribute("class") != null &&
                link.getAttribute("class").contains("active");
    }

    // ============ ВЫХОД ============

    /**
     * Выйти из системы
     */
    public PlaywrightLoginPage logout() {
        page.click(logoutLink);
        waitForPageLoad();
        return new PlaywrightLoginPage(page);
    }

    /**
     * Проверить, отображается ли ссылка выхода
     */
    public boolean isLogoutLinkVisible() {
        return page.isVisible(logoutLink);
    }

    /**
     * Получить текст ссылки выхода
     */
    public String getLogoutText() {
        return page.textContent(logoutLink);
    }

    // ============ FLASH СООБЩЕНИЯ ============

    /**
     * Получить все flash сообщения
     */
    public List<String> getFlashMessages() {
        return page.locator(flashMessages).allTextContents();
    }

    /**
     * Получить flash сообщения определенного типа
     */
    public List<String> getFlashMessagesByType(String type) {
        String selector = ".flash." + type;
        return page.locator(selector).allTextContents();
    }

    /**
     * Проверить наличие flash сообщения
     */
    public boolean hasFlashMessage(String message) {
        List<String> messages = getFlashMessages();
        return messages.stream().anyMatch(msg -> msg.contains(message));
    }

    /**
     * Проверить наличие успешного flash сообщения
     */
    public boolean hasSuccessMessage(String message) {
        List<String> messages = getFlashMessagesByType("success");
        return messages.stream().anyMatch(msg -> msg.contains(message));
    }

    /**
     * Проверить наличие ошибки
     */
    public boolean hasErrorMessage(String message) {
        List<String> messages = getFlashMessagesByType("error");
        return messages.stream().anyMatch(msg -> msg.contains(message));
    }

    /**
     * Проверить наличие предупреждения
     */
    public boolean hasWarningMessage(String message) {
        List<String> messages = getFlashMessagesByType("warning");
        return messages.stream().anyMatch(msg -> msg.contains(message));
    }

    /**
     * Проверить наличие информационного сообщения
     */
    public boolean hasInfoMessage(String message) {
        List<String> messages = getFlashMessagesByType("info");
        return messages.stream().anyMatch(msg -> msg.contains(message));
    }

    /**
     * Закрыть все flash сообщения
     */
    public void closeAllFlashMessages() {
        Locator closeButtons = page.locator(closeFlashButton);
        int count = closeButtons.count();
        for (int i = count - 1; i >= 0; i--) {
            closeButtons.nth(i).click();
        }
    }

    /**
     * Закрыть flash сообщение по тексту
     */
    public void closeFlashMessage(String message) {
        Locator flash = page.locator(flashMessages).filter(new Locator.FilterOptions()
                .setHasText(message));
        if (flash.count() > 0) {
            flash.locator(closeFlashButton).click();
        }
    }

    /**
     * Ожидать исчезновения flash сообщения
     */
    public void waitForFlashToDisappear(String message, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (!hasFlashMessage(message)) {
                return;
            }
            page.waitForTimeout(500);
        }
        throw new RuntimeException("Flash сообщение не исчезло за " + timeoutSeconds + " секунд: " + message);
    }

    /**
     * Получить количество flash сообщений
     */
    public int getFlashMessagesCount() {
        return page.locator(flashMessages).count();
    }

    // ============ МОДАЛЬНОЕ ОКНО УДАЛЕНИЯ ============

    /**
     * Открыть модальное окно удаления
     */
    public void openDeleteModal(String url) {
        page.evaluate("openDeleteModal('" + url + "')");
    }

    /**
     * Подтвердить удаление в модальном окне
     */
    public void confirmDelete() {
        page.evaluate("confirmDelete()");
        waitForPageLoad();
    }

    /**
     * Отменить удаление в модальном окне
     */
    public void cancelDelete() {
        page.evaluate("closeDeleteModal()");
    }

    /**
     * Проверить, открыто ли модальное окно
     */
    public boolean isDeleteModalVisible() {
        return page.isVisible(deleteModal);
    }

    /**
     * Закрыть модальное окно через кнопку "Нет"
     */
//    public void closeDeleteModal() {
//        page.click(deleteCancelButton);
//        page.waitForSelector(deleteModal, new Page.WaitForSelectorOptions()
//                .setState(Page.WaitForSelectorState.HIDDEN));
//    }

    /**
     * Подтвердить удаление через кнопку "Да"
     */
    public void confirmDeleteModal() {
        page.onDialog(dialog -> {
            dialog.accept();
        });
        waitForPageLoad();
    }

    /**
     * Получить текст модального окна
     */
    public String getDeleteModalText() {
        return page.textContent(deleteModalContent);
    }

    // ============ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ============

    /**
     * Получить заголовок страницы
     */
    public String getPageTitle() {
        return page.title();
    }

    /**
     * Получить текущий URL
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Проверить, содержит ли URL текст
     */
    public boolean isUrlContains(String text) {
        return page.url().contains(text);
    }

    /**
     * Скроллить до элемента
     */
    public void scrollToElement(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    /**
     * Сделать скриншот страницы
     */
    public void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get("screenshots/" + name + ".png"))
                .setFullPage(true));
    }

    /**
     * Обновить страницу
     */
    public void refreshPage() {
        page.reload();
        waitForPageLoad();
    }
}