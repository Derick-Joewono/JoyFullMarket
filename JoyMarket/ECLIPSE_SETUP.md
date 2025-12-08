# Eclipse Setup Instructions

## Fix "The import javafx cannot be resolved" Error

The `.classpath` file has been updated with the correct JavaFX paths. Follow these steps:

### Step 1: Refresh Eclipse Project
1. Right-click on the `JoyMarket` project in Eclipse
2. Select **Refresh** (or press F5)
3. Wait for Eclipse to rebuild the project

### Step 2: Clean and Rebuild
1. Right-click on the project → **Clean...**
2. Select `JoyMarket` project
3. Click **Clean**
4. Eclipse will rebuild automatically

### Step 3: Configure Run Configuration (Required for Running)

1. Right-click `MainApplication.java` → **Run As** → **Run Configurations...**
2. Select your run configuration (or create new one)
3. Go to **Arguments** tab
4. In **VM arguments**, add:
   ```
   --module-path "C:\Users\moren\OneDrive\Documents\Library\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml
   ```
5. Click **Apply** and **Run**

### Alternative: If Errors Persist

If the import errors still appear after refreshing:

1. **Remove and Re-add JavaFX Libraries:**
   - Right-click project → **Properties**
   - **Java Build Path** → **Libraries** tab
   - Remove any broken JavaFX entries
   - Click **Add External JARs...**
   - Navigate to: `C:\Users\moren\OneDrive\Documents\Library\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib`
   - Select all `.jar` files (javafx.base.jar, javafx.controls.jar, javafx.fxml.jar, javafx.graphics.jar, javafx.swing.jar)
   - Click **Open**

2. **Update MySQL Driver:**
   - In the same **Libraries** tab
   - Remove old MySQL connector (if it shows Mac path)
   - Click **Add External JARs...**
   - Navigate to: `C:\Users\moren\OneDrive\Documents\Library\mysql-connector-j-9.5.0`
   - Select `mysql-connector-j-9.5.0.jar`
   - Click **Open**

3. **Apply** and **OK**

### Verify Fix

After refreshing, the import errors should disappear. If they don't:
- Check that the JavaFX JAR files exist at the specified paths
- Try closing and reopening Eclipse
- Check **Window** → **Preferences** → **Java** → **Installed JREs** to ensure JDK 11+ is configured






