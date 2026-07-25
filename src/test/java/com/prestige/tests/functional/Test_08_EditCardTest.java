package com.prestige.tests.functional;

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

import static com.prestige.tests.TestGroups.CARD;

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
        greateCardInTheDatabase();
        generateCardDataForEdit();
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

    public void generateCardDataForEdit(){
        editCardData = CardFactory.createRandomCard();
        testData.addCard(editCardData);
    }

    public void greateCardInTheDatabase(){
        createdCardData = CardFactory.createRandomCard();
        createdCardData.setId(dbAdapter.addCard(createdCardData));
        testData.addCard(createdCardData);
    }
}
