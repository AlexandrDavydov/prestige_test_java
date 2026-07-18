# Prestige Test

UI-автотесты для приложения [Prestige](https://github.com/lexx918/prestige) на базе Playwright + Java + JUnit 5.

## Стек

- **Java 17**, Maven
- **Playwright** — браузерная автоматизация
- **JUnit 5** — тестовый раннер
- **SQLite** — подготовка и очистка тестовых данных
- **Lombok** — модели
- **Faker / Russian Data Generator** — генерация тестовых данных

## Структура тестов

| Тест             | Сущность  | Действие  |
|------------------|-----------|-----------|
| `Test_01`        | Student   | Создание  |
| `Test_02`        | Student   | Редактирование |
| `Test_03`        | Student   | Удаление  |
| `Test_04`        | Coach     | Создание  |
| `Test_05`        | Coach     | Редактирование |
| `Test_06`        | Coach     | Удаление  |
| `Test_07`        | Card      | Создание  |
| `Test_08`        | Card      | Редактирование |
| `Test_09`        | Card      | Удаление  |

## Тестовая документация

Благодаря **Hexagonal Architecture** (порт-адаптер), тестовые сценарии отделены от инфраструктуры. Это позволяет:

- **Экспортировать** тестовые сценарии в отдельный документ (например, спецификация в `TestDescription`)
- **Загружать** в TMS (TestRail, Zephyr и др.) без привязки к реализации
- Использовать одни и те же сценарии для ручного и автоматизированного прогона

Пример сценария в коде — класс `TestDescription` содержит название и шаги теста без привязки к UI/API.

## Теги (группы)

- `student` — тесты учеников
- `coach` — тесты тренеров
- `card` — тесты абонементов
- `smoke` — smoke-тесты

Запуск конкретной группы:

```bash
mvn test -Dgroups=card
```

## Запуск

### Локально

```bash
mvn test
mvn test -Dgroups=smoke
mvn test -Dheadless=true
```

### Docker

```bash
run-tests.bat
# или
APP_URL=http://my-server:5000 run-tests.bat
```

## Параллельный запуск

Классы из разных групп (`student`, `coach`, `card`) запускаются параллельно (4 потока).  
Классы внутри одной группы бегут последовательно за счёт `@ResourceLock`.

## Page Objects

- `CardsPage`, `AddCardPage`, `EditCardPage` — абонементы
- `CoachesPage`, `AddCoachPage`, `EditCoachPage` — тренеры
- `StudentsPage`, `AddStudentPage`, `EditStudentPage` — ученики
- `DashboardPage` — главная страница
