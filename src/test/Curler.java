package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class Curler {
	
	public static double getCurrentPrice(String stockName) {
		long to = Instant.now().getEpochSecond();
		long from = to-3*24*3600;
		
		return getStock(stockName,from,to).last().close;
		
		
	}
	
	public static ArrayList<Double> getCloses(String sn, long from, long to){
	
		String s = getProcessOutput(sn,from,to);
		
		String[] ss = s.split("\n");
		
		String sstockName = ss[5];
		String[] t=ss[6].split(" ");
		
		ArrayList<Double> c = new ArrayList<>();
		
		t=ss[8].split(" ");
		for(int i =0;i<t.length;i++) {
			try {
			c.add(Double.parseDouble(t[i]));
			}
			catch(NumberFormatException nf) {}
		}
		
		return c;
	}
	
	public static Stock getStock(String stockName,long from, long to) {
		
		String s = getProcessOutput(stockName,from,to);
		
		String[] ss = s.split("\n");
		
		String sstockName = ss[5];
		String[] t=ss[6].split(" ");
		
		ArrayList<Integer> ts=new ArrayList<>();
		ArrayList<Double> o = new ArrayList<>();
		ArrayList<Double> c = new ArrayList<>();
		ArrayList<Double> l = new ArrayList<>();
		ArrayList<Double> h = new ArrayList<>();
	
		for(int i =0;i<t.length;i++) {
			try {
			ts.add(Integer.parseInt(t[i]));
			}
			catch(NumberFormatException nf) {}
		}
		
		t=ss[7].split(" ");
		for(int i =0;i<t.length;i++) {
			try {
			o.add(Double.parseDouble(t[i]));
			}
			catch(NumberFormatException nf) {}
		}
		
		t=ss[8].split(" ");
		for(int i =0;i<t.length;i++) {
			try {
			c.add(Double.parseDouble(t[i]));
			}
			catch(NumberFormatException nf) {}
		}
		
		t=ss[9].split(" ");
		for(int i =0;i<t.length;i++) {
			try {
			h.add(Double.parseDouble(t[i]));
			}
			catch(NumberFormatException nf) {}
		}
		
		t=ss[10].split(" ");
		for(int i =0;i<t.length;i++) {
			try {
			l.add(Double.parseDouble(t[i]));
			}
			catch(NumberFormatException nf) {}
		}
		
		return new Stock(sstockName,ts,o,c,l,h);
		
		
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Stock s = getStock("aapl", 1613072670, 1617531870);
		User.currentStock = s;
		//System.out.println(getCurrentPrice("doge-usd"));
		MainScreen m = new MainScreen();

	}
	
	public static String getProcessOutput()
    {
		return getProcessOutput("aapl", 1616072670, 1617531870);
        
    }
	
	public static String getProcessOutput(String stockName, long tsfrom, long tsto)
    {
		
        ProcessBuilder processBuilder = new ProcessBuilder("POOP_1.exe",
                stockName, Long.toString(tsfrom), Long.toString(tsto));

        processBuilder.redirectErrorStream(true);

        Process process;
		try {
			process = processBuilder.start();
		
        StringBuilder processOutput = new StringBuilder();

        try (BufferedReader processOutputReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));)
        {
            String readLine;

            while ((readLine = processOutputReader.readLine()) != null)
            {
                processOutput.append(readLine + System.lineSeparator());
            }

            process.waitFor();
            return processOutput.toString().trim();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return null;
    }
	
	
	
    }

