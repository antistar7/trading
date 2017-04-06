package net.antistar.restful.action;

import net.antistar.restful.context.RestfulContext;
import net.antistar.restful.exception.UCloudHttpException;
import net.antistar.logger.tx.TxRecord;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 24.
 * Time: 오후 1:34
 * To change this template use File | Settings | File Templates.
 */
public interface RestfulAction {
    public void doAction(TxRecord record, Map<String, String> urlKeyMap, RestfulContext context, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException;
}
