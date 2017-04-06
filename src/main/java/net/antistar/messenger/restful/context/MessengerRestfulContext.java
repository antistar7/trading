package net.antistar.messenger.restful.context;

import net.antistar.restful.context.RestfulContext;
import net.antistar.exception.AntistarException;
import net.antistar.logger.json.JSONArray;
import net.antistar.logger.json.JSONObject;
import net.antistar.logger.AntistarLogger;
import javax.servlet.ServletConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * @author : syfer
 * @version 0.1
 * @since 13. 4. 11.
 */
public class MessengerRestfulContext implements RestfulContext {
    private static final AntistarLogger logger = AntistarLogger.getLogger(MessengerRestfulContext.class);
    
    @Override
    public void destroy() throws AntistarException {

    }

    @Override
    public void init(ServletConfig config) throws AntistarException {    	

    }

}
