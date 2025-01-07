// CustomDateFileAppender.java
package com.socgen.dbe;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFileAppender extends DailyRollingFileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String baseFileName;
    
    public synchronized void setFile(String fileName) {
        this.baseFileName = fileName;
        String date = dateFormat.format(new Date());
        // Create filename with date prefix
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
        String newFileName = "logs/" + date + "maestroLogs.log";
        try {
            super.setFile(newFileName, true, bufferedIO, bufferSize);
        } catch (IOException e) {
            LogLog.error("Error setting file name", e);
        }
    }
    
    public void subAppend(org.apache.log4j.spi.LoggingEvent event) {
        String currentDate = dateFormat.format(new Date());
        String expectedFileName = "logs/" + currentDate + "maestroLogs.log";
        
        if (!expectedFileName.equals(getFile())) {
            // Date has changed, need new file
            this.closeFile();
            setFile(baseFileName);
        }
        super.subAppend(event);
    }
}
