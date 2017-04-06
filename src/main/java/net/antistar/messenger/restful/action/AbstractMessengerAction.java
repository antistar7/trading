package net.antistar.messenger.restful.action;

import net.antistar.restful.action.RestfulAction;
import net.antistar.restful.context.RestfulContext;
import net.antistar.restful.exception.UCloudHttpException;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.tx.TxRecord;
import net.antistar.messenger.restful.context.MessengerRestfulContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.io.PrintWriter;

/**
 * @author : syfer
 * @version 0.1
 * @since 13. 4. 5.
 */
public abstract class AbstractMessengerAction implements RestfulAction {
    @Override
    public void doAction(TxRecord record, Map<String, String> urlKeyMap, RestfulContext context, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException {
        MessengerRestfulContext ctx = (MessengerRestfulContext) context;
	try {
//		PrintWriter out = resp.getWriter();
//		out.println("<html>");
//		out.println("<body>");
//		out.println("<h1>AbstractMesssengerAction Get</h1>");
//		out.println("</body>");
//		out.println("</html>");
		process(record, urlKeyMap, req, resp);
        } catch(UCloudHttpException e) {
            getLogger().debug(e);
            throw e;
	} catch(Exception e) {
	    getLogger().fatal("Unknown Exception ", e);
	}

    }

    protected abstract AntistarLogger getLogger();
    protected abstract void process(TxRecord record, Map<String, String> urlKeyMap, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException, IOException;
}
