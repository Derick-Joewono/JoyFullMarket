@echo off
REM ============================================
REM JoyMarket Run Script
REM ============================================
REM Before using this script:
REM 1. Make sure compile.bat ran successfully
REM 2. Update JAVAFX_PATH below to match compile.bat
REM 3. Ensure MySQL database is running
REM ============================================

REM JavaFX path (update if your SDK is elsewhere)
set JAVAFX_PATH=C:\Users\moren\OneDrive\Documents\Library\openjfx-17.0.7_windows-x64_bin-sdk\javafx-sdk-17.0.7\lib
set MYSQL_DRIVER=C:\Users\moren\OneDrive\Documents\Library\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar

REM Check if bin directory exists
if not exist "bin" (
    echo ERROR: bin directory not found!
    echo Please run compile.bat first
    pause
    exit /b 1
)

REM Check if JavaFX path exists
if not exist "%JAVAFX_PATH%" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    echo Please download JavaFX SDK from https://openjfx.io/
    echo and update JAVAFX_PATH in this script
    pause
    exit /b 1
)

echo ============================================
echo Starting JoyMarket Application...
echo ============================================
echo.

REM Run the application with JavaFX modules
java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "bin;%MYSQL_DRIVER%" main.MainApplication

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ============================================
    echo Application failed to start!
    echo ============================================
    echo Please check:
    echo 1. Database is running and accessible
    echo 2. Database 'joymarket' exists
    echo 3. All dependencies are correctly configured
    echo ============================================
    pause
)

