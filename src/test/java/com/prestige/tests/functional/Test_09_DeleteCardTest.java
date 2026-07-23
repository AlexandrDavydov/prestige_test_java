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
    Card editCardData;

    @Test
    @Tag(CARD)
    public void test_09_DeleteCard() {
        uiTestFragments.login();
        deleteCardWithUi(createdCardData);
        uiTestFragments.checkCardExists(editCardData, false);
    }

    @BeforeEach
    void beforeTest() {
        createdCardData = CardFactory.createRandomCard();
        editCardData = CardFactory.createRandomCard();
        createdCardData.setId(new DbAdapter().addCard(createdCardData));
        testData.addCard(createdCardData);
        testData.addCard(editCardData);
    }

    public void deleteCardWithUi(Card cardData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CardsPage cardsPage = dashboardPage.goToCards();
        cardsPage.waitForPageLoad();
        cardsPage.deleteCard(cardData.getName());
    }
}
