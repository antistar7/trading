package com.ucloud.dms.producer;

import java.io.IOException;

import com.kt.ucloud.async.comm.MQUtility;
import com.kt.ucloud.async.comm.exception.MissingPropertyExcetpion;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.ucloud.logging.logger.UCloudLogger;
import com.ucloud.tiny.dms.config.DMSConfig;


public class DMSProducer {

	private static DMSProducer singleton;
	private static final String EXCHANGE_NAME = "topic_logs";
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	Address[] mqAddressArray;
	private long index = 0;
	private final static UCloudLogger log = UCloudLogger.getLogger(DMSProducer.class);
	
	private DMSProducer() throws IOException, MissingPropertyExcetpion {
		init();
	}
	
	public static DMSProducer getInstance() throws IOException, MissingPropertyExcetpion {		
        if(singleton == null){
            synchronized(DMSProducer.class) {
                if (singleton == null) {
                	singleton = new DMSProducer();
                }
            }
        }
        
		return singleton;
	}
	
	public void sendMessage(String sendMsg) throws Exception {
		boolean success = false;
		init();
		for(int i=0; i<3; i++){
			try{
				log.info("channel : " + channel.getChannelNumber() + "," + channel.getConnection().toString());
				//channel.exchangeDeclare(EXCHANGE_NAME, TOPIC);
				channel.basicPublish(EXCHANGE_NAME, DMSConfig.getInstance().getRoutingkey(), null, sendMsg.getBytes());
		    	success = true;
		    	break;
			} catch (Exception e){
				log.info("channel : " + channel);  
		    	log.info("Exception Mq error : " + e.getMessage()); 
		    	log.fatal(MQUtility.getStackTrace(e));
		    	try{
			    	closeConnection();
			    	Thread.sleep(1000);
			    	init();
			    	log.info("Mq Retry init success"); 
		    	}catch (IOException e1){
			    	log.info("Exception Mq init error : ", e1); 
			    	log.fatal(MQUtility.getStackTrace(e));
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
	
	public void sendStatusMessage(String sendMsg) throws Exception {
		boolean success = false;
		init();
		for(int i=0; i<3; i++){
			try{
				log.info("channel : " + channel.getChannelNumber() + "," + channel.getConnection().toString());
				//channel.exchangeDeclare(EXCHANGE_NAME, TOPIC);
				channel.basicPublish(EXCHANGE_NAME, DMSConfig.getInstance().getStatusRoutingkey(), null, sendMsg.getBytes());
		    	success = true;
		    	break;
			} catch (Exception e){
				log.info("channel : " + channel);  
		    	log.info("Exception Mq error : " + e.getMessage()); 
		    	log.fatal(MQUtility.getStackTrace(e));
		    	try{
			    	closeConnection();
			    	Thread.sleep(1000);
			    	init();
			    	log.info("Mq Retry init success"); 
		    	}catch (IOException e1){
			    	log.info("Exception Mq init error : ", e1); 
			    	log.fatal(MQUtility.getStackTrace(e));
		    	}	
		    	
			} finally{
				if(channel!= null){
					channel.close();
				}
			}	
		}
		
		if(!success){
			throw new Exception("Exception Mq send error");
		}
	}

	private void init() throws IOException {		
		try {
//			index = TinyConfig.getTrashWorkerFrom();
			factory = new ConnectionFactory();
	        factory.setUsername(DMSConfig.getInstance().getConsumerId());
	        factory.setPassword(DMSConfig.getInstance().getConsumerPw());
	        factory.setAutomaticRecoveryEnabled(true);
	        factory.setTopologyRecoveryEnabled(true);
	        factory.setNetworkRecoveryInterval(0);
	        factory.setRequestedHeartbeat(5);
	        
			mqAddressArray = Address.parseAddresses(DMSConfig.getInstance().getConsumerAddress());
			connection = factory.newConnection(mqAddressArray);
			channel = connection.createChannel();
		} catch (IOException e) {
			log.info("Exception MQ newConnection init ", e);
			log.fatal(MQUtility.getStackTrace(e));
			throw new IOException("Exception MQ newConnection init");
		}
	}

	public void closeConnection() throws IOException{
		if(connection != null){
			connection.close();
		}
	}	

}
