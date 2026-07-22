package com.prestige.utils;

import io.qameta.allure.Allure;

import java.util.function.Supplier;

public class StepHelper {

    public static void step(String name, Runnable action) {
        Allure.step(name, () -> action.run());
    }

    public static <T> T step(String name, Supplier<T> action) {
        return Allure.step(name, action::get);
    }
}
