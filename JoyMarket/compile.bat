@echo off
REM ============================================
REM JoyMarket Compilation Script
REM ============================================
REM Before using this script:
REM 1. Download JavaFX SDK from https://openjfx.io/
REM 2. Update JAVAFX_PATH below to point to your JavaFX SDK lib folder
REM 3. Download MySQL JDBC driver and place it in JoyMarket/lib/ folder
REM ============================================

REM JavaFX path (update if your SDK is elsewhere)
set JAVAFX_PATH=C:\Users\moren\OneDrive\Documents\Library\openjfx-17.0.7_windows-x64_bin-sdk\javafx-sdk-17.0.7\lib
set MYSQL_DRIVER=C:\Users\moren\OneDrive\Documents\Library\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar

REM Check if JavaFX path exists
if not exist "%JAVAFX_PATH%" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    echo Please download JavaFX SDK from https://openjfx.io/
    echo and update JAVAFX_PATH in this script
    pause
    exit /b 1
)

REM Check if MySQL driver exists
if not exist "%MYSQL_DRIVER%" (
    echo WARNING: MySQL JDBC driver not found at %MYSQL_DRIVER%
    echo Please download from https://dev.mysql.com/downloads/connector/j/
    echo and place it in JoyMarket/lib/ folder
    pause
)

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile with JavaFX modules
echo Compiling JoyMarket project...
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -d bin -cp "%MYSQL_DRIVER%" -sourcepath src src\main\MainApplication.java src\view\*.java src\controller\*.java src\model\*.java src\repository\*.java src\helper\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo Compilation successful!
    echo ============================================
    echo Run the project using: run.bat
    echo ============================================
) else (
    echo.
    echo ============================================
    echo Compilation failed!
    echo ============================================
    echo Please check:
    echo 1. JavaFX SDK path is correct
    echo 2. MySQL JDBC driver is in lib/ folder
    echo 3. All source files are present
    echo ============================================
)

pause

