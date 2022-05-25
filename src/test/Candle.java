package test;

import java.awt.Color;

public class Candle
{
	int timestamp;
	double open, close, low, high;
	Color color;

	public Candle(int ts, double o, double c, double l, double h) {
		timestamp=ts;
		open=o;
		close=c; 
		low=l;
		high=h;
		color = ((close > open) ? Color.GREEN : Color.RED);
	}
	
	

	@Override
	public String toString() {
		return "ts=" + timestamp + ", OPEN=" + open + ", CLOSE=" + close + ", LOW=" + low + ", HIGH="
				+ high;
	}



	public double getClose() { return close; }

	public int getTs() { return timestamp; }

};
