# Quick Setup Instructions

## The Problem
Your project won't run because it's missing:
1. **JavaFX SDK** (for the GUI)
2. **MySQL JDBC Driver** (for database connection)

## Quick Fix (5 minutes)

### 1. Download JavaFX SDK
- Visit: https://openjfx.io/
- Click "Download" → Choose **JavaFX 21 SDK** → **Windows**
- Extract to: `C:\javafx-sdk-21` (or any location you prefer)

### 2. Download MySQL JDBC Driver
- Visit: https://dev.mysql.com/downloads/connector/j/
- Choose **Platform Independent** → Download ZIP
- Extract and copy `mysql-connector-j-8.x.x.jar` to `JoyMarket/lib/` folder
- Create `lib` folder if it doesn't exist

### 3. Update Scripts
- Open `compile.bat` and `run.bat`
- Change `JAVAFX_PATH=C:\javafx-sdk-21\lib` to match your JavaFX location

### 4. Compile & Run
```batch
cd JoyMarket
compile.bat
run.bat
```

## Alternative: Use an IDE

### Eclipse Setup:
1. Right-click project → Properties
2. Java Build Path → Libraries → Add External JARs
3. Add MySQL connector from `lib/` folder
4. Add all JavaFX jars from `javafx-sdk-21/lib/` folder
5. Run → Run Configurations → Arguments → VM arguments:
   ```
   --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
   ```

### IntelliJ IDEA Setup:
1. File → Project Structure → Libraries
2. Add `lib` folder (for MySQL driver)
3. Add `javafx-sdk-21/lib` folder (for JavaFX)
4. Run → Edit Configurations → VM options:
   ```
   --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
   ```

## Database Setup
1. Make sure MySQL/MariaDB is running
2. Create database: `CREATE DATABASE joymarket;`
3. Run `joymarket.sql` to create tables
4. Check credentials in `DatabaseConnection.java` match your MySQL setup

## Still Having Issues?
Check `SETUP_GUIDE.md` in the project root for detailed troubleshooting.






