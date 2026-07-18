package com.prestige.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.prestige.models.Card;

import java.util.ArrayList;
import java.util.List;

public class CardsPage extends BasePage {

    private final String pageTitle = "h1";

    private final String addCardButton = "a[title='Добавить абонемент']";
    private final String addCardImage = "a[title='Добавить абонемент'] img";

    private final String tableRows = "table tr:not(.section_sub_title)";
    private final String tableHeader = "table tr.section_sub_title";

    private final String cardName = "td:nth-child(1)";
    private final String cardPrice = "td:nth-child(2)";
    private final String cardLessonsCount = "td:nth-child(3)";
    private final String cardDuration = "td:nth-child(4)";
    private final String cardColor = "td:nth-child(5)";
    private final String cardActions = "td:nth-child(6)";

    private final String editButton = "a[title='Редактировать']";
    private final String deleteButton = "a.delete-btn";

    public CardsPage(Page page) {
        super(page);
    }

    public CardsPage navigateTo() {
        page.navigate("/cards");
        waitForPageLoad();
        return this;
    }

    @Override
    public boolean isPageLoaded() {
        return page.isVisible(pageTitle) &&
                page.textContent(pageTitle).contains("Абонементы");
    }

    public String getPageHeader() {
        return page.textContent(pageTitle);
    }

    public AddCardPage clickAddCard() {
        page.click(addCardButton);
        return new AddCardPage(page);
    }

    public boolean isAddCardButtonVisible() {
        return page.isVisible(addCardImage);
    }

    public int getCardsCount() {
        return page.locator(tableRows).count();
    }

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            Card card = mapRowToCard(row);
            cards.add(card);
        }

        return cards;
    }

    public Card getCardByName(String name) {
        Locator row = findCardRow(name);
        if (row != null) {
            return mapRowToCard(row);
        }
        return null;
    }

    private Locator findCardRow(String name) {
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            String cardNameText = row.locator(cardName).textContent().trim();

            if (cardNameText.equals(name)) {
                return row;
            }
        }

        return null;
    }

    public List<Card> findCardsByName(String namePart) {
        List<Card> result = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            Locator row = rows.nth(i);
            String cardNameText = row.locator(cardName).textContent().trim();

            if (cardNameText.toLowerCase().contains(namePart.toLowerCase())) {
                result.add(mapRowToCard(row));
            }
        }

        return result;
    }

    private Card mapRowToCard(Locator row) {
        String name = row.locator(cardName).textContent().trim();
        String priceStr = row.locator(cardPrice).textContent().trim();
        String lessonsCountStr = row.locator(cardLessonsCount).textContent().trim();
        String duration = row.locator(cardDuration).textContent().trim();

        double price = parseDouble(priceStr);
        int lessonsCount = parseInteger(lessonsCountStr);

        Card card = new Card();
        card.setName(name);
        card.setPrice(price);
        card.setLessonsCount(lessonsCount);
        card.setDuration(duration);
        return card;
    }

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public EditCardPage clickEditCard(String name) {
        Locator row = findCardRow(name);
        if (row != null) {
            row.locator(editButton).click();
            return new EditCardPage(page);
        }
        throw new RuntimeException("Абонемент не найден: " + name);
    }

    public EditCardPage clickEditCardAtIndex(int index) {
        Locator rows = page.locator(tableRows);
        if (index < rows.count()) {
            rows.nth(index).locator(editButton).click();
            return new EditCardPage(page);
        }
        throw new RuntimeException("Абонемент с индексом " + index + " не найден");
    }

    public CardsPage deleteCard(String name) {
        Locator row = findCardRow(name);
        if (row != null) {
            row.locator(deleteButton).click();
            confirmDeleteModal();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Абонемент не найден: " + name);
    }

    public CardsPage deleteCardWithModal(String name) {
        Locator row = findCardRow(name);
        if (row != null) {
            String deleteUrl = row.locator(deleteButton).getAttribute("data-url");
            row.locator(deleteButton).click();
            assert isDeleteModalVisible() : "Модальное окно не открылось";
            confirmDeleteModal();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Абонемент не найден: " + name);
    }

    public CardsPage deleteCardAtIndex(int index) {
        Locator rows = page.locator(tableRows);
        if (index < rows.count()) {
            rows.nth(index).locator(deleteButton).click();
            confirmDeleteModal();
            waitForPageLoad();
            return this;
        }
        throw new RuntimeException("Абонемент с индексом " + index + " не найден");
    }

    public boolean isCardExists(String name) {
        return findCardRow(name) != null;
    }

    public boolean isCardExists(Card card) {
        String name = card.getName();
        Card found = getCardByName(name);

        if (found == null) {
            return false;
        }

        return found.getPrice() == card.getPrice() &&
                found.getLessonsCount() == card.getLessonsCount() &&
                found.getDuration().equals(card.getDuration());
    }

    public boolean isTableNotEmpty() {
        return getCardsCount() > 0;
    }

    public List<String> getAllCardNames() {
        List<String> names = new ArrayList<>();
        Locator rows = page.locator(tableRows);

        for (int i = 0; i < rows.count(); i++) {
            String name = rows.nth(i).locator(cardName).textContent().trim();
            names.add(name);
        }

        return names;
    }

    public CardsPage waitForCardToAppear(String name, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (isCardExists(name)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Абонемент не появился за " + timeoutSeconds + " секунд: " + name);
    }

    public CardsPage waitForCardToDisappear(String name, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (!isCardExists(name)) {
                return this;
            }
            page.waitForTimeout(500);
        }

        throw new RuntimeException("Абонемент не исчез за " + timeoutSeconds + " секунд: " + name);
    }
}
