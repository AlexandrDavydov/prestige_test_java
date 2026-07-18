package com.prestige.tests;

import com.prestige.base.BaseTest;
import com.prestige.models.Card;
import com.prestige.pages.AddCardPage;
import com.prestige.pages.CardsPage;
import com.prestige.pages.DashboardPage;
import com.prestige.utils.CardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.CARD;
import static com.prestige.tests.TestGroups.LOCK_CARD;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ResourceLock(LOCK_CARD)
class Test_07_CreateCardTest extends BaseTest {
    Card cardData;

    @Test
    @Tag(CARD)
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