// CustomDailyFolderAppender.java
package com.socgen.dbe;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class CustomDailyFolderAppender extends DailyRollingFileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // Remove @Override since setFile is protected in parent class
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) {
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
        
        try {
            super.setFile(newFileName, append, bufferedIO, bufferSize);
        } catch (IOException e) {
            LogLog.error("Error setting file name", e);
        }
    }
    
    // Instead of overriding rollOver, we'll implement our own date change handling
    public void subAppend(org.apache.log4j.spi.LoggingEvent event) {
        String currentDate = dateFormat.format(new Date());
        String currentFilePath = getFile();
        
        if (currentFilePath != null && !currentFilePath.contains(currentDate)) {
            // Date has changed, need to update file location
            String newFolderPath = "logs/" + currentDate;
            String newFileName = newFolderPath + "/maestroLogs.log";
            
            // Create new directory
            File directory = new File(newFolderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Close current file and open new one
            this.closeFile();
            setFile(newFileName, true, bufferedIO, bufferSize);
        }
        
        super.subAppend(event);
    }
}
