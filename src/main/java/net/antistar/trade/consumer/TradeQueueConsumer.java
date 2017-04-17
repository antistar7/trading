package net.antistar.trade.consumer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import net.antistar.core.db.TradeDBConnecter;
import net.antistar.core.model.TBL_StockInfo;
import net.antistar.logger.AntistarLogger;
import net.antistar.logger.json.JSONObject;
import net.antistar.util.TradeUtils;

public class TradeQueueConsumer extends DefaultConsumer {
	private static final AntistarLogger logger = AntistarLogger.getLogger(TradeQueueConsumer.class);
	private int retried = 0;
	private Channel channel;
	private TradeDBConnecter mTradeDBConnecter;
	public TradeQueueConsumer(TradeDBConnecter mTradeDBConnecter, Channel channel) {
		super(channel);
		this.mTradeDBConnecter = mTradeDBConnecter;
		try {
			this.channel = channel; // mq channel
		} catch (Exception e) {
			logger.fatal(e);
		}
	}

	private void exceptionHandler(String routingKey, String message, int retried) {
		// exception handling
	}
	
	
	public static String timeStamp(long timeStamp){
		String t = String.valueOf(timeStamp/1000);
		return t;
	}
	
	public static String timeStamp2Date(String seconds, String format) {
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			return "";
		}
		if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds+"000")));
	}
	
	private void TradeMessageHandler(String routingKey, String message) { 
		
		if(routingKey.contains("Trade"))
		{
			String code = null;
			try {
				logger.info(message);
				
				TBL_StockInfo mTBL_stockInfo = new TBL_StockInfo();
				JSONObject TBL_stockInfo = new JSONObject(message);
				if(TBL_stockInfo.get("StartJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setStartJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("StartJuka"))));
				else
					mTBL_stockInfo.setStartJuka((Integer)TBL_stockInfo.get("StartJuka"));
				logger.info("start juka : " + mTBL_stockInfo.getStartJuka());
				
				if(TBL_stockInfo.get("CurJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setCurJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("CurJuka"))));
				else
					mTBL_stockInfo.setCurJuka((Integer)TBL_stockInfo.get("CurJuka"));
				logger.info("Cur juka : " + mTBL_stockInfo.getCurJuka());
				
				if(TBL_stockInfo.get("DownJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setDownJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("DownJuka"))));
				else
					mTBL_stockInfo.setDownJuka((Integer)TBL_stockInfo.get("DownJuka"));
				logger.info("Down juka : " + mTBL_stockInfo.getDownJuka());
				
				if(TBL_stockInfo.get("HighJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setHighJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("HighJuka"))));
				else
					mTBL_stockInfo.setHighJuka((Integer)TBL_stockInfo.get("HighJuka"));
				logger.info("High juka : " + mTBL_stockInfo.getHighJuka());
				
				if(TBL_stockInfo.get("LowJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setLowJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("LowJuka"))));
				else
					mTBL_stockInfo.setLowJuka((Integer)TBL_stockInfo.get("LowJuka"));			
				logger.info("Low juka : " + mTBL_stockInfo.getLowJuka());
				
				if(TBL_stockInfo.get("PrevJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setPrevJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("PrevJuka"))));
				else
					mTBL_stockInfo.setPrevJuka((Integer)TBL_stockInfo.get("PrevJuka"));						
				logger.info("Prev juka : " + mTBL_stockInfo.getPrevJuka());
				
				if(TBL_stockInfo.get("UpJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setUpJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("UpJuka"))));
				else
					mTBL_stockInfo.setUpJuka((Integer)TBL_stockInfo.get("UpJuka"));									
				logger.info("Up juka : " + mTBL_stockInfo.getUpJuka());
				
				if(TBL_stockInfo.get("Per").getClass().getName().contains("Double"))
					mTBL_stockInfo.setPer((Double)TBL_stockInfo.get("Per"));
				else if(TBL_stockInfo.get("UpJuka").getClass().getName().contains("Integer"))
					mTBL_stockInfo.setPer((Integer)TBL_stockInfo.get("Per"));
				else if(TBL_stockInfo.get("UpJuka").getClass().getName().contains("Long"))
					mTBL_stockInfo.setPer((Long)TBL_stockInfo.get("Per"));
	
				logger.info("Per : " + mTBL_stockInfo.getPer());
				
				if(TBL_stockInfo.get("FaceJuka").getClass().getName().contains("String"))
					mTBL_stockInfo.setFaceJuka(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("FaceJuka"))));
				else
					mTBL_stockInfo.setFaceJuka((Integer)TBL_stockInfo.get("FaceJuka"));
				logger.info("Face Juka : " + mTBL_stockInfo.getFaceJuka());
				
				if(TBL_stockInfo.get("High52").getClass().getName().contains("String"))
					mTBL_stockInfo.setHigh52(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("High52"))));
				else
					mTBL_stockInfo.setHigh52((Integer)TBL_stockInfo.get("High52"));
				logger.info("High52 : " + mTBL_stockInfo.getHigh52());				
				
				if(TBL_stockInfo.get("Low52").getClass().getName().contains("String"))
					mTBL_stockInfo.setLow52(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Low52"))));
				else
					mTBL_stockInfo.setLow52((Integer)TBL_stockInfo.get("Low52"));
				logger.info("Low52 : " + mTBL_stockInfo.getLow52());
								
				if(TBL_stockInfo.get("Debi").getClass().getName().contains("String"))
					mTBL_stockInfo.setDebi(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Debi"))));
				else
					mTBL_stockInfo.setDebi((Integer)TBL_stockInfo.get("Debi"));
				logger.info("Debi : " + mTBL_stockInfo.getDebi());
								
				if(TBL_stockInfo.get("Amount").getClass().getName().contains("String"))
					mTBL_stockInfo.setAmount(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Amount"))));
				else
					mTBL_stockInfo.setAmount((Long)TBL_stockInfo.get("Amount"));
				logger.info("Amount : " + mTBL_stockInfo.getAmount());
				
				if(TBL_stockInfo.get("Volume").getClass().getName().contains("String"))
					mTBL_stockInfo.setVolume(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Volume"))));
				else
					mTBL_stockInfo.setVolume((Long)TBL_stockInfo.get("Volume"));
				logger.info("Volume : " + mTBL_stockInfo.getVolume());
				
				if(TBL_stockInfo.get("Money").getClass().getName().contains("String"))
					mTBL_stockInfo.setMoney(Long.parseLong(TradeUtils.ToNumeric((String) TBL_stockInfo.get("Money"))));
				else
					mTBL_stockInfo.setMoney((Long)TBL_stockInfo.get("Money"));
				logger.info("Money : " + mTBL_stockInfo.getMoney());				
				
				if(TBL_stockInfo.get("DungRak").getClass().getName().contains("String"))
					mTBL_stockInfo.setDungRak(Integer.parseInt(TradeUtils.ToNumeric((String) TBL_stockInfo.get("DungRak"))));
				else
					mTBL_stockInfo.setDungRak((Integer)TBL_stockInfo.get("Debi"));
				logger.info("DungRak : " + mTBL_stockInfo.getDungRak());
				
				mTBL_stockInfo.setJongName((String) TBL_stockInfo.get("JongName"));

				if(TBL_stockInfo.get("jongmok").getClass().getName().contains("String"))
					code = (String) TBL_stockInfo.get("jongmok");
				else
					code = String.valueOf(TBL_stockInfo.get("jongmok"));
				logger.info("jongmok : " + code);
				
				insertTrade(mTBL_stockInfo, code);

			} catch (Exception e) {
				logger.fatal(e.getMessage(), e);			
				exceptionHandler(routingKey, message, retried++);
			}
		}
		else {
			logger.fatal("Invalid routing key : " + routingKey);
		}
	}

	@Override
	public void handleDelivery(String consumeTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
			throws IOException {
		// String contentType = properties.getContentType();
		String routingKey = envelope.getRoutingKey();
		String msg = new String(body);

		long deliveryTag = envelope.getDeliveryTag();

		// message handling logic here
		TradeMessageHandler(routingKey, msg);

		// multiple - false if we are acknowledging multiple messages with the
		// same delivery tag
		// this.channel.basicAck(deliveryTag, false); // Modify by K.H Kim
		// (2016.12.05)		
		try {
			this.channel.basicAck(deliveryTag, false);
		} catch (Exception ex) {
			// Log
			logger.fatal("[handleDelivery]" + ex);
		}
	}

	public void insertTrade(TBL_StockInfo mTBL_StockInfo, String code)
	{
		mTradeDBConnecter.InsertTrade(mTBL_StockInfo, code);
	}
	
	public void close() throws TimeoutException {
		try {
			this.channel.close();
		} catch (IOException e) {
			logger.fatal("[close] can not close channel");
		}
	}
}
