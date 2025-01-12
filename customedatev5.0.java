package com.socgen.dbe;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomRealTimeAppender extends FileAppender {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String lastFileName;

  // private boolean appendToFile = false; // Add this field to control append behavior
    
  //   public void setAppendToFile(boolean append) {
  //       this.appendToFile = append;
  //   }
    
    @Override
    public void activateOptions() {
        updateFileName();
        super.activateOptions();
    }
    
    private void updateFileName() {
        try {
            String date = dateFormat.format(new Date());
            File logsDir = new File("logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }
            
              String newFileName = "logs/maestrologs_" + date + ".log";
            
            if (!newFileName.equals(lastFileName)) {
                lastFileName = newFileName;
                setFile(newFileName, true, bufferedIO, bufferSize);
            }
        } catch (IOException e) {
            LogLog.error("Error updating log file", e);
        }
    }
    
    @Override
    protected void subAppend(org.apache.log4j.spi.LoggingEvent event) {
        String currentDate = dateFormat.format(new Date());
        String expectedFileName = "logs/maestrologs_" + currentDate + ".log";
        
        if (!expectedFileName.equals(lastFileName)) {
            updateFileName();
        }
        
        super.subAppend(event);
        if (qw != null) {
            qw.flush();
        }
    }
}
