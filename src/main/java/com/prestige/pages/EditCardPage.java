package com.prestige.pages;

import com.microsoft.playwright.Page;
import com.prestige.models.Card;

import java.util.List;

public class EditCardPage extends BasePage {

    private final String pageTitle = "h1";
    private final String nameInput = "input[name='name']";
    private final String colorInput = "input[name='color']";
    private final String colorValue = ".color-value";
    private final String priceInput = "input[name='price']";
    private final String lessonsCountInput = "input[name='lessons_count']";
    private final String durationInput = "input[name='duration']";
    private final String saveButton = "button[type='submit']";
    private final String cancelButton = "a[href*='cards']";

    private final String errorMessages = ".error-message";
    private final String fieldErrors = ".field-error";

    public EditCardPage(Page page) {
        super(page);
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    public EditCardPage fillName(String name) {
        page.fill(nameInput, name);
        return this;
    }

    public String getName() {
        return page.inputValue(nameInput);
    }

    public EditCardPage fillColor(String color) {
        page.fill(colorInput, color.toLowerCase());
        return this;
    }

    public String getColor() {
        return page.inputValue(colorInput);
    }

    public String getColorDisplayValue() {
        return page.textContent(colorValue);
    }

    public EditCardPage fillPrice(double price) {
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

    public EditCardPage fillLessonsCount(int count) {
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

    public EditCardPage fillDuration(String duration) {
        page.fill(durationInput, duration);
        return this;
    }

    public String getDuration() {
        return page.inputValue(durationInput);
    }

    public EditCardPage fillCardForm(Card card) {
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

    public EditCardPage fillCardFormCleared(Card card) {
        clearAllFields();
        return fillCardForm(card);
    }

    public EditCardPage fillRequiredFields(Card card) {
        fillName(card.getName());
        fillPrice(card.getPrice());
        fillLessonsCount(card.getLessonsCount());
        fillDuration(card.getDuration());
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

    public CardsPage clickSave() {
        page.click(saveButton);
        waitForPageLoad();
        return new CardsPage(page);
    }

    public EditCardPage clickSaveAndStay() {
        page.click(saveButton);
        waitForPageLoad();
        return this;
    }

    public CardsPage clickCancel() {
        page.click(cancelButton);
        waitForPageLoad();
        return new CardsPage(page);
    }

    public EditCardPage clearAllFields() {
        page.fill(nameInput, "");
        page.fill(colorInput, "");
        page.fill(priceInput, "");
        page.fill(lessonsCountInput, "");
        page.fill(durationInput, "");
        return this;
    }

    public EditCardPage clearField(String fieldName) {
        page.fill(getFieldSelector(fieldName), "");
        return this;
    }

    public boolean isFormDataCorrect(Card expectedCard) {
        Card actualCard = getCardFromForm();

        boolean nameMatch = expectedCard.getName().equals(actualCard.getName());
        boolean colorMatch = expectedCard.getColor().equals(actualCard.getColor());
        boolean priceMatch = expectedCard.getPrice() == actualCard.getPrice();
        boolean lessonsCountMatch = expectedCard.getLessonsCount() == actualCard.getLessonsCount();
        boolean durationMatch = expectedCard.getDuration().equals(actualCard.getDuration());

        return nameMatch && colorMatch && priceMatch && lessonsCountMatch && durationMatch;
    }

    public boolean isFieldFilled(String fieldName) {
        String value = page.inputValue(getFieldSelector(fieldName));
        return value != null && !value.isEmpty();
    }

    public boolean areRequiredFieldsFilled() {
        return isFieldFilled("name") &&
                isFieldFilled("price") &&
                isFieldFilled("lessons_count") &&
                isFieldFilled("duration");
    }

    public boolean hasErrorMessages() {
        return page.locator(errorMessages).count() > 0 ||
                page.locator(fieldErrors).count() > 0;
    }

    public List<String> getErrorMessages() {
        return page.locator(errorMessages).allTextContents();
    }

    public String getFieldError(String fieldName) {
        String selector = ".field-error[data-field='" + fieldName + "']";
        if (page.isVisible(selector)) {
            return page.textContent(selector);
        }
        return null;
    }

    public boolean isSaveButtonVisible() {
        return page.isVisible(saveButton);
    }

    public boolean isCancelButtonVisible() {
        return page.isVisible(cancelButton);
    }

    public boolean isFieldEditable(String fieldName) {
        return page.isEditable(getFieldSelector(fieldName));
    }

    public boolean isFormValid() {
        return !hasErrorMessages() && areRequiredFieldsFilled();
    }

    public CardsPage saveAndVerifySuccess() {
        clickSave();
        assert hasSuccessMessage("Абонемент успешно обновлен") :
                "Не найдено сообщение об успешном обновлении";
        return new CardsPage(page);
    }

    public boolean isUrlCorrect() {
        return page.url().contains("/edit_card");
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
