package com.prestige.utils;

import com.prestige.models.TestDescription;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HtmlReportGenerator {

    /**
     * Генерирует HTML файл с отчетом о сценариях.
     * @param tests Список описаний тестов.
     * @param filePath Путь к файлу, куда сохранить отчет (например, "report.html").
     */
    public static void generateHtmlReport(List<TestDescription> tests, String filePath) {
        StringBuilder html = new StringBuilder();

        // Начало HTML документа и CSS стилей
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<title>Test Suite Report</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; background-color: #f4f7f6; }\n");
        html.append("h1 { color: #333; }\n");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }\n");
        html.append("th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #ddd; }\n");
        html.append("th { background-color: #4CAF50; color: white; }\n");
        html.append("tr:nth-child(even) { background-color: #f9f9f9; }\n");
        html.append(".step-list { margin: 0; padding-left: 20px; font-size: 0.9em; color: #555; } \n");
        html.append(".id-badge { background: #e0e0e0; padding: 4px 8px; border-radius: 4px; font-family: monospace; }\n");
        html.append("</style>\n</head>\n<body>\n");

        html.append("<h1>Test Suite Overview</h1>");
        html.append("<p>Generated on: ").append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("</p>");

        // Начало таблицы
        html.append("<table>\n");
        html.append("<thead>\n<tr><th>ID</th><th>Name</th><th>Steps</th></tr>\n</thead>\n<tbody>\n");

        for (TestDescription test : tests) {
            html.append("<tr>\n");
            // ID теста
            html.append("<td><span class=\"id-badge\">").append(test.getTestId()).append("</span></td>\n");
            // Название теста
            html.append("<td>").append(test.getTestName()).append("</td>\n");

            // Шаги (рендерим как список)
            html.append("<td><ul class=\"step-list\">");
            if (test.getTestSteps() != null && !test.getTestSteps().isEmpty()) {
                for (String step : test.getTestSteps()) {
                    html.append("<li>").append(step).append("</li>");
                }
            } else {
                html.append("<li>No steps defined</li>");
            }
            html.append("</ul></td>\n");

            html.append("</tr>\n");
        }

        html.append("</tbody>\n</table>\n");
        html.append("</body>\n</html>");

        // Запись в файл
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.write(html.toString());
            System.out.println("Report successfully generated at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
