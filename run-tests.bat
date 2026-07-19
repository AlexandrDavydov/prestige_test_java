@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo   🐳 Running tests in Docker
echo ==========================================

if "%APP_URL%"=="" set APP_URL=http://127.0.0.1:5000

set RESULTS_DIR=%CD%\test-results
if not exist "%RESULTS_DIR%" mkdir "%RESULTS_DIR%"

echo.
echo 📋 Parameters:
echo    APP_URL: %APP_URL%
echo    HEADLESS: %HEADLESS%
echo    Results: %RESULTS_DIR%
echo.

echo 🔨 Building Docker image...
docker build -f Dockerfile.test -t tests-runner .

if errorlevel 1 (
    echo ❌ Docker build failed!
    pause
    exit /b 1
)

echo.
echo 🚀 Running tests...
docker run --rm ^
    -e APP_URL="%APP_URL%" ^
    -e HEADLESS="%HEADLESS%" ^
    -e DB_URL="%DB_URL%" ^
    -e APP_USERNAME="%APP_USERNAME%" ^
    -e APP_PASSWORD="%APP_PASSWORD%" ^
    -e VIEWPORT_WIDTH="%VIEWPORT_WIDTH%" ^
    -e VIEWPORT_HEIGHT="%VIEWPORT_HEIGHT%" ^
    -e BROWSER_LOCALE="%BROWSER_LOCALE%" ^
    -e BROWSER_TYPE="%BROWSER_TYPE%" ^
    -v "%RESULTS_DIR%:/app/target/surefire-reports" ^
    tests-runner

if errorlevel 1 (
    echo.
    echo ❌ Tests failed!
    echo    Reports saved in: %RESULTS_DIR%
) else (
    echo.
    echo ✅ All tests passed!
    echo    Reports saved in: %RESULTS_DIR%
)

echo.
pause