package net.antistar.logger;

import net.antistar.logger.message.AntistarMessageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 12. 7
 */
public class AntistarLogger {
    private AntistarLogger parent;

    private AntistarLevel level = AntistarLevel.INFO;
    private Logger logger;
    private String loggerName;
    private String logPath = ".";
    private String logCycle = "daily";

    private Map<String, AntistarHandler> handlerMap = new HashMap<String, AntistarHandler>();

    AntistarLogger(AntistarLogger parent, String loggerName) {
        this.parent = parent;
        this.level = parent.getLevel();
        this.logCycle = parent.getLogCycle();
        this.loggerName = loggerName;
        logger = Logger.getLogger(loggerName);
        logger.setParent(parent.logger);
    }

    AntistarLogger(String loggerName) {
        // For Root Logger
        this.loggerName = loggerName;
        this.logCycle = "daily";
        this.level = AntistarLevel.INFO;
        this.logPath = ".";

        logger = Logger.getLogger(loggerName);
    }

    public static AntistarLogger getLogger(String clazzName) {
        return AntistarLoggerManager.getInstance().getLogger(clazzName);
    }

    public static AntistarLogger getLogger(Class clazz) {
        return AntistarLoggerManager.getInstance().getLogger(clazz.getCanonicalName());
    }

    public AntistarLogger getParent() {
        return parent;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public AntistarLevel getLevel() {
        return level;
    }

    public String getLogLevel() {
        return level.getName();
    }

    public synchronized void setLevel(AntistarLevel level) {
		if (level == null) {
			setLevel(AntistarLevel.INFO);
			return;
		}
		this.level = level;
		logger.setLevel(level.getLevel());
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			handler.setLevel(level.getLevel());
		}
	}

    public synchronized void setLevel(String level) {
		if (level == null) {
			setLevel(AntistarLevel.INFO);
			return;
		}
		AntistarLevel ucloudLevel = AntistarLevel.getLevel(level);
		if (ucloudLevel == null) {
			setLevel(AntistarLevel.INFO);
        }
		else {
			setLevel(ucloudLevel);
        }
	}

	public boolean isDebugEnabled() {
        return level.intValue() < AntistarLevel.DEBUG.intValue();
	}

	public boolean isLoggable(AntistarLevel ucloudLevel) {
		return level.intValue() <= ucloudLevel.intValue();
	}

    public void log(Object messageID) {
		if (messageID == null) {
			log(AntistarLevel.INFO, "");
			return;
		}
		AntistarLevel level = AntistarMessageManager.getLevel(messageID);
		log(level, messageID);
	}

	public void log(AntistarLevel ucloudLevel, Object messageID) {
		if(!isLoggable(ucloudLevel))
			return;
		if (messageID == null) {
			doLog(null, ucloudLevel, null);
			return;
		}
		if (!AntistarMessageManager.isInitialized()) {
			doLog(null,ucloudLevel, messageID);
			return;
		}

		String message = AntistarMessageManager.getMessage(messageID);
		doLog(null, ucloudLevel, message);
	}

	public void log(AntistarLevel ucloudLevel, Exception e) {
		if( !isLoggable(ucloudLevel) )
			return;
		doLog(e, ucloudLevel, e.getMessage());
	}

	public void log(Object messageID, Throwable e) {
		AntistarLevel level = AntistarMessageManager.getLevel(messageID);
		log(level, messageID, e);
	}

	public void log(AntistarLevel ucloudLevel, Object messageID, Throwable e) {
		if( !isLoggable(ucloudLevel) ) {
			return;
        }
		String message = AntistarMessageManager.getMessage(messageID);
		doLog(e, ucloudLevel, message);
	}

	public void log(Object messageID, Object... args) {
		AntistarLevel level = AntistarMessageManager.getLevel(messageID);
		log(level, messageID, args);
	}

	public void log(AntistarLevel ucloudLevel, Object messageID, Object... args) {
		if( !isLoggable(ucloudLevel) )
			return;
		if (!AntistarMessageManager.isInitialized()) {
			doLog(null,ucloudLevel, messageID);
			return;
		}
		if (isLoggable(ucloudLevel)) {
			String message = AntistarMessageManager.getMessage(messageID, args);
			doLog(null, ucloudLevel, message);
		}
	}

	public void log(Object messageID, Throwable e, Object... args) {
		if (messageID == null) {
			log(AntistarLevel.INFO, "", e, args);
			return;
		}
		AntistarLevel level = AntistarMessageManager.getLevel(messageID);
		log(level, messageID, e, args);
	}

	public void log(AntistarLevel ucloudLevel, Object messageID, Throwable e, Object... args) {
		if( !isLoggable(ucloudLevel) ) {
			return;
        }
		String message = AntistarMessageManager.getMessage(messageID, args);
		doLog(e, ucloudLevel, message);
	}

    public void info(Object messageID) {
        log(AntistarLevel.INFO, messageID);
    }

    public void info(Object messageID, Throwable e) {
		log(AntistarLevel.INFO, messageID, e);
	}

    public void info(Object messageID, Throwable e, Object... args) {
		log(AntistarLevel.INFO, messageID, e, args);
	}

    public void warn(Object messageID) {
		log(AntistarLevel.WARN, messageID);
	}

	public void warn(Object messageID, Throwable e) {
		log(AntistarLevel.WARN, messageID, e);
	}

	public void warn(Object messageID, Throwable e, Object... args) {
		log(AntistarLevel.WARN, messageID, e, args);
	}

	public void fatal(Object messageID) {
		log(AntistarLevel.FATAL, messageID);
	}

	public void fatal(Object messageID, Throwable e) {
		log(AntistarLevel.FATAL, messageID, e);
	}

	public void fatal(Object messageID, Throwable e, Object... args) {
		log(AntistarLevel.FATAL, messageID, e, args);
	}

	public void debug(Object messageID) {
		log(AntistarLevel.DEBUG, messageID);
	}

	public void debug(Object messageID, Throwable e) {
		log(AntistarLevel.DEBUG, messageID, e);
	}

	public void debug(Object messageID, Throwable e, Object... args) {
		log(AntistarLevel.DEBUG, messageID, e, args);
	}

	public void error(Object messageID) {
		log(AntistarLevel.ERROR, messageID);
	}

	public void error(Object messageID, Throwable e) {
		log(AntistarLevel.ERROR, messageID, e);
	}

	public void error(Object messageID, Throwable e, Object... args) {
		log(AntistarLevel.ERROR, messageID, e, args);
	}

	public void trace(Object messageID) {
		log(AntistarLevel.TRACE, messageID);
	}

	public void trace(Object messageID, Throwable e) {
		log(AntistarLevel.TRACE, messageID, e);
	}

	public void trace(Object messageID, Throwable e, Object... args) {
		log(AntistarLevel.TRACE, messageID, e, args);
	}

    void addHandler(String handlerName, AntistarHandler handler) {
        handlerMap.put(handlerName, handler);
        logger.addHandler((Handler) handler);
        logger.setUseParentHandlers(false);
    }

    void removeHandler(String handlerName) {
        Handler handler = (Handler) handlerMap.remove(handlerName);
        if(handlerMap.size() == 0) {
            logger.setUseParentHandlers(true);
        }
        logger.removeHandler(handler);
    }

    AntistarHandler getHandler(String handlerName) {
        return handlerMap.get(handlerName);
    }

    public String getLogPath() {
        return logPath;
    }

    public String getLogCycle() {
        return logCycle;
    }

    private void doLog(Throwable t, AntistarLevel ucLevel, Object msg) {
		// millis and thread are filled by the constructor
        String message;
        if(msg == null) {
            message = "";
        } else {
            message = msg.toString();
        }
		LogRecord record = new LogRecord(ucLevel.getLevel(), message);
		record.setLoggerName(loggerName);
        record.setSourceClassName(loggerName);
		record.setThrown(t);
		logger.log(record);
	}
}

