package net.antistar.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.logging.*;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 12. 7
 */
class AntistarDailyRollingLogFileHandler extends StreamHandler implements AntistarHandler {
    /** File prefix. */
    //private static String DEFAULT_PREFIX = "ucloud.";
    /** File suffix. */
    private static String DEFAULT_SUFFIX = ".log";
    
    /**
     * 로거마다 핸들러가 각자 들어간다. 로그를 찍을 때 자신이 찍을 파일이 날짜에 제대로 맞는지 확인하는 작업이 필요하다. 
     * Time in milliseconds for the next cycle */
    private long nextCycle = 0;
    //private static long size = 0;
    //private static long maxSize = Long.MAX_VALUE;
    /** Time cycle (for file rolling) */
    private CycleType cycle; //default

    private String logDirPath;
    private String prefix;
    private String suffix = DEFAULT_SUFFIX;

    private enum RoleType {
        TIME
    //    SIZE
    }

    private enum CycleType {
        YEAR,
        MONTH,
        WEEK,
        DAILY,
        HOUR
    }
    
    /**
    * Constructor.
     * @param cycle Logger가 File을 변경하는 주기. year, month, week, day중에서 한 가지를 선택.
     * @param logFilePath Logger File의 위치. cycle이 지나면 file 뒤에 날짜/시간이 붙는다.
     */
	public AntistarDailyRollingLogFileHandler(String cycle, String logFilePath) {
		super();
		this.cycle = CycleType.valueOf(cycle.toUpperCase().trim());

		File file = new File(logFilePath);
		this.logDirPath = file.getParent();
		if (logDirPath == null) {
			logDirPath = ".";
        }

		arrangeFilename(file.getName());
        openNewLogFile();
	}

    private void arrangeFilename(String fileName) {
    	int index = fileName.lastIndexOf('.');
    	if( index < 0 ) {
    		prefix = fileName;
            suffix = DEFAULT_SUFFIX;
    	} else {
    		String extension = fileName.substring(index);
            prefix = fileName.substring(0,index);
    		if(!extension.equals(suffix) ) {
    			suffix = extension;
            }
    	}
    }

    /**
    * Open existing or create new log file.
    */
    private synchronized void openNewLogFile() {
    //create file name:
        /*String dateFormat = "yyyy-MM-dd";
        String dateString = dateFormat; //default (to note error in file name)*/
        Date currentDate= new Date();
        /*try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            dateString = sdf.format(currentDate);
        } catch (IllegalArgumentException iae) {
        *//* ignore wrong date format *//*
        }*/
        //compute next cycle:
        nextCycle = computeNextCycle(currentDate);
        //create new file:
        String fileName = logDirPath + File.separator + prefix + suffix;
        File file = new File(fileName);
        //create file:
        if (!file.exists()) {
            try {
            	File parent = file.getParentFile();
                boolean mkdirs;
            	if( !parent.exists() ) {
                    mkdirs = parent.mkdirs();
                    if(!mkdirs) {
                        throw new IOException("Cannot create log directory : [" + parent.getPath() + "]");
                    }
                }

                if(!file.createNewFile()) {
                    System.err.println("Can't Create File " + fileName);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        }
        //set log file as OutputStream:
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            this.setOutputStream(fos);
        } catch (FileNotFoundException fnfe) {
            reportError(null, fnfe, ErrorManager.OPEN_FAILURE);
            fnfe.printStackTrace(System.err);
            this.setOutputStream(System.out); //fallback stream
        }
    }//openDailyFile()

	private long computeNextCycle(Date currentDate) {
		Date nextDate;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(currentDate);
        switch (cycle) {
            case YEAR: {
                gc.add(Calendar.YEAR, 1);
                int year = gc.get(Calendar.YEAR);
                GregorianCalendar gc2 = new GregorianCalendar(year, 0, 1);
                nextDate = gc2.getTime();
                break;
            }
            case MONTH: {
                gc.add(Calendar.MONTH, 1);
                int month = gc.get(Calendar.MONTH);
                int year = gc.get(Calendar.YEAR);
                GregorianCalendar gc2 = new GregorianCalendar(year, month, 1);
                nextDate = gc2.getTime();
                break;
            }
            case WEEK: {
                gc.add(Calendar.WEEK_OF_YEAR, 1);
                nextDate = gc.getTime();
                break;
            }
            case DAILY: {
                gc.add(Calendar.DAY_OF_MONTH, 1);
                nextDate = gc.getTime();
                break;
            }
            case HOUR: {
                gc.add(Calendar.HOUR, 1);
                nextDate = gc.getTime();
                break;
            }
            default:
                nextDate = gc.getTime();
        }

        //to zero time:
        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.setTime(nextDate);
        gc2.setTimeZone(TimeZone.getDefault());
        gc2.set(Calendar.HOUR, 0);
        gc2.set(Calendar.HOUR_OF_DAY, 0);
        gc2.set(Calendar.MINUTE, 0);
        gc2.set(Calendar.SECOND, 0);
        gc2.set(Calendar.MILLISECOND, 0);
        nextDate = gc2.getTime();
        return nextDate.getTime();
	}
    /**
    * Overwrites super.
    */
    public synchronized void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        //check if we need to rotate
        //size += record.getMessage().length();
        if (System.currentTimeMillis() >= nextCycle) { //next cycle?
            role(RoleType.TIME);
        } /*else if(size >= maxSize) {
            role(ROLE_TYPE.SIZE);
            size = 0;
        }*/
        super.publish(record);
        flush();


    }

    /*private void openSizeFile() {
        //To change body of created methods use File | Settings | File Templates.
    }*/

    /**
    * Role file. Close current file and possibly create new file.
     * @param type ROLLING Type
     */
    private synchronized void role(RoleType type) {
        String dateFormat = "yyyy-MM-dd";
        String dateString = dateFormat; //default (to note error in file name)
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, -1);
        Date currentDate= gc.getTime();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            dateString = sdf.format(currentDate);
        } catch (IllegalArgumentException iae) {
        /* ignore wrong date format */
        }
        //compute next cycle:
        nextCycle = computeNextCycle(currentDate);

        Level oldLevel = getLevel();
        setLevel(Level.OFF);
        this.close();
        /*switch (type) {
            case TIME:*/
        String backupFileName = logDirPath + File.separator + prefix + suffix + "." + dateString;
        String fileName = logDirPath + File.separator + prefix + suffix;
        File file = new File(fileName);
        if(!file.renameTo(new File(backupFileName))) {
            System.err.println("Cannot move old log file [" + fileName + "] to [" + backupFileName + "]");
        } else {
            openNewLogFile();
        }
            /*case SIZE:
                openSizeFile();
                break;*/
        //}
        setLevel(oldLevel);
    }//rotate()
}//RollingFileHandler
