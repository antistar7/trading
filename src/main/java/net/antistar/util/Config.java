package net.antistar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	public static File getTradeConfigFromSystem(){
		File confFile = new File(Constants.TRADE_CONFIG_PATH, Constants.TRADE_PROP_FILENAME);
		
		if(confFile.exists() && confFile.isFile()) {
			return confFile;
		}
		
		return null;
	}
	
	public static String getTradeUrl(File confFile) {
		if(confFile == null){
			return null;
		}		
		
		if(confFile.exists() && confFile.isFile()) {
			String url = getTradeUrlFromSystemFile(confFile);
			if(url == null){
				return null;
			}else {
				return url;
			}
		}	
		return null;
	}

	public static String getTradeDBName(File confFile) {
		if(confFile == null){
			return null;
		}		
		
		if(confFile.exists() && confFile.isFile()) {
			String db = getTradeDBNAmeFromSystemFile(confFile);
			if(db == null){
				return null;
			}else {
				return db;
			}
		}	
		return null;
	}

	
	public static int getTradePort(File confFile) {
		if(confFile == null){
			return 0;
		}		
		
		if(confFile.exists() && confFile.isFile()) {
			String port = getTradePortFromSystemFile(confFile);
			if(port == null){
				return 0;
			}else {
				int rPort = Integer.valueOf(port);
				return rPort;
			}
		}	
		return 0;
	}
	
	protected static String getTradeUrlFromSystemFile(File configFile) {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(configFile);
			properties.load(is);
			if(!properties.containsKey(Constants.TRADE_URL)){
				return null;	
			}
			return properties.getProperty(Constants.TRADE_URL);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if( is != null) 
				try { is.close(); } catch (Exception ignore) {}
		}
	}
	
	protected static String getTradeDBNAmeFromSystemFile(File configFile) {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(configFile);
			properties.load(is);
			if(!properties.containsKey(Constants.TRADE_DB)){
				return null;	
			}
			return properties.getProperty(Constants.TRADE_DB);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if( is != null) 
				try { is.close(); } catch (Exception ignore) {}
		}
	}

	protected static String getTradePortFromSystemFile(File configFile) {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(configFile);
			properties.load(is);
			if(!properties.containsKey(Constants.TRADE_PORT)){
				return null;	
			}
			return properties.getProperty(Constants.TRADE_PORT);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if( is != null) 
				try { is.close(); } catch (Exception ignore) {}
		}
	}
}
