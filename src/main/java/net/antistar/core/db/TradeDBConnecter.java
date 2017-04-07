package net.antistar.core.db;

import com.mongodb.MongoClient;

public class TradeDBConnecter {
	MongoClient mongo;
	String dbName;
	
	public TradeDBConnecter(String url, int port, String dbName)
	{
		mongo = new MongoClient(url, port);
		this.dbName = dbName;
	}
}
