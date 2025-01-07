import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.PatternParser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDailyRollingFileAppender extends DailyRollingFileAppender {

    @Override
    public void setFile(String fileName) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        fileName = fileName.replace("${date}", date);
        super.setFile(fileName);
    }

    @Override
    public void activateOptions() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File file = new File(getFile());
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        super.activateOptions();
    }
}
