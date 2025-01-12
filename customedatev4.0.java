package com.socgen.dbe;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomRealTimeAppender extends DailyRollingFileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public void activateOptions() {
        try {
            String date = dateFormat.format(new Date());
            File logsDir = new File("logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }
            
            String fileName = "logs/" + date + "maestroLogs.log";
            setFile(fileName, true, false, bufferSize);
        } catch (IOException e) {
            LogLog.error("Error initializing log file", e);
        }
        super.activateOptions();
    }
    
    @Override
    protected void subAppend(org.apache.log4j.spi.LoggingEvent event) {
        try {
            String currentDate = dateFormat.format(new Date());
            String expectedFileName = "logs/" + currentDate + "maestroLogs.log";
            
            if (!expectedFileName.equals(getFile())) {
                this.closeFile();
                setFile(expectedFileName, true, false, bufferSize);
            }
            
            super.subAppend(event);
            qw.flush();
        } catch (IOException e) {
            LogLog.error("Error in logging", e);
        }
    }
}
