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
    	try {
    		resp.setContentType("text/plain;charset=UTF-8");
			String code = urlKeyMap.get("code");
	
			String url = "http://asp1.krx.co.kr/servlet/krx.asp.XMLSise?code=" + code;
			String html = JSONObject.getHtml(url);
			
			JSONObject object = XML.toJSONObject(html);
			JSONObject stock = (JSONObject)object.get("stockprice");
			JSONObject TBL_stockInfo = (JSONObject)stock.get("TBL_StockInfo");
			
			JSONObject stockInfo = (JSONObject)stock.get("stockInfo");
			JSONObject TBL_DailyStock = (JSONObject)stock.get("TBL_DailyStock");
			JSONObject TBL_TimeConclude = (JSONObject)stock.get("TBL_TimeConclude");
			JSONObject TBL_Hoga = (JSONObject)stock.get("TBL_Hoga");
			JSONObject TBL_AskPrice = (JSONObject)stock.get("TBL_AskPrice");
			
			TBL_StockInfo mTBL_stockInfo = new TBL_StockInfo();
			
			mTBL_stockInfo.setStartJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("StartJuka"))));
			logger.info("start juka : " + mTBL_stockInfo.getStartJuka());
			mTBL_stockInfo.setCurJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("CurJuka"))));
			logger.info("Cur juka : " + mTBL_stockInfo.getCurJuka());
			mTBL_stockInfo.setDownJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("DownJuka"))));
			logger.info("Down juka : " + mTBL_stockInfo.getDownJuka());
			mTBL_stockInfo.setHighJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("HighJuka"))));
			logger.info("High juka : " + mTBL_stockInfo.getHighJuka());
			mTBL_stockInfo.setLowJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("LowJuka"))));
			logger.info("Low juka : " + mTBL_stockInfo.getLowJuka());
			mTBL_stockInfo.setPrevJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("PrevJuka"))));
			logger.info("Prev juka : " + mTBL_stockInfo.getPrevJuka());			
			mTBL_stockInfo.setUpJuka(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("UpJuka"))));
			logger.info("Up juka : " + mTBL_stockInfo.getUpJuka());
			mTBL_stockInfo.setPer((Double)TBL_stockInfo.get("Per"));
			logger.info("Per : " + mTBL_stockInfo.getPer());
			mTBL_stockInfo.setFaceJuka((Integer)TBL_stockInfo.get("FaceJuka"));
			logger.info("Face Juka : " + mTBL_stockInfo.getFaceJuka());
			mTBL_stockInfo.setHigh52(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("High52"))));
			mTBL_stockInfo.setLow52(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Low52"))));
			mTBL_stockInfo.setDebi(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Debi"))));
			mTBL_stockInfo.setAmount(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Amount"))));
			mTBL_stockInfo.setVolume(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Volume"))));
			mTBL_stockInfo.setMoney(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Money"))));
			mTBL_stockInfo.setDungRak((Integer)TBL_stockInfo.get("DungRak"));
			mTBL_stockInfo.setJongName((String) TBL_stockInfo.get("JongName"));
			mTBL_stockInfo.setJongMok(Integer.parseInt(code));

			
			resp.getWriter().print(mTBL_stockInfo.toString() + "\n" + TBL_stockInfo.toString());
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		logger.error(ex.getMessage(), ex);
    	}
    }


}

