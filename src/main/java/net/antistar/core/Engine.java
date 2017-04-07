package net.antistar.core;

import net.antistar.core.db.TradeDBConnecter;

public class Engine {
	public String mongo_url;
	public int mongo_port;
	private TradeDBConnecter mTradeDBConnecter;
	public Engine(TradeDBConnecter mTradeDBConnecter) {
		this.mTradeDBConnecter = mTradeDBConnecter;
	}
}
