package net.antistar.logger.tx;

import net.antistar.logger.AntistarLogger;
import net.antistar.logger.AntistarLoggerManager;

/**
 * @author : syfer
 * @version 0.1
 * @since 12. 7. 5.
 */
public class TxLogger {
    private static final AntistarLogger logger = AntistarLoggerManager.getTxLogger();
    private static ThreadLocal<TxRecord> record = new ThreadLocal<TxRecord>();
    private static String apiName = "X";

    public static void end(TxRecord record, int status, int respCode, int internalCode) {
        if(logger == null) {
            return;
        }
        if(record != null) {
            record.setStatus(status, respCode, internalCode);
            logger.info(record.getMessage());
            TxLogger.record.remove();
        }
    }

    public static void setAPIName(String apiName) {
        TxLogger.apiName = apiName;
    }

    public static String getAPIName() {
        return apiName;
    }
}
