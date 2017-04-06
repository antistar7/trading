package net.antistar.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author : syfer
 * @version 0.1
 * @since 12. 6. 14.
 */
class AntistarLogTxFileFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append(logRecord.getMessage()).append("\n");
        return logBuffer.toString();
    }
}
