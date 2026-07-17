package com.prestige.tests;

import com.prestige.base.BaseTest;
import com.prestige.models.Card;
import com.prestige.pages.AddCardPage;
import com.prestige.pages.CardsPage;
import com.prestige.pages.DashboardPage;
import com.prestige.utils.CardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_07_CreateCardTest extends BaseTest {
    Card cardData;

    @Test
    public void test_07_CardStudent() {
        uiTestFragments.login();
        createCardWithUi(cardData);
        uiTestFragments.checkCardExists(cardData, true);
    }

    @BeforeEach
    void beforeTest() {
        cardData = CardFactory.createRandomCard();
        testData.addCard(cardData);
    }

    public void createCardWithUi(Card cardData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CardsPage cardsPage = dashboardPage.goToCards();
        cardsPage.waitForPageLoad();
        AddCardPage addCardPage = cardsPage.clickAddCard();
        addCardPage.waitForPageLoad();
        addCardPage.submitCard(cardData);
    }
}