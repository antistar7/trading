package net.antistar.trade.restful.context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletConfig;

import net.antistar.core.Engine;
import net.antistar.core.db.TradeDBConnecter;
import net.antistar.exception.AntistarException;
import net.antistar.logger.AntistarLogger;
import net.antistar.restful.context.RestfulContext;
import net.antistar.util.Config;

/**
 * @author : syfer
 * @version 0.1
 * @since 13. 4. 11.
 */
public class TradeRestfulContext implements RestfulContext {
    private static final AntistarLogger logger = AntistarLogger.getLogger(TradeRestfulContext.class);
    private Engine engine;
    
    @Override
    public void destroy() throws AntistarException {

    }

    @Override
    public void init(ServletConfig config) throws AntistarException, IOException, TimeoutException { 
    	File file = Config.getTradeConfigFromSystem();
    	TradeDBConnecter mTradeDBConnecter = new TradeDBConnecter(Config.getTradeUrl(file), Config.getTradePort(file), Config.getTradeDBName(file));
    	engine = new Engine(mTradeDBConnecter);
    }
    
    public Engine getEngine() {
        return engine;
    }
}
