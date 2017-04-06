package net.antistar.logger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class AntistarLevel {
	public static final AntistarLevel ALL = new AntistarLevel(Level.ALL);
    public static final AntistarLevel TRACE = new AntistarLevel(Level.FINEST);
    public static final AntistarLevel FINEST = new AntistarLevel(Level.FINEST);
    public static final AntistarLevel DEBUG = new AntistarLevel(Level.FINER);
    public static final AntistarLevel FINER = new AntistarLevel(Level.FINER);
    public static final AntistarLevel FINE = new AntistarLevel(Level.FINE);
    public static final AntistarLevel CONFIG = new AntistarLevel(Level.CONFIG);
    public static final AntistarLevel INFO = new AntistarLevel(Level.INFO);
    public static final AntistarLevel WARN = new AntistarLevel(Level.WARNING);
    public static final AntistarLevel ERROR = new AntistarLevel(Level.SEVERE);
    public static final AntistarLevel SEVERE = new AntistarLevel(Level.SEVERE);
    public static final AntistarLevel FATAL = new AntistarLevel(Level.SEVERE);
    public static final AntistarLevel OFF = new AntistarLevel(Level.OFF);
    
    private static Map<String, AntistarLevel> stringLevelMapper = new HashMap<String, AntistarLevel>();
    static {
        stringLevelMapper.put("ALL", AntistarLevel.ALL);
        stringLevelMapper.put("TRACE", TRACE);
        stringLevelMapper.put("FINEST", FINEST);
        stringLevelMapper.put("DEBUG", DEBUG);
        stringLevelMapper.put("FINER", FINER);
        stringLevelMapper.put("FINE", FINE);
        stringLevelMapper.put("CONFIG", CONFIG);
        stringLevelMapper.put("INFO", INFO);
        stringLevelMapper.put("WARN", WARN);
        stringLevelMapper.put("ERROR", ERROR);
        stringLevelMapper.put("SEVERE", SEVERE);
        stringLevelMapper.put("FATAL", FATAL);
        stringLevelMapper.put("OFF", OFF);
    }

    private final Level level;
    private final String name;

    public AntistarLevel(Level level) {
    	this.level = level;
        this.name = level.getName();
    }

    public String getName() {
        return name;
    }
    
    public int intValue() {
    	return level.intValue();
    }
    
    public static AntistarLevel getLevel(String levelName) {
    	return stringLevelMapper.get(levelName.toUpperCase());
    }

	@Override
	public String toString() {
		return name;
	}

    Level getLevel() {
        return level;
    }
    
}
