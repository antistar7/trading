package net.antistar.trade.producer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.antistar.logger.AntistarLogger;
import net.antistar.util.Config;


public class TradeProducer {

	private static TradeProducer singleton;
	private static final String EXCHANGE_NAME = "topic_trade";
	private String TOPIC;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	Address[] mqAddressArray;
	private long index = 0;
	private final static AntistarLogger log = AntistarLogger.getLogger(TradeProducer.class);
	String ID, PW, address;
	int magic;
	private TradeProducer() throws IOException, TimeoutException {
		File file = Config.getTradeConfigFromSystem();
		TOPIC = Config.getTradeQueueName(file);
		ID = Config.getTradeID(file);
		PW = Config.getTradePW(file);
		address = Config.getTradeAddress(file);
		magic = Config.getTradeMagic(file);
		init();
	}
	
	public static TradeProducer getInstance() throws IOException, TimeoutException{		
        if(singleton == null){
            synchronized(TradeProducer.class) {
                if (singleton == null) {
                	singleton = new TradeProducer();
                }
            }
        }
        
		return singleton;
	}
	
	public void sendMessage(String sendMsg, String code) throws Exception {
		boolean success = false;
		int index = 0;
		init();
		
		for(int i=0; i<3; i++){
			try{
				index = Integer.parseInt(code)%magic;
				log.info("channel : " + channel.getChannelNumber() + "," + channel.getConnection().toString());
				String routingKey = "Trade." + index;
				//channel.exchangeDeclare(EXCHANGE_NAME, TOPIC);
				log.info(EXCHANGE_NAME + "," + routingKey);
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, sendMsg.getBytes());
		    	success = true;
		    	break;
			} catch (Exception e){
				log.info("channel : " + channel);  
		    	log.info("Exception Mq error : " + e.getMessage()); 
//		    	log.fatal(MQUtility.getStackTrace(e));
		    	try{
			    	closeConnection();
			    	Thread.sleep(1000);
			    	init();
			    	//log.info("Mq Retry init success"); 
		    	}catch (IOException e1){
			    	log.info("Exception Mq init error : ", e1);
			    	e.printStackTrace();
//			    	log.fatal(MQUtility.getStackTrace(e));
		    	}	
		    	
			} finally{
				//if(channel!= null){
				//	channel.close();
				//}
			}	
		}
		
		if(!success){
			throw new Exception("Exception Mq send error");
		}
	}
	
	private void init() throws IOException, TimeoutException {		
		try {
			factory = new ConnectionFactory();
	        factory.setUsername(ID);
	        log.info("user name : " + factory.getUsername());
	        factory.setPassword(PW);
	        log.info("pw : " + factory.getPassword());
	        factory.setVirtualHost("trade");
	        factory.setAutomaticRecoveryEnabled(true);
	        factory.setTopologyRecoveryEnabled(true);
	        factory.setNetworkRecoveryInterval(0);
	        factory.setRequestedHeartbeat(5);
	        
			mqAddressArray = Address.parseAddresses(address);
			log.info("address : " + address);
			connection = factory.newConnection(mqAddressArray);
			channel = connection.createChannel();
		} catch (IOException e) {
			log.info("Exception MQ newConnection init ", e);
			e.printStackTrace();
//			log.fatal(MQUtility.getStackTrace(e));
			throw new IOException("Exception MQ newConnection init");
		}
	}

	public void closeConnection() throws IOException{
		if(connection != null){
			connection.close();
		}
	}	

}
