package net.antistar.core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.antistar.core.db.TradeDBConnecter;
import net.antistar.logger.AntistarLogger;
import net.antistar.trade.consumer.TradeQueueListener;
import net.antistar.trade.producer.TradeProducer;

public class Engine {
	private static final AntistarLogger logger = AntistarLogger.getLogger(Engine.class);
	private TradeDBConnecter mTradeDBConnecter;
	private TradeQueueListener mTradeQueueListener;
	private TradeProducer mTradeProducer;
	
	public Engine(TradeDBConnecter mTradeDBConnecter) throws IOException, TimeoutException {
		logger.info("============ init Engine ===================");
		this.mTradeDBConnecter = mTradeDBConnecter;		
		start();
		mTradeProducer = TradeProducer.getInstance();
		logger.info("============ init Engine End===================");
	}
	
	private void start(){		
		this.mTradeQueueListener = new TradeQueueListener(mTradeDBConnecter);
		try {
			this.mTradeQueueListener.invoke();
		} catch (Exception e) {
			logger.fatal(e);
		}          
	}

	public void sendMessage(String msg, String code) throws Exception
	{
		mTradeProducer.sendMessage(msg, code);
	}	
}
