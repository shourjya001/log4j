// CustomRealTimeAppender.java
package com.socgen.dbe;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomRealTimeAppender extends DailyRollingFileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String baseFileName;
    
    public CustomRealTimeAppender() {
        setImmediateFlush(true);  // Enable immediate flushing
        activateOptions();        // Activate options immediately
    }
    
    public synchronized void setFile(String fileName) {
        this.baseFileName = fileName;
        updateFileName();
    }
    
    private void updateFileName() {
        String date = dateFormat.format(new Date());
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
        
        String newFileName = "logs/" + date + "maestroLogs.log";
        try {
            super.setFile(newFileName, true, false, bufferSize); // Disable buffering
            this.setWriter(createWriter(newFileName));  // Create new writer
        } catch (IOException e) {
            LogLog.error("Error setting file name", e);
        }
    }
    
    @Override
    protected void subAppend(org.apache.log4j.spi.LoggingEvent event) {
        String currentDate = dateFormat.format(new Date());
        String expectedFileName = "logs/" + currentDate + "maestroLogs.log";
        
        if (!expectedFileName.equals(getFile())) {
            closeFile();
            updateFileName();
        }
        
        super.subAppend(event);
        getWriter().flush();  // Force flush after each write
    }
}
