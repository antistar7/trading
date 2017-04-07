package net.antistar.trade.restful.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.antistar.core.Engine;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.tx.TxRecord;
import net.antistar.restful.action.RestfulAction;
import net.antistar.restful.context.RestfulContext;
import net.antistar.restful.exception.UCloudHttpException;
import net.antistar.trade.restful.context.TradeRestfulContext;

/**
 * @author : syfer
 * @version 0.1
 * @since 13. 4. 5.
 */
public abstract class AbstractTradeAction implements RestfulAction {
    @Override
    public void doAction(TxRecord record, Map<String, String> urlKeyMap, RestfulContext context, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException {
        TradeRestfulContext ctx = (TradeRestfulContext) context;
        Engine engine = ctx.getEngine();
	try {

		process(record, engine,urlKeyMap, req, resp);
        } catch(UCloudHttpException e) {
            getLogger().debug(e);
            throw e;
	} catch(Exception e) {
	    getLogger().fatal("Unknown Exception ", e);
	}

    }

    protected abstract AntistarLogger getLogger();
    protected abstract void process(TxRecord record, Engine engine, Map<String, String> urlKeyMap, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException, IOException;
}
