package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Card;
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
class Test_09_DeleteCardTest extends BaseTest {
    Card createdCardData;

    @Test
    @Tag(CARD)
    public void test_09_DeleteCard() {
        uiTestFragments.login();
        deleteCardWithUi(createdCardData);
        uiTestFragments.checkCardExists(createdCardData, false);
    }

    @BeforeEach
    void beforeTest() {
        CreateCardInTheDatabase();
    }

    public void deleteCardWithUi(Card cardData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CardsPage cardsPage = dashboardPage.goToCards();
        cardsPage.waitForPageLoad();
        cardsPage.deleteCard(cardData.getName());
    }

    public void CreateCardInTheDatabase() {
        createdCardData = CardFactory.createRandomCard();
        createdCardData.setId(dbAdapter.addCard(createdCardData));
        testData.addCard(createdCardData);
    }
}
