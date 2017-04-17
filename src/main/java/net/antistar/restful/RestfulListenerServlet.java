package net.antistar.restful;

import net.antistar.restful.exception.UCloudHttpException;
import net.antistar.exception.AntistarException;
import net.antistar.logger.json.JSONObject;
import net.antistar.logger.json.XML;
import net.antistar.restful.action.*;
import net.antistar.restful.context.RestfulContextFactory;
import net.antistar.restful.context.RestfulContext;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.tx.TxLogger;
import net.antistar.logger.tx.TxRecord;
import net.antistar.logger.util.TxLogHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.io.PrintWriter;

/**
 * Restful Servlet Framework
 *
 * @author : yongoo.hur@kt.com
 * @version 0.1
 * @since 12. 7. 18.
 */
public class RestfulListenerServlet extends HttpServlet {
    private static final AntistarLogger logger = AntistarLogger.getLogger(RestfulListenerServlet.class);

    public static final String GET_METHOD = "GET";

    private RestfulActionFactory actionFactory;
    private RestfulContext context;
    private boolean isInitialized = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doExecute(GET_METHOD, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doExecute("POST", req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doExecute("PUT", req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doExecute("DELETE", req, resp);
    }

    private void doExecute(String method, HttpServletRequest req, HttpServletResponse resp) {
        if(!isInitialized) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }

        String hiddenMethod = req.getParameter("_method");
        if(hiddenMethod != null) {
            method = hiddenMethod.toUpperCase();
        }
        RestfulAction action;
        Map<String, String> actionContext;
        String pathInfo = req.getPathInfo();
        if(pathInfo.endsWith("/")) {
            pathInfo = pathInfo.substring(0, pathInfo.length() -1);
        }
        RestfulActionInfo actionInfo = actionFactory.getActionInfo(method, pathInfo);
        TxRecord record = null;
        if(actionInfo == null) {
            action = BadRequestAction.getInstance();
            actionContext = null;
        } else {
            record = TxLogHelper.createTxLogRecord(req, actionInfo.getActionID()); //new TxRecord(actionInfo.getActionID());
            action = actionInfo.getAction();
            actionContext = actionInfo.getActionContext();
        }

        AntistarException lastError = null;

        try {
/*
                PrintWriter out = resp.getWriter();
                out.println("<html>");
                out.println("<body>");
                out.println("<h1>MessengerAction Get</h1>");
                out.println("</body>");
                out.println("</html>");
*/
            action.doAction(record, actionContext, context, req, resp);
        } catch(UCloudHttpException e) {
            e.printStackTrace();
            lastError = e;
            if(record != null) {
                TxLogger.end(record, TxRecord.ERROR, e.getResponseCode(), e.getErrorCode());
            }
        } catch(Exception e) {
            e.printStackTrace();
            lastError = new AntistarException("UnknownException", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            if(record != null) {
                TxLogger.end(record, TxRecord.ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 1000);
            }
        } finally{
            if(lastError == null) {
                if(record != null) {
                    System.out.println("End Record");
                    TxLogger.end(record, TxRecord.SUCCEED, getHttpSuccessCode(method), 0);
                }
            }
        }
    }

    private int getHttpSuccessCode(String method) {
        if(method.equalsIgnoreCase("POST")) {
            return HttpServletResponse.SC_CREATED;
        } else if(method.equalsIgnoreCase("DELETE")) {
            return HttpServletResponse.SC_NO_CONTENT;
        }
        return HttpServletResponse.SC_OK;
    }

    @Override
    public void destroy() {
        try {
            context.destroy();
        } catch (AntistarException e) {
            e.printStackTrace();
        }
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String apiFileName = config.getInitParameter("api-xml");
        if(apiFileName == null) {
            apiFileName = "api.xml";
        }
        config.getServletContext().getServerInfo();

        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(apiFileName)));
        System.out.println(apiFileName);

        String line;
        StringBuilder builder = new StringBuilder();
        JSONObject apiXml;
        try {
            while( (line=br.readLine()) != null) {
                builder.append(line);
            }
            apiXml = XML.toJSONObject(builder.toString()).getJSONObject("api-setting");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return;
        }
        String apiName = apiXml.getString("api-name");
        if(apiName == null) {
            throw new NullPointerException("api-name cannot be null");
        }
        TxLogger.setAPIName(apiName);
        actionFactory = RestfulActionFactory.createActionFactory(apiXml.getJSONObject("api-list"));
        if(apiXml.getJSONObject("api-context") != null && apiXml.getJSONObject("api-context").getJSONObject("context") != null) {
            try {

                context = RestfulContextFactory.createContext(apiXml.getJSONObject("api-context").getJSONObject("context"));
                context.init(config);
            } catch (ClassNotFoundException e) {
                logger.fatal(e);
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return;
            } catch (IllegalAccessException e) {
                logger.fatal(e);
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return;
            } catch (InstantiationException e) {
                logger.fatal(e);
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return;
            } catch (AntistarException e) {
                logger.fatal(e);
                e.printStackTrace();
                return;
            } catch (IOException e) {
            	logger.fatal(e);
				e.printStackTrace();
			} catch (TimeoutException e) {
				logger.fatal(e);
				e.printStackTrace();
			}
        }

        isInitialized = true;
    }
}
