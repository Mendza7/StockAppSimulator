package test;

import java.io.BufferedWriter;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import test.Transaction.type;

import java.util.ArrayList;
import java.util.HashMap;



public class User {
	
	double bal;
	private	String user;
	public static HashMap<String,ArrayList<Transaction>> portfolio = new HashMap<String,ArrayList<Transaction>>();
	public static User currentUser;
	public static Stock currentStock;
	public static MainScreen ms;
	

	boolean Login(String user,String pass) {
		File myObj = new File("users.txt");
		Scanner myReader;
		try {
			myReader = new Scanner(myObj);
		
			while (myReader.hasNextLine()) {
				String l = myReader.nextLine();
				if (l.equals(user)) {
					String p = myReader.nextLine();
					if(p.equals(pass)) {
						this.bal = myReader.nextDouble();
						this.user = user;
						System.out.println("SUCCESS");
						myReader.close();
						currentUser = this;
						portfolio = new HashMap<>();
						ms.updateLabel();
						return true;
					}
				}
				else {
					myReader.nextLine();
					myReader.nextLine();
				}
			}
			myReader.close();
			return false;
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	public static void Register(String user, String pass, double balance) {
		try {
			User u = new User();
			if(u.Login(user,pass)) {
				System.out.println("User vec postoji, logovanje");
				return;
			}
			BufferedWriter output = new BufferedWriter(new FileWriter("users.txt",true));
			
			output.append(user);
			output.newLine();
			output.append(pass);
			output.newLine();
			output.append(String.format("%f", balance));
			output.newLine();
			
			output.close();
			u.Login(user, pass);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean sell(String stockName, double amount) {
		ArrayList<Transaction> trans = portfolio.get(stockName);
		if (trans==null) {
			return false;
		}
		
		double price = Curler.getCurrentPrice(stockName);
		
		double remaining = amount;
		double sum = 0;
		for (int i =0;i<trans.size();i++) {
			if (trans.get(i).Ttype==type.BUY) {
				sum+=trans.get(i).getAmount();
			}
		}
		if (sum<amount) {
			return false;
		}
		else {
			int i =0;
			while(remaining>0 && i<trans.size()) {
				if (trans.get(i).Ttype == type.BUY) {
					if(trans.get(i).amount>remaining) {
						trans.get(i).amount-=remaining;
						remaining =0;
					}
					else {
						remaining-=trans.get(i).amount;
						trans.remove(i);
						i--;
					}
				}
				i++;
			}
			trans.add(new Transaction(amount,price,stockName,type.SELL));
			bal += amount*price;
			ms.updateLabel();

		}
		ms.updateTable();
		ms.repaint();
		return true;
		
	}
	
	
	public boolean buy(String stockName, double amount) {
		double price = Curler.getCurrentPrice(stockName);
		if (bal < price* amount) {
			return false;
		}
		
		if(portfolio.get(stockName)==null) {
			portfolio.put(stockName, new ArrayList<Transaction>());
		}
		portfolio.get(stockName).add(new Transaction(amount, price, stockName, type.BUY));
		
		bal-=price*amount;
		ms.updateLabel();
		ms.updateTable();
		ms.repaint();
		return true;
		
	}
	
	@Override
	public String toString() {
		return "User [bal=" + bal + ", user=" + user + "]";
	}
	
	
		

}
