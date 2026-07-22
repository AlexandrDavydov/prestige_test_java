package com.prestige.pages;

import com.microsoft.playwright.Page;
public class EditLessonPage extends BaseLessonPage<EditLessonPage> {

    public EditLessonPage(Page page) {
        super(page);
    }

    @Override
    public boolean isUrlCorrect() {
        return page.url().contains("/edit_lesson");
    }
}
