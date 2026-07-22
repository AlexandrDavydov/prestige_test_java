package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Card;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

public class AddCardPage extends BasePage {

    private final String pageTitle = "h1";
    private final String nameInput = "input[name='name']";
    private final String colorInput = "input[name='color']";
    private final String colorValue = ".color-value";
    private final String priceInput = "input[name='price']";
    private final String lessonsCountInput = "input[name='lessons_count']";
    private final String durationInput = "input[name='duration']";
    private final String submitButton = "button[type='submit']";
    private final String cancelButton = "a[href*='cards']";

    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";
    private final String requiredAttribute = "required";
    private final String oninputAttribute = "oninput";

    public AddCardPage(Page page) {
        super(page);
    }

    @Step("Переход на страницу добавления абонемента")
    public AddCardPage navigateTo() {
        page.navigate("/add_card");
        waitForPageLoad();
        return this;
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    @Step("Заполнить название абонемента: {name}")
    public AddCardPage fillName(String name) {
        page.fill(nameInput, name);
        return this;
    }

    @Step("Заполнить название абонемента с капитализацией: {name}")
    public AddCardPage fillNameWithCapitalize(String name) {
        page.fill(nameInput, name);
        page.evaluate("document.querySelector('input[name=\"name\"]').dispatchEvent(new Event('input'))");
        return this;
    }

    public String getName() {
        return page.inputValue(nameInput);
    }

    @Step("Заполнить цвет абонемента: {color}")
    public AddCardPage fillColor(String color) {
        page.fill(colorInput, color.toLowerCase());
        return this;
    }

    public String getColor() {
        return page.inputValue(colorInput);
    }

    public String getColorDisplayValue() {
        return page.textContent(colorValue);
    }

    @Step("Заполнить цену абонемента: {price}")
    public AddCardPage fillPrice(int price) {
        page.fill(priceInput, String.valueOf(price));
        return this;
    }

    public int getPrice() {
        String value = page.inputValue(priceInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Step("Заполнить количество занятий: {count}")
    public AddCardPage fillLessonsCount(int count) {
        page.fill(lessonsCountInput, String.valueOf(count));
        return this;
    }

    public int getLessonsCount() {
        String value = page.inputValue(lessonsCountInput);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Step("Заполнить срок действия: {duration}")
    public AddCardPage fillDuration(String duration) {
        page.fill(durationInput, duration);
        return this;
    }

    public String getDuration() {
        return page.inputValue(durationInput);
    }

    @Step("Заполнить форму абонемента")
    public AddCardPage fillCardForm(Card card) {
        if (card.getName() != null) {
            fillName(card.getName());
        }
        if (card.getColor() != null) {
            fillColor(card.getColor());
        }
        fillPrice(card.getPrice());
        fillLessonsCount(card.getLessonsCount());
        if (card.getDuration() != null) {
            fillDuration(card.getDuration());
        }
        return this;
    }

    public Card getCardFromForm() {
        Card card = new Card();
        card.setName(getName());
        card.setColor(getColor());
        card.setPrice(getPrice());
        card.setLessonsCount(getLessonsCount());
        card.setDuration(getDuration());
        return card;
    }

    @Step("Сохранить абонемент")
    public CardsPage clickSave() {
        page.click(submitButton);
        waitForPageLoad();
        return new CardsPage(page);
    }

    @Step("Сохранить и остаться на странице")
    public AddCardPage clickSaveAndStay() {
        page.click(submitButton);
        waitForPageLoad();
        return this;
    }

    @Step("Отменить добавление абонемента")
    public CardsPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new CardsPage(page);
    }

    @Step("Отправить форму абонемента")
    public CardsPage submitCard(Card card) {
        fillCardForm(card);
        return clickSave();
    }

    @Step("Отправить форму абонемента и проверить")
    public CardsPage submitCardAndVerify(Card card) {
        fillCardForm(card);
        CardsPage cardsPage = clickSave();

        String name = card.getName();
        cardsPage.waitForCardToAppear(name, 10);

        assert cardsPage.hasSuccessMessage("Абонемент успешно добавлен") :
                "Не найдено сообщение об успешном добавлении";

        return cardsPage;
    }

    @Step("Очистить поле: {fieldName}")
    public AddCardPage clearField(String fieldName) {
        page.fill(getFieldSelector(fieldName), "");
        return this;
    }

    @Step("Очистить все поля")
    public AddCardPage clearAllFields() {
        page.fill(nameInput, "");
        page.fill(colorInput, "");
        page.fill(priceInput, "");
        page.fill(lessonsCountInput, "");
        page.fill(durationInput, "");
        return this;
    }

    public boolean areAllFieldsVisible() {
        return page.isVisible(nameInput) &&
                page.isVisible(colorInput) &&
                page.isVisible(priceInput) &&
                page.isVisible(lessonsCountInput) &&
                page.isVisible(durationInput);
    }

    public boolean isFieldRequired(String fieldName) {
        return page.getAttribute(getFieldSelector(fieldName), requiredAttribute) != null;
    }

    public boolean areRequiredFieldsMarked() {
        return isFieldRequired("name") &&
                isFieldRequired("price") &&
                isFieldRequired("lessons_count") &&
                isFieldRequired("duration");
    }

    public boolean hasCapitalizationAttribute() {
        String oninput = page.getAttribute(nameInput, oninputAttribute);
        return oninput != null && oninput.contains("capitalizeFirst");
    }

    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0;
    }

    public List<String> getErrorMessages() {
        List<String> errors = new ArrayList<>();
        errors.addAll(page.locator(errorMessages).allTextContents());
        errors.addAll(page.locator(fieldErrors).allTextContents());
        return errors;
    }

    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    public boolean isFormValid() {
        return !hasErrorMessages() &&
                isFieldFilled("name") &&
                isFieldFilled("price") &&
                isFieldFilled("lessons_count") &&
                isFieldFilled("duration");
    }

    public boolean isFieldFilled(String fieldName) {
        String value = page.inputValue(getFieldSelector(fieldName));
        return value != null && !value.isEmpty();
    }

    public boolean isSaveButtonVisible() {
        return page.isVisible(submitButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public boolean isUrlCorrect() {
        return page.url().contains("/add_card");
    }

    private String getFieldSelector(String fieldName) {
        switch (fieldName) {
            case "name": return nameInput;
            case "color": return colorInput;
            case "price": return priceInput;
            case "lessons_count": return lessonsCountInput;
            case "duration": return durationInput;
            default: throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }
}
