package net.antistar.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

/**
 * @author : syfer
 * @version 0.1
 * @since 12. 7. 16.
 */
class AntistarConsoleHandler extends ConsoleHandler implements AntistarHandler {
    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        super.publish(record);
        flush();
    }
}

