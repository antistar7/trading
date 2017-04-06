package net.antistar.restful.action;

import net.antistar.restful.context.RestfulContext;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.tx.TxRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 23.
 * Time: 오후 10:19
 * To change this template use File | Settings | File Templates.
 */
public class BadRequestAction implements RestfulAction {
    private static RestfulAction instance = new BadRequestAction();
    private static final AntistarLogger logger = AntistarLogger.getLogger(BadRequestAction.class);

    public static RestfulAction getInstance() {
        return instance;
    }

    public void doAction(TxRecord record, Map<String, String> urlKeyMap, RestfulContext context, HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        logger.warn("Bad Request : [" + req.getPathInfo() + "]");
    }
}
