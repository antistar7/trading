package net.antistar.trade.consumer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.antistar.core.db.TradeDBConnecter;
import net.antistar.logger.AntistarLogger;
import net.antistar.util.Config;


public class TradeQueueListener {
	private static final AntistarLogger logger = AntistarLogger.getLogger(TradeQueueListener.class);
	
	ConnectionFactory mqConnFactory;
	Address[] mqAddressArray;
	Connection connection = null;	
	TradeDBConnecter mTradeDBConnecter;
	
	// quename 모듈 명
	private final String EXCHANGE_NAME = "topic_trade";
		
	public TradeQueueListener(TradeDBConnecter mTradeDBConnecter){
		this.mTradeDBConnecter = mTradeDBConnecter;
	}
	
	private void createChannel(Connection conn, File file){
		// 컨슈머 그룹을 생성			
		boolean durable = true;
		boolean autoDelete = false;
		boolean exclusive = false;
		int magic = Config.getTradeMagic(file);
		try {
			String queueName = Config.getTradeQueueName(file);
			Channel channel = conn.createChannel();
			channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
			
			for (int i = 0; i < magic; i++) {
				String routingKey = "Trade." + i;
				logger.info(queueName + "," + EXCHANGE_NAME + "," + routingKey);
				channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
				channel.basicRecover(true);
				channel.basicConsume(queueName, false, new TradeQueueConsumer(mTradeDBConnecter, channel));
				logger.info("Create Channel(" + queueName + "," + routingKey);
			}
		} catch (IOException e) {
			logger.fatal(e.getMessage(), e);
			e.printStackTrace();
		}

	}
		
	public void invoke() throws Exception{  
		logger.info("============ invoke MQ Start===================");
		File file = Config.getTradeConfigFromSystem();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(Config.getTradeID(file));
        logger.info("User name : " + factory.getUsername());
        factory.setPassword(Config.getTradePW(file));
        logger.info("Password : " + factory.getPassword());
        factory.setVirtualHost("trade");
        factory.setAutomaticRecoveryEnabled(true);
        //factory.setTopologyRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(0);
        factory.setRequestedHeartbeat(5);
        
		mqAddressArray = Address.parseAddresses(Config.getTradeAddress(file));
        ExecutorService es = Executors.newFixedThreadPool(1);
        Connection conn = factory.newConnection(es, mqAddressArray);
        createChannel(conn, file);
        logger.info("============ invoke MQ End===================");
    } //invoke
	
	public void close() throws IOException{
		this.connection.close();		
	}
}
