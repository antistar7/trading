package net.antistar.trade.restful.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.antistar.core.Engine;
import net.antistar.core.model.TBL_StockInfo;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.json.JSONObject;
import net.antistar.logger.json.XML;
import net.antistar.logger.tx.TxRecord;
import net.antistar.restful.exception.UCloudHttpException;
import net.antistar.util.TradeUtils;


/**
 * @author : syfer
 * @version 0.1
 * @since 13. 4. 7.
 */
public class TradeAction extends AbstractTradeAction {
    private static final AntistarLogger logger = AntistarLogger.getLogger(TradeAction.class);

    @Override
    protected AntistarLogger getLogger() {
        return logger;
    }

    @Override
    protected void process(TxRecord record, Engine engine, Map<String, String> urlKeyMap, HttpServletRequest req, HttpServletResponse resp) throws UCloudHttpException,IOException {
    	JSONObject TBL_stockInfo = null;
    	
    	try {
    		resp.setContentType("text/plain;charset=UTF-8");
			String code = urlKeyMap.get("code");
	
			String url = "http://asp1.krx.co.kr/servlet/krx.asp.XMLSise?code=" + code;
			String html = JSONObject.getHtml(url);
			
			JSONObject object = XML.toJSONObject(html);
			JSONObject stock = (JSONObject)object.get("stockprice");
			TBL_stockInfo = (JSONObject)stock.get("TBL_StockInfo");
			TBL_stockInfo.put("jongmok", code);
								
			engine.sendMessage(TBL_stockInfo.toString(), code);
			
//			JSONObject stockInfo = (JSONObject)stock.get("stockInfo");
//			JSONObject TBL_DailyStock = (JSONObject)stock.get("TBL_DailyStock");
//			JSONObject TBL_TimeConclude = (JSONObject)stock.get("TBL_TimeConclude");
//			JSONObject TBL_Hoga = (JSONObject)stock.get("TBL_Hoga");
//			JSONObject TBL_AskPrice = (JSONObject)stock.get("TBL_AskPrice");
			
			resp.getWriter().print(TBL_stockInfo.toString());
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		resp.getWriter().print(TBL_stockInfo.toString());
    		logger.error(ex.getMessage(), ex);
    	}
    }


}

