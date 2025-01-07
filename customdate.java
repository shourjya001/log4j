// CustomDailyFolderAppender.java
import org.apache.log4j.DailyRollingFileAppender;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDailyFolderAppender extends DailyRollingFileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public void setFile(String fileName) {
        String date = dateFormat.format(new Date());
        // Create date-specific folder path
        String folderPath = "logs/" + date;
        // Create full file path including date folder
        String newFileName = folderPath + "/maestroLogs.log";
        
        // Create directory if it doesn't exist
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        super.setFile(newFileName);
    }
    
    @Override
    public void rollOver() {
        // Get tomorrow's date for the new file
        String newDate = dateFormat.format(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        String newFolderPath = "logs/" + newDate;
        
        // Create tomorrow's directory
        File directory = new File(newFolderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Set the new file path
        String newFileName = newFolderPath + "/maestroLogs.log";
        super.setFile(newFileName);
        
        // Call parent rollOver to handle file creation
        super.rollOver();
    }
}
