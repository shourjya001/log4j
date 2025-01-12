// CustomRealTimeAppender.java
package com.socgen.dbe;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomRealTimeAppender extends DailyRollingFileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String baseFileName;
    private Writer writer;
    
    public CustomRealTimeAppender() {
        setImmediateFlush(true);
        activateOptions();
    }
    
    @Override
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
            // Close existing writer if any
            if (writer != null) {
                writer.close();
            }
            
            // Create new writer
            writer = new BufferedWriter(new FileWriter(newFileName, true));
            super.setFile(newFileName, true, false, bufferSize);
            
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
        
        // Flush the writer
        try {
            if (writer != null) {
                writer.flush();
            }
        } catch (IOException e) {
            LogLog.error("Error flushing writer", e);
        }
    }
    
    // Override closeFile to properly close our writer
    @Override
    public void closeFile() {
        try {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            LogLog.error("Error closing writer", e);
        }
        super.closeFile();
    }
}
