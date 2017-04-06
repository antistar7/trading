package net.antistar.logger;

import net.antistar.logger.json.JSONArray;
import net.antistar.logger.json.JSONObject;
import net.antistar.logger.json.XML;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 12. 7
 */
public class AntistarLoggerManager {
    public static final String DEFAULT_LOG_FILE_NAME = "antistar.out";
    private static final String LOGGER_NAME_SEPARATOR = ".";

    private static final String LOGGER_SETTING_FILE_KEY = "com.ucloud.logging.setting";

    private static final String ROOT_LOGGER_NAME = "com.ucloud";
    private static final String TX_LOGGER_NAME = "mstf.ucloud.txlogging";

    private static final String FILE_HANDLER_NAME = "file-handler";
    private static final String CONSOLE_HANDLER_NAME = "console-handler";
    private static final String TX_HANDLER_NAME = "tx-handler";

    private static final String DEFAULT_TX_LOG_FILE_NAME = "tx.log";
    private static final String DEFAULT_LOG_DIR = ".";
    private static final String DEFAULT_LOG_CYCLE = "daily";

    private static final String LOGGING_ROOT_ELEMENT_NAME = "ucloud-logging";
    private static final String SYSTEM_LOGGER_ROOT_ELEMENT = "system-logger";
    private static final String UCLOUD_LOGGER_ROOT_ELEMENT = "ucloud-logger";
    private static final String TX_LOGGER_ELEMENT_NAME = "tx-logger";

    private static final String LOG_PATH_ELEMENT_NAME = "log-path";
    private static final String LOG_LEVEL_ELEMENT_NAME = "log-level";
    private static final String LOG_ROLLING_ELEMENT_NAME = "rolling";
    private static final String LOGGER_NAME_ELEMENT_NAME = "logger-name";

    private static AntistarLoggerManager instance;
    private Map<String, AntistarLogger> loggerMap = new HashMap<String, AntistarLogger>();

    private static AntistarLogFileFormatter logFormatter = new AntistarLogFileFormatter();
    private static AntistarLogTxFileFormatter txFileFormatter = new AntistarLogTxFileFormatter();
    private static AntistarLogConsoleFormatter consoleFormatter = new AntistarLogConsoleFormatter();

    private AntistarLogger rootLogger;
    private AntistarLogger txLogger;


    public static AntistarLoggerManager getInstance() {
        if(instance == null) {
            instance = new AntistarLoggerManager();
        }
        return instance;
    }

    private AntistarLoggerManager() {
        rootLogger = new AntistarLogger(ROOT_LOGGER_NAME);
        txLogger = new AntistarLogger(rootLogger, TX_LOGGER_NAME);
        loggerMap.put(rootLogger.getLoggerName(), rootLogger);
        loggerMap.put(txLogger.getLoggerName(), txLogger);

        //todo 단계별로 여러 곳에서 설정을 loading할 수 있도록 해야함.
        String settingXMLPath = System.getProperty(LOGGER_SETTING_FILE_KEY);
        BufferedReader br = null;
        if(settingXMLPath != null) {
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(settingXMLPath))));
                StringBuilder buffer = new StringBuilder();
                String line;
                while( (line = br.readLine() ) != null) {
                    buffer.append(line);
                }

                reloadConfiguration(buffer.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        System.err.print("Cannot close setting file ");
                    }
                }
            }
        } else {
            loadDefaultSetting();
        }
    }

    private void loadDefaultSetting() {
        AntistarDailyRollingLogFileHandler fileHandler = new AntistarDailyRollingLogFileHandler(DEFAULT_LOG_CYCLE, DEFAULT_LOG_DIR + File.separator + DEFAULT_LOG_FILE_NAME);
        //AntistarConsoleHandler consoleHandler = new AntistarConsoleHandler();
        fileHandler.setFormatter(logFormatter);
        //consoleHandler.setFormatter(consoleFormatter);
        rootLogger.addHandler(FILE_HANDLER_NAME, fileHandler);
        //rootLogger.addHandler(CONSOLE_HANDLER_NAME, consoleHandler);

        AntistarDailyRollingLogFileHandler txLogHandler = new AntistarDailyRollingLogFileHandler(DEFAULT_LOG_CYCLE, DEFAULT_LOG_DIR + File.separator + DEFAULT_TX_LOG_FILE_NAME);
        txLogHandler.setFormatter(txFileFormatter);
        txLogger.addHandler(TX_HANDLER_NAME, txLogHandler);
    }

    public void reloadConfiguration(String xml) {
        JSONObject setting = XML.toJSONObject(xml).getJSONObject(LOGGING_ROOT_ELEMENT_NAME);
        reloadConfiguration(setting);
    }

    public void reloadConfiguration(JSONObject setting) {
        JSONObject txLoggerSetting = setting.getJSONObject(TX_LOGGER_ELEMENT_NAME);
        if(txLoggerSetting != null) {
            String txLogPath = txLoggerSetting.getString(LOG_PATH_ELEMENT_NAME) == null?DEFAULT_LOG_DIR + File.separator + DEFAULT_TX_LOG_FILE_NAME:txLoggerSetting.getString(LOG_PATH_ELEMENT_NAME);
            String cycle = txLoggerSetting.getString(LOG_ROLLING_ELEMENT_NAME) == null?DEFAULT_LOG_CYCLE:txLoggerSetting.getString(LOG_ROLLING_ELEMENT_NAME);

            AntistarDailyRollingLogFileHandler handler = new AntistarDailyRollingLogFileHandler(cycle, txLogPath);
            handler.setFormatter(txFileFormatter);
            txLogger.removeHandler(TX_HANDLER_NAME);
            txLogger.addHandler(TX_HANDLER_NAME, handler);
        }

        JSONObject systemLoggerSetting = setting.getJSONObject(SYSTEM_LOGGER_ROOT_ELEMENT);
        JSONArray loggerArray = systemLoggerSetting.getJSONArray(UCLOUD_LOGGER_ROOT_ELEMENT);
        int loggerNum = loggerArray.length();
        for(int i = 0; i < loggerNum; i++) {
            JSONObject loggerSetting = loggerArray.getJSONObject(i);
            String loggerName = loggerSetting.getString(LOGGER_NAME_ELEMENT_NAME);
            if(loggerName == null) {
                continue;
            }
            AntistarLogger logger = getLogger(loggerName);
            String logPath = loggerSetting.getString(LOG_PATH_ELEMENT_NAME) == null? logger.getLogPath():loggerSetting.getString(LOG_PATH_ELEMENT_NAME);
            String cycle = loggerSetting.getString(LOG_ROLLING_ELEMENT_NAME) == null?logger.getLogCycle():loggerSetting.getString(LOG_ROLLING_ELEMENT_NAME);
            String level = loggerSetting.getString(LOG_LEVEL_ELEMENT_NAME) == null?logger.getLevel().getName():loggerSetting.getString(LOG_LEVEL_ELEMENT_NAME);
            AntistarDailyRollingLogFileHandler handler = new AntistarDailyRollingLogFileHandler(cycle, logPath);
            handler.setFormatter(logFormatter);
            logger.removeHandler(FILE_HANDLER_NAME);
            logger.addHandler(FILE_HANDLER_NAME, handler);
            logger.setLevel(level);
        }

    }

    public static AntistarLogger getTxLogger() {
        return getInstance().txLogger;
    }

    public synchronized AntistarLogger getLogger(String loggerName) {
        AntistarLogger logger = loggerMap.get(loggerName);
        if(logger == null) {
            logger = createLogger(loggerName);
            loggerMap.put(loggerName, logger);
        }
        return logger;
    }

    private AntistarLogger createLogger(String loggerName) {
        int parentLoggerNameIndex = loggerName.lastIndexOf(LOGGER_NAME_SEPARATOR);
        if(parentLoggerNameIndex == -1) {
            AntistarLogger logger = new AntistarLogger(rootLogger, loggerName);
            loggerMap.put(loggerName, logger);
            return logger;
        } else {
            String parentLoggerName;
            parentLoggerName = loggerName.substring(0, parentLoggerNameIndex);
            AntistarLogger parentLogger;
            parentLogger = getLogger(parentLoggerName);
            AntistarLogger logger = new AntistarLogger(parentLogger, loggerName);
            loggerMap.put(loggerName, logger);
            return logger;
        }

    }
}

