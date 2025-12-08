# JoyMarket Project Setup Guide

## Why the Project Won't Run

Your project has **two main missing dependencies**:

1. **JavaFX SDK** - JavaFX is not included in JDK 11+ and needs to be downloaded separately
2. **MySQL JDBC Driver** - Required to connect to your MySQL database

## Solution: Download Required Libraries

### Step 1: Download JavaFX SDK

1. Go to: https://openjfx.io/
2. Download **JavaFX 21 SDK** for Windows (or match your JDK version)
3. Extract it to a folder (e.g., `C:\javafx-sdk-21`)

### Step 2: Download MySQL JDBC Driver

1. Go to: https://dev.mysql.com/downloads/connector/j/
2. Download **Platform Independent** ZIP file
3. Extract `mysql-connector-j-8.x.x.jar` from the zip

### Step 3: Create lib Folder

1. Create a `lib` folder in your `JoyMarket` directory:
   ```
   JoyMarket/
   ├── lib/
   │   ├── mysql-connector-j-8.x.x.jar
   │   └── (JavaFX jars will be referenced from SDK folder)
   ├── src/
   └── ...
   ```

2. Copy the MySQL JDBC jar to `JoyMarket/lib/`

### Step 4: Configure Your IDE

#### Option A: Eclipse/IntelliJ IDEA

**Eclipse:**
1. Right-click project → Properties → Java Build Path → Libraries
2. Add External JARs:
   - Add MySQL connector from `lib/` folder
   - Add all JavaFX jars from `javafx-sdk-21/lib/` folder:
     - `javafx.base.jar`
     - `javafx.controls.jar`
     - `javafx.fxml.jar`
     - `javafx.graphics.jar`
     - `javafx.media.jar` (optional)
     - `javafx.swing.jar` (optional)
     - `javafx.web.jar` (optional)

**IntelliJ IDEA:**
1. File → Project Structure → Libraries
2. Click `+` → Java → Select the `lib` folder
3. Add JavaFX SDK: File → Project Structure → Libraries → `+` → Java → Select `javafx-sdk-21/lib` folder

#### Option B: Command Line Compilation & Run

Use the provided `compile.bat` and `run.bat` scripts (see below).

### Step 5: Set VM Arguments for JavaFX

When running the application, you need to add VM arguments:

```
--module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
```

**In Eclipse:**
- Run → Run Configurations → Arguments → VM arguments → Add the above

**In IntelliJ:**
- Run → Edit Configurations → VM options → Add the above

## Quick Setup Scripts

I've created `compile.bat` and `run.bat` files to help you compile and run the project easily.

**Before using them:**
1. Edit `compile.bat` and `run.bat` 
2. Update the `JAVAFX_PATH` variable to point to your JavaFX SDK location
3. Make sure MySQL connector is in `JoyMarket/lib/` folder

## Database Setup

Make sure:
1. MySQL/MariaDB is running on `localhost:3306`
2. Database `joymarket` exists
3. Run `joymarket.sql` to create tables
4. Database credentials in `DatabaseConnection.java` are correct:
   - User: `root`
   - Password: (empty by default)

## Troubleshooting

### Error: "package javafx does not exist"
- **Solution:** JavaFX SDK not in classpath. Follow Step 1-4 above.

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- **Solution:** MySQL JDBC driver not in classpath. Add it to `lib/` folder.

### Error: "JavaFX runtime components are missing"
- **Solution:** Add VM arguments: `--module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml`

### Error: Database Connection Failed
- **Solution:** 
  1. Check MySQL is running
  2. Verify database `joymarket` exists
  3. Check credentials in `DatabaseConnection.java`






