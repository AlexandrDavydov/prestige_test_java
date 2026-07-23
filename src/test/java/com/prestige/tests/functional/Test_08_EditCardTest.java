package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Card;
import com.prestige.pages.CardsPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.EditCardPage;
import com.prestige.utils.CardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.CARD;
import static com.prestige.tests.TestGroups.LOCK_CARD;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_08_EditCardTest extends BaseTest {
    Card createdCardData;
    Card editCardData;

    @Test
    @Tag(CARD)
    public void test_08_EditCard() {
        uiTestFragments.login();
        editCardWithUi(createdCardData, editCardData);
        uiTestFragments.checkCardExists(editCardData, true);
    }

    @BeforeEach
    void beforeTest() {
        createdCardData = CardFactory.createRandomCard();
        editCardData = CardFactory.createRandomCard();
        createdCardData.setId(new DbAdapter().addCard(createdCardData));
        testData.addCard(createdCardData);
        testData.addCard(editCardData);
    }

    public void editCardWithUi(Card cardForEditData, Card newCardData) {
        DashboardPage dashboardPage = new DashboardPage(page);
        CardsPage cardsPage = dashboardPage.goToCards();
        cardsPage.waitForPageLoad();
        EditCardPage editCardPage = cardsPage.clickEditCard(cardForEditData.getName());
        editCardPage.waitForPageLoad();
        editCardPage.fillCardForm(newCardData);
        editCardPage.clickSave();
    }
}
