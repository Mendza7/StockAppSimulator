package test;

public class Transaction {
	public enum type { BUY, SELL };
	
	static int TransactionID;
	double amount;
	double price;
	String stock;
	int id;
	type Ttype;

	
	public Transaction(double a, double p, String stockName,type T) {
		amount = a;
		price=p;
		stock=stockName;
		Ttype= T;
		id =TransactionID++;
	}
	
	public String[] toTable() {
		String [] s = {String.valueOf(id), stock,String.valueOf(amount),String.valueOf(price),String.valueOf(Curler.getCurrentPrice(stock)-price),String.valueOf((Curler.getCurrentPrice(stock)/price)*100)};
		return s;
	}
	
	public String getStock() { return stock; }
	public double getAmount() { return amount; }
	public double getPrice() { return price; }

}
