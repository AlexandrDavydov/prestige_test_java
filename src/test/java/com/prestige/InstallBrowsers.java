package com.prestige;
import com.microsoft.playwright.Playwright;
public class InstallBrowsers {
    public static void main(String[] args) {
        System.out.println("🚀 Начинаю установку браузеров Playwright...");
        System.out.println("Это займет 2-3 минуты, пожалуйста, подождите.");

        try (Playwright playwright = Playwright.create()) {
            // Устанавливаем Chromium
            System.out.println("📦 Устанавливаю Chromium...");
            playwright.chromium().launch().close();

            // Устанавливаем Firefox
            System.out.println("📦 Устанавливаю Firefox...");
            playwright.firefox().launch().close();

            // Устанавливаем WebKit (Safari)
            System.out.println("📦 Устанавливаю WebKit...");
            playwright.webkit().launch().close();

            System.out.println("✅ Все браузеры успешно установлены!");
            System.out.println("📁 Они сохранены в: C:\\Users\\ALEKSANDER\\.cache\\ms-playwright");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при установке: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
