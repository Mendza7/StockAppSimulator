package test;

import java.util.ArrayList;
import java.util.Collections;

public class Stock {
	String name;
	ArrayList<Candle> candles = new ArrayList<>();
	double min, max;
	double ratio;
	
	ArrayList<Double> MAs = new ArrayList<Double>();
	ArrayList<Double> EMAs = new ArrayList<Double>();
	static int n;
	ArrayList<Double> EMAaux;
	static boolean advanced_info=false;
	
	
	void prepareEMA() {
		EMAaux = Curler.getCloses(name.replace("\r",""), candles.get(0).timestamp-n*2*24*3600, candles.get(0).timestamp);
		while(EMAaux.size()>n) {
			EMAaux.remove(0);
		}
	}
	
	void calculateMA() {
		int i = 0;
		
		while(i<candles.size()) {
			double sum = 0;
			if (i < n) {
				int k = n - i;
				for ( int temp = EMAaux.size()-k;temp < EMAaux.size();temp++) {
					sum+=EMAaux.get(temp);
				}
				for ( int t = 0; t< i; t++) {
					sum+=candles.get(t).close;
				}
			}
			else{
				for( int t = i-n;t<i;t++) {
					sum+=candles.get(t).close;
				}
			}
			MAs.add(sum/n);
			i++;
		}
	}
	
	void calculateEMA() {
		int i = 0;
		if (MAs.size()!=candles.size())calculateMA();
		while(i<candles.size()) {
			if (i < n) EMAs.add(MAs.get(i));
			else {
				double t = candles.get(i).close * (2.0/(n+1)) + EMAs.get(i-1)*(1-(2.0/(n+1)));
				EMAs.add(t);
			}
			i++;
		}
	}

	public Stock(String sn,ArrayList<Integer> ts, ArrayList<Double> o, ArrayList<Double> c, ArrayList<Double> l, ArrayList<Double> h) {
		name= sn;
		
		ArrayList<Double> maxes = new ArrayList<>();
		ArrayList<Double> minimums = new ArrayList<>();
		
		maxes.add(Collections.max(o));
		maxes.add(Collections.max(c));
		maxes.add(Collections.max(l));
		maxes.add(Collections.max(h));
		
		minimums.add(Collections.min(o));
		minimums.add(Collections.min(c));
		minimums.add(Collections.min(l));
		minimums.add(Collections.min(h));
		
		max = Collections.max(maxes);
		min = Collections.min(minimums);
		
		ratio = max- min;
		
		
		for (int i = 0; i < o.size(); i++) {
			candles.add(new Candle(ts.get(i), o.get(i), c.get(i), l.get(i), h.get(i)));
		}
		
		if(advanced_info) {
			prepareEMA();
			calculateEMA();
		}
	}
	
	public Candle last() {
		return candles.get(candles.size() - 1);
	}

	public void setInfo() { advanced_info = true; }

	public String getName() { return name; }
	

}
