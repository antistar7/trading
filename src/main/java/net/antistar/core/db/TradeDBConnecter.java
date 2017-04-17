package net.antistar.core.db;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.antistar.core.model.TBL_StockInfo;

public class TradeDBConnecter {
	MongoClient mongo;
	String dbName;
	MongoDatabase db = null;
	
	public TradeDBConnecter(String url, int port, String dbName)
	{
		mongo = new MongoClient(url, port);
		this.dbName = dbName;
		db = mongo.getDatabase(this.dbName);
	}
	
	
	
	public void InsertTrade(TBL_StockInfo mTBL_StockInfo, String code)
	{
		String collections = "STOCK_" + code;
		MongoCollection<Document> trade = db.getCollection(collections);
		Document doc = new Document("StartJuka", mTBL_StockInfo.getStartJuka())
				.append("_id", System.currentTimeMillis())
				.append("HighJuka", mTBL_StockInfo.getHighJuka())
				.append("UpJuka", mTBL_StockInfo.getUpJuka())
				.append("LowJuka", mTBL_StockInfo.getLowJuka())
				.append("DownJuka", mTBL_StockInfo.getDownJuka())
				.append("Amount", mTBL_StockInfo.getAmount())
				.append("CurJuka", mTBL_StockInfo.getCurJuka())
				.append("PrevJuka", mTBL_StockInfo.getPrevJuka())
				.append("FaceJuka", mTBL_StockInfo.getFaceJuka())
				.append("Debi", mTBL_StockInfo.getDebi())
				.append("High52", mTBL_StockInfo.getHigh52())
				.append("Low52", mTBL_StockInfo.getLow52())
				.append("Per", mTBL_StockInfo.getPer())
				.append("Volume", mTBL_StockInfo.getVolume())
				.append("JongName", mTBL_StockInfo.getJongName())
				.append("Money", mTBL_StockInfo.getMoney())
				.append("DungRak", mTBL_StockInfo.getDungRak());
		
		trade.insertOne(doc);
	}
}
