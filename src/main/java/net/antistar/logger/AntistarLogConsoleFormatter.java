package net.antistar.logger;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 12. 7
 */
class AntistarLogConsoleFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        // [%d{yyyy-MM-dd HH:mm:ss}][%-5p][%t] %m%n
        // [2010-12-13 06:00:32][FATAL][ThreadName] message\n
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date(logRecord.getMillis()));

        String priority = logRecord.getLevel().getName();
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("[").append(date).append("]")
                 .append("[").append(priority).append("]")
                 .append("[").append(logRecord.getThreadID()).append("] ")
                 .append(logRecord.getMessage()).append("\n");

        return logBuffer.toString();
    }
}
