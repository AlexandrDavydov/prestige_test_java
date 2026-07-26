# Prestige Test

UI-тестовый фреймворк на **Java 17 + Playwright + JUnit 5** для тестирования веб-приложения [Prestige](https://github.com/lexx918/prestige) (система управления спортивной школой/клубом).  
Используется **SQLite** для тестовых данных, **Allure** для отчётов, **Lombok** для уменьшения бойлерплейта.

---

## Стек

- **Java 17**, Maven
- **Playwright** — браузерная автоматизация
- **JUnit 5** — тестовый раннер
- **SQLite** — подготовка и очистка тестовых данных
- **Lombok** — модели
- **Faker / Russian Data Generator** — генерация тестовых данных
- **Allure** — отчёты

---

## Архитектурные паттерны

### 1. Page Object Model (POM) + Page Factory

- **BasePage** — абстрактный базовый класс со общими действиями (навигация, flash-сообщения, модалки удаления)
- Специализированные страницы: `StudentsPage`, `CoachesPage`, `CardsPage`, `LessonsPage`, `LessonTemplatesPage`, `DashboardPage`
- Страницы создания/редактирования: `AddStudentPage`, `EditStudentPage`, `AddCoachPage` и т.д.
- Навигация через методы `goToStudents()`, `goToCoaches()` — возвращают новые Page Object, поддерживая чейнинг

### 2. Hexagonal / Ports & Adapters (упрощённый)

- **Domain модели** (`Student`, `Coach`, `Card`, `Lesson`, `LessonTemplate`) — чистые POJO с Lombok (`@Data`, `@Builder`)
- **DbAdapter** — порт/адаптер для работы с БД (SQLite), изолирует тесты от схемы БД
- **TestData** — контейнер тестовых данных, управляет жизненным циклом сущностей и чистит БД после тестов

### 3. Factory Pattern для тестовых данных

- `StudentFactory`, `CoachFactory`, `CardFactory`, `LessonFactory`, `LessonTemplateFactory`
- Используют **Faker** + **RussianDataGenerator** для реалистичных русских ФИО, телефонов, дат
- Методы: `createRandom()`, `createWithCustomName()`, `createWithCustomData()`

### 4. BaseTest + Resource Locking (JUnit 5 Parallel Execution)

- `BaseTest` управляет Playwright (`Page`, `Browser`, `BrowserContext`) и Allure шагами
- `@ResourceLock` (`LOCK_STUDENT`, `LOCK_COACH` и др.) — параллельный запуск без конфликтов данных между тестами одной группы
- `@Tag` для группировки (`student`, `coach`, `card`, `smoke`) — запуск через `mvn test -Dgroups=student`

### 5. Test Data Management (DB-first подход)

- `TestData` хранит созданные сущности в памяти теста
- `@AfterEach` / `deleteTestData()` — чистит БД через `DbAdapter` по именам/ID
- `DbAdapter` — **ThreadLocal** + connection pooling (SQLite WAL mode, busy_timeout), реализует `AutoCloseable`

### 6. Step Helper + Allure Integration

- `StepHelper.step(name, action)` — обёртка над `Allure.step`, делает шаги читаемыми в отчёте
- `AllureAttachments` — скриншоты, HTML, URL, JSON данных, консоль браузера

### 7. Configuration Management

- `TestConfig` — приоритет: System Property → Env Var → `config.properties` → дефолт
- Поддерживает: `base.url`, `headless`, `browser.type`, `db.url`, credentials, viewport, locale

---

## Best Practices

| Паттерн / Практика | Где применено | Практическая польза |
|---|---|---|
| Page Object + Fluent Navigation | `BasePage.goToStudents()` → `StudentsPage` | Чистые тесты, нет селекторов в тестах, легко менять UI |
| Builder Pattern (Lombok @Builder) | Все модели (`Student.builder()...`) | Чистое создание объектов, именованные параметры, иммутабельность |
| Factory Pattern | `StudentFactory.createRandomStudent()` | Переиспользуемые реалистичные данные, изоляция генерации |
| ThreadLocal DbAdapter | `DbAdapter.getInstance()` | Безопасная параллельность, изоляция БД на поток |
| ResourceLock (JUnit 5) | `@ResourceLock(LOCK_STUDENT)` | Параллельный запуск тестов без конфликтов данных |
| Tag-based grouping | `@Tag(STUDENT)` + `mvn test -Dgroups=student` | Гибкий запуск: smoke, регресс, по модулям |
| Test Data Cleanup (DB) | `TestData.deleteTestData()` в `@AfterEach` | Чистое состояние БД между тестами, нет флаков |
| Allure Steps + Attachments | `StepHelper.step()`, `AllureAttachments` | Понятные отчёты, быстрая отладка (скриншоты, HTML, JSON) |
| Config via Env/Props | `TestConfig.resolve()` | Один код — разные окружения (local, CI, Docker) без пересборки |
| AutoCloseable + Try-with-resources | `DbAdapter`, `Connection` | Нет утечек соединений, надежная работа с БД |
| UiTestFragments (Page Object Composition) | `UiTestFragments.login()`, `checkStudentExists()` | DRY — общие сценарии вынесены, тесты лаконичны |

---

## Теги (группы)

- `student` — тесты учеников
- `coach` — тесты тренеров
- `card` — тесты абонементов
- `lesson-template` — тесты шаблонов занятий
- `lesson` — тесты занятий
- `smoke` — smoke-тесты

Запуск конкретной группы:
```bash
mvn test -Dgroups=card
```

---

## Структура тестов (функциональные)

| Тест | Группа | Что проверяет |
|---|---|---|
| `Test_01_CreateStudentTest` | student | Создание ученика через UI |
| `Test_02_EditStudentTest` | student | Редактирование |
| `Test_03_DeleteStudentTest` | student | Удаление |
| `Test_04_CreateCoachTest` | coach | Создание тренера |
| `Test_05_EditCoachTest` | coach | Редактирование тренера |
| `Test_06_DeleteCoachTest` | coach | Удаление тренера |
| `Test_07_CreateCardTest` | card | Создание абонемента |
| `Test_08_EditCardTest` | card | Редактирование |
| `Test_09_DeleteCardTest` | card | Удаление |
| `Test_10_CreateLessonTemplateTest` | lesson-template | Создание шаблона занятия |
| `Test_11_EditLessonTemplateTest` | lesson-template | Редактирование шаблона |
| `Test_12_DeleteLessonTemplateTest` | lesson-template | Удаление шаблона |
| `Test_13_CreateLessonTest` | lesson | Создание занятия |
| `Test_14_EditLessonTest` | lesson | Редактирование занятия |
| `Test_15_DeleteLessonTest` | lesson | Удаление занятия |
| `Test_16_StudentsBirthdayTest` | student | Дни рождения (спец. кейс) |
| `Test_17_CoachConductsTheLessonTest` | coach + lesson | Проведение занятия тренером |

---

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
# или с кастомным URL
APP_URL=http://my-server:5000 run-tests.bat
```

---

## Параллельный запуск

Классы из разных групп (`student`, `coach`, `card` и др.) запускаются параллельно (4 потока).  
Классы внутри одной группы бегут последовательно за счёт `@ResourceLock`.

---

## Page Objects

- `CardsPage`, `AddCardPage`, `EditCardPage` — абонементы
- `CoachesPage`, `AddCoachPage`, `EditCoachPage` — тренеры
- `StudentsPage`, `AddStudentPage`, `EditStudentPage` — ученики
- `LessonTemplatesPage`, `AddLessonTemplatePage`, `EditLessonTemplatePage` — шаблоны занятий
- `LessonsPage`, `AddLessonPage`, `EditLessonPage` — занятия
- `DashboardPage` — главная страница
- `PlaywrightLoginPage` — авторизация

---

## CI/CD (GitHub Actions + Docker)

- `.github/workflows/test.yml` — запуск тестов в контейнере
- `Dockerfile.test` — образ с Playwright браузерами
- `run-tests.sh` / `run-tests.bat` — локальный запуск с параметрами (`APP_URL`, `HEADLESS`)

---

## Практическая ценность

- **Надёжность** — изоляция тестов через БД + ResourceLock позволяет запускать параллельно без флаков
- **Скорость разработки** — фабрики данных + POM + фрагменты UI = новый тест за 10–20 строк
- **Отладка** — Allure отчёты со скриншотами/HTML/JSON на каждом шаге
- **Масштабируемость** — hexagonal-подход позволяет заменить Playwright на API-тесты или Selenium без переписывания бизнес-логики
- **Простота CI** — один Docker-образ, запуск через env vars, поддержка тегов/групп