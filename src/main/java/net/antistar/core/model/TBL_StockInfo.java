package net.antistar.core.model;

public class TBL_StockInfo {
	private long StartJuka;
	private long HighJuka;
	private long UpJuka;
	private long LowJuka;
	private long DownJuka;
	private long Amount;
	private long CurJuka;
	private long PrevJuka;
	private int FaceJuka;
	private long Debi;
	private int High52;
	private int Low52;
	private double Per;
	private long Volume;
	private String JongName;
	private int JongMok;
	private long Money;
	private double DungRak;
	
	public TBL_StockInfo() {
		
	}
	
	public TBL_StockInfo(TBL_StockInfo copy) {
		StartJuka = copy.getStartJuka();
		HighJuka = copy.getHighJuka();
		UpJuka = copy.getUpJuka();
		LowJuka = copy.getLowJuka();
		DownJuka = copy.getDownJuka();
		Amount = copy.getAmount();
		CurJuka = copy.getCurJuka();
		PrevJuka = copy.getPrevJuka();
		FaceJuka = copy.getFaceJuka();
		Debi = copy.getDebi();
		High52 = copy.getHigh52();
		Low52 = copy.getLow52();
		Per = copy.getPer();
		Volume = copy.getVolume();
		JongName = copy.getJongName();
		JongMok = copy.getJongMok();
		Money = copy.getMoney();
		DungRak = copy.getDungRak();
	}
	
	
	public double getDungRak() {
		return DungRak;
	}

	public void setDungRak(double dungRak) {
		DungRak = dungRak;
	}

	public long getMoney() {
		return Money;
	}

	public void setMoney(long money) {
		Money = money;
	}

	public long getStartJuka() {
		return StartJuka;
	}
	public void setStartJuka(long startJuka) {
		StartJuka = startJuka;
	}
	public long getHighJuka() {
		return HighJuka;
	}
	public void setHighJuka(long highJuka) {
		HighJuka = highJuka;
	}
	public long getUpJuka() {
		return UpJuka;
	}
	public void setUpJuka(long upJuka) {
		UpJuka = upJuka;
	}
	public long getLowJuka() {
		return LowJuka;
	}
	public void setLowJuka(long lowJuka) {
		LowJuka = lowJuka;
	}
	public long getDownJuka() {
		return DownJuka;
	}
	public void setDownJuka(long downJuka) {
		DownJuka = downJuka;
	}
	public long getAmount() {
		return Amount;
	}
	public void setAmount(long amount) {
		Amount = amount;
	}
	public long getCurJuka() {
		return CurJuka;
	}
	public void setCurJuka(long curJuka) {
		CurJuka = curJuka;
	}
	public long getPrevJuka() {
		return PrevJuka;
	}
	public void setPrevJuka(long prevJuka) {
		PrevJuka = prevJuka;
	}
	public int getFaceJuka() {
		return FaceJuka;
	}
	public void setFaceJuka(int faceJuka) {
		FaceJuka = faceJuka;
	}
	public long getDebi() {
		return Debi;
	}
	public void setDebi(long debi) {
		Debi = debi;
	}
	public int getHigh52() {
		return High52;
	}
	public void setHigh52(int high52) {
		High52 = high52;
	}
	public int getLow52() {
		return Low52;
	}
	public void setLow52(int low52) {
		Low52 = low52;
	}
	public double getPer() {
		return Per;
	}
	public void setPer(double per) {
		Per = per;
	}
	public long getVolume() {
		return Volume;
	}
	public void setVolume(long volume) {
		Volume = volume;
	}

	public String getJongName() {
		return JongName;
	}

	public void setJongName(String jongName) {
		JongName = jongName;
	}

	public int getJongMok() {
		return JongMok;
	}

	public void setJongMok(int jongMok) {
		JongMok = jongMok;
	}

	public String toString() {
		return 	StartJuka + "," + HighJuka + "," + UpJuka + "," + LowJuka + "," 
	+ DownJuka + "," + Amount + "," + CurJuka + "," + PrevJuka + "," + FaceJuka + "," + Debi
	 + "," + High52 + "," + Low52 + "," + Per + "," + Volume + "," + JongName + "," + JongMok
	  + "," + Money + "," + DungRak;
	}
}

