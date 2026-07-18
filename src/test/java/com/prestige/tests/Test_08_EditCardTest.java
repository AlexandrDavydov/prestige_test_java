package com.prestige.tests;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Card;
import com.prestige.pages.CardsPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditCardPage;
import com.prestige.utils.CardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_08_EditCardTest extends BaseTest {
    Card createdCardData;
    Card editCardData;

    @Test
    public void test_08_EditCard() {
        uiTestFragments.login();
        editCard(createdCardData, editCardData);
        uiTestFragments.checkCardExists(editCardData, true);
    }

    @BeforeEach
    void beforeTest() {
        createdCardData = CardFactory.createRandomCard();
        editCardData = CardFactory.createRandomCard();
        new DbAdapter().addCard(createdCardData);
        testData.addCard(createdCardData);
        testData.addCard(editCardData);
    }

    public void editCard(Card cardForEditData, Card newCardData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CardsPage cardsPage = dashboardPage.goToCards();
        cardsPage.waitForPageLoad();
        EditCardPage editCardPage = cardsPage.clickEditCard(cardForEditData.getName());
        editCardPage.waitForPageLoad();
        editCardPage.fillCardForm(newCardData);
        editCardPage.clickSave();
    }
}
