# Интеграция E2E-тестов в пайплайн приложения

## Вариант A: Отдельный репозиторий тестов

Добавить в `.github/workflows/deploy.yml` тестируемого проекта (`C:\Users\ALEKSANDER\PycharmProjects\prestige\`)

### Изменения в deploy.yml

```yaml
name: CI/CD

on:
  push:
    branches: [main, master]
  workflow_dispatch:

jobs:
  e2e-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          path: app

      - name: Checkout E2E tests
        uses: actions/checkout@v4
        with:
          repository: AlexandrDavydov/prestige_test_java
          ref: ci_cd_integration
          path: tests
          token: ${{ secrets.GH_PAT }}

      - name: Build app Docker image
        run: docker build -t prestige-app ./app

      - name: Start app container
        run: |
          docker run -d --name app \
            -p 5000:5000 \
            -v ${{ github.workspace }}/instance:/app/instance \
            prestige-app
          sleep 5
          curl --retry 10 --retry-delay 2 --retry-connrefused http://localhost:5000

      - name: Set up Java 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
          cache-dependency-path: tests/pom.xml

      - name: Cache Playwright browsers
        uses: actions/cache@v4
        with:
          path: ~/.cache/ms-playwright
          key: playwright-${{ runner.os }}-${{ hashFiles('tests/pom.xml') }}

      - uses: actions/setup-node@v4
        with:
          node-version: 20

      - name: Install Playwright browsers
        run: npx playwright install chromium

      - name: Run E2E tests
        run: |
          cd tests
          mvn test \
            -Dbase.url=http://localhost:5000 \
            -Dheadless=true \
            -Ddb.url=jdbc:sqlite:${{ github.workspace }}/instance/db.db

      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: e2e-results
          path: tests/target/surefire-reports/

  build-and-deploy:
    needs: e2e-tests  # <-- деплой только при успешных тестах
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: docker/setup-qemu-action@v3
      - uses: docker/setup-buildx-action@v3
        with:
          driver: docker-container
          driver-opts: image=moby/buildkit:latest
      - uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_TOKEN }}
      - uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          platforms: linux/amd64
          tags: |
            aleksander75/prestige-app:latest
            aleksander75/prestige-app:${{ github.sha }}
          cache-from: type=gha
          cache-to: type=gha,mode=max
      - uses: appleboy/ssh-action@v1.0.0
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          password: ${{ secrets.SERVER_PASSWORD }}
          port: 22
          script: |
            docker stop prestige-app || true
            docker rm prestige-app || true
            docker pull aleksander75/prestige-app:latest
            docker run -d \
              --name prestige-app \
              -p 80:5000 \
              -v /var/lib/prestige/instance:/app/instance \
              --restart always \
              aleksander75/prestige-app:latest
```

### Какая ветка тестов забирается

Параметр `ref: main` в `actions/checkout` указывает какую ветку тестового репозитория использовать. Варианты:

| `ref` | Поведение |
|-------|-----------|
| `master` | Всегда самая свежая стабильная версия тестов |
| `${{ github.ref_name }}` | Ветка, соответствующая ветке тестируемого приложения (например, `develop` → `develop`) |
| `v1.0` | Фиксированный тег (релизная версия) |

Если `ref` не указан — берётся default branch репозитория.

### Настройка секретов GitHub

| Secret | Значение |
|--------|----------|
| `GH_PAT` | Personal Access Token с доступом к репозиторию тестов |

---

## Вариант C: Монорепозиторий (проще)

Папка `tests/` кладётся прямо в тестируемый проект:

```
prestige/
├── .github/workflows/deploy.yml
├── app.py
├── tests/                ← скопировать престиж_тест сюда
│   ├── pom.xml
│   └── src/
```

Тогда workflow не требует `GH_PAT` и кросс-репо доступа:

```yaml
e2e-tests:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v4

    - name: Build & start app
      run: |
        docker build -t prestige-app .
        docker run -d --name app \
          -p 5000:5000 \
          -v ${{ github.workspace }}/instance:/app/instance \
          prestige-app
        sleep 5
        curl --retry 10 --retry-delay 2 --retry-connrefused http://localhost:5000

    - name: Set up Java 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
        cache-dependency-path: tests/pom.xml

    - uses: actions/setup-node@v4
      with:
        node-version: 20

    - name: Install Playwright browsers
      run: npx playwright install chromium

    - name: Run E2E tests
      run: |
        cd tests
        mvn test \
          -Dbase.url=http://localhost:5000 \
          -Dheadless=true \
          -Ddb.url=jdbc:sqlite:${{ github.workspace }}/instance/db.db

    - name: Upload test results
      if: always()
      uses: actions/upload-artifact@v4
      with:
        name: e2e-results
        path: tests/target/surefire-reports/
```
