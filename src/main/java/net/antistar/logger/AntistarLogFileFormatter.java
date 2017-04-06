package net.antistar.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 12. 7
 */
class AntistarLogFileFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        // [%d{yyyy-MM-dd HH:mm:ss}] [%-5p] [%t] | %-17c{2} | %m%n
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date(logRecord.getMillis()));

        String priority = logRecord.getLevel().getName();
        String sourceClassName = logRecord.getSourceClassName();
        if( sourceClassName == null) {
        	sourceClassName = logRecord.getLoggerName();
        }
        String name;
//        // debug이하 로그일경우 메써드 이름도 표시한다.
//        if( logRecord.getLevel().intValue() <= Level.FINE.intValue() )
//        	name = loggerName.substring(loggerName.lastIndexOf(".")+1)+"#"+logRecord.getSourceMethodName();
//        else
        name = sourceClassName.substring(sourceClassName.lastIndexOf(".")+1);

        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append("[").append(date).append("]")
                 .append("[").append(priority).append("]")
                 .append("[").append(String.valueOf(logRecord.getThreadID())).append("]")
                 .append("[").append(name).append("] ")
                 .append(logRecord.getMessage()).append("\n");
        if(logRecord.getThrown() != null) {
        	logBuffer.append(generateStackTrace(logRecord.getLevel(), logRecord.getThrown()));
        }
        return logBuffer.toString();
    }

    private StringBuilder generateStackTrace(Level level, Throwable thrown) {
        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append(thrown.getClass().getName()).append(':').append(thrown.getMessage() == null? "" : thrown.getMessage()).append("\n");
        if(level.intValue() <= Level.FINE.intValue()) {
            StackTraceElement[] stackTraceElements = thrown.getStackTrace();
            for(StackTraceElement stackTraceElement:stackTraceElements) {
                logBuffer.append("\tat ").append(stackTraceElement.toString()).append("\n");
            }

            if(thrown.getCause() != null) {
                logBuffer.append("Cause by: ").append(generateStackTrace(level, thrown.getCause()));
            }
        }
        return logBuffer;
    }
}
