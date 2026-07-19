#!/bin/bash

# Проверка версии Java
echo "=========================================="
echo "☕ Java version:"
java -version
echo "=========================================="

echo "=========================================="
echo "🚀 Запуск тестов с параметрами:"
echo "   APP_URL: ${APP_URL}"
echo "   HEADLESS: ${HEADLESS}"
echo "   DB_URL: ${DB_URL}"
echo "   VIEWPORT: ${VIEWPORT_WIDTH}x${VIEWPORT_HEIGHT}"
echo "=========================================="

# Запускаем тесты
mvn test \
  -Dbase.url="${APP_URL}" \
  -Dheadless="${HEADLESS}" \
  -Ddb.url="${DB_URL}" \
  -Dapp.username="${APP_USERNAME}" \
  -Dapp.password="${APP_PASSWORD}" \
  -Dviewport.width="${VIEWPORT_WIDTH}" \
  -Dviewport.height="${VIEWPORT_HEIGHT}" \
  -Dbrowser.locale="${BROWSER_LOCALE}" \
  -Dbrowser.type="${BROWSER_TYPE}" \
  -Dscreenshot.dir="${SCREENSHOT_DIR}"

# Сохраняем код возврата
EXIT_CODE=$?

echo "=========================================="
if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ Все тесты успешно выполнены!"
else
    echo "❌ Некоторые тесты завершились с ошибками (код: $EXIT_CODE)"
fi
echo "=========================================="

exit $EXIT_CODE