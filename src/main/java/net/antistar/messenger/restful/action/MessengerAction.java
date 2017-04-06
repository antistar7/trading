package net.antistar.messenger.restful.action;

import net.antistar.logger.json.JSONArray;
import net.antistar.logger.json.JSONException;
import net.antistar.logger.json.JSONObject;
import net.antistar.logger.json.XML;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.tx.TxRecord;
import net.antistar.restful.exception.UCloudHttpException;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author : syfer
 * @version 0.1
 * @since 13. 4. 7.
 */
public class MessengerAction extends AbstractMessengerAction {
    private static final AntistarLogger logger = AntistarLogger.getLogger(MessengerAction.class);

    @Override
    protected AntistarLogger getLogger() {
        return logger;
    }

    @Override
    protected void process(TxRecord record, Map<String, String> urlKeyMap, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException,IOException {
		String auth_id = urlKeyMap.get("auth_id");
		String url = "http://dart.fss.or.kr/api/search.json?auth=" + auth_id;
		String html = getHtml(url);
		PrintWriter out = resp.getWriter();
		System.out.println(html);
		logger.info("auth id : " + auth_id); 
		logger.info("<html>");
		logger.info("<body>");
		logger.info("<h2>MessengerAction Get</h2>");
		logger.info("</body>");
		logger.info("</html>");
    }

    public static String getHtml(String url) {
            try {
                   URL targetUrl = new URL(url);
                   BufferedReader reader = new BufferedReader(new InputStreamReader(targetUrl.openStream()));
                   StringBuilder html = new StringBuilder();
                   String current = "";
                   while ((current = reader.readLine()) != null) {
                           html.append(current);
                   }
                   reader.close();
                   return html.toString();
            } catch (MalformedURLException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            return null;
    }

}

