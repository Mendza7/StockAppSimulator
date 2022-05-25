package test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MainScreen extends Frame {
	private Scene scene = new Scene(this);
	
	private Panel eastPanel = new Panel(new GridLayout(2,1,0,5));
	public Panel centerPanel = new Panel(new GridLayout(1,1));

	public JTable table;
	String[] columnNames = {"Id", "Name", "Amount","Price","PriceChg","%"};
	String[][] dummy = {{"test","test","test","test","test","test"}};
	
	public DefaultTableModel model = new DefaultTableModel(dummy,columnNames);

	Label balLabel;
	
	public MainScreen() {
		scene.owner = this;
		User.ms = this;
		setBounds(200, 200, 1280, 720);
		setResizable(true);
		table = new JTable(model);
		
		
		
		setTitle("Stocks");
		populateWindow();
		showLoginDialog();
		
		//pack();
		
	}
	
	private void populateWindow() {
		
		
		setLayout(new BorderLayout());
		setBackground(Color.GRAY);
		
		populateEast();
		
		
		
		centerPanel.add(scene);
		
		
		add(centerPanel, BorderLayout.CENTER);
		add(eastPanel, BorderLayout.EAST);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//showQuitDialog();
				dispose();
			}
		});
		
		setVisible(true);
		
	}

	private void populateEast() {
		TextField actionName = new TextField(10);
		TextField timestampFrom = new TextField(10);
		TextField timestampTo = new TextField(10);
		TextField amountText = new TextField(10);
		Button get = new Button("Get");
		Button buyButton = new Button("Buy");
		Button sellButton = new Button("Sell");
		Button advInfo = new Button("Adv info");
		
		
		get.addActionListener((ae)->{
			String sn = actionName.getText();
			long tsf = Long.parseLong(timestampFrom.getText());
			long tst = Long.parseLong(timestampTo.getText());
			
			User.currentStock = Curler.getStock(sn, tsf, tst);
			scene.reset();
			scene.repaint();
			
		});
		
		advInfo.addActionListener((ae)->{
			Stock.n = 10;
			Stock.advanced_info = !Stock.advanced_info;
			if(Stock.advanced_info) {
				User.currentStock.prepareEMA();
				User.currentStock.calculateEMA();
			}
		});
		
		
		buyButton.addActionListener((ae)->{
			String sn = actionName.getText();
			double amt = Double.parseDouble(amountText.getText());
			User.currentUser.buy(sn,amt);
		});
		
		sellButton.addActionListener((ae)->{
			String sn = actionName.getText();
			double amt = Double.parseDouble(amountText.getText());
			User.currentUser.sell(sn,amt);
		});
		
		//gornji panel u East
				
				
				
				
				
				JScrollPane upperPanel = new JScrollPane(table);
				upperPanel.setBackground(Color.GREEN);
				//BALANCE - TO_DO
				
				
				//donji panel u East
				Panel lowerPanel = new Panel(new GridLayout(0,1,0,5));
				lowerPanel.setBackground(Color.GRAY);
				
				Panel balancePanel = new Panel();
				balLabel = new Label("BALANCE:               ");
				balancePanel.add(balLabel);
				
				Panel actionNamePanel = new Panel();
				actionNamePanel.add(new Label("Action name: "));
				actionNamePanel.add(actionName);
				
				Panel timestampFromPanel = new Panel();
				timestampFromPanel.add(new Label("Timestamp from: "));
				timestampFromPanel.add(timestampFrom);
				
				Panel timestampToPanel = new Panel();
				timestampToPanel.add(new Label("Timestamp to: "));
				timestampToPanel.add(timestampTo);
				
				//button Get
				Panel getPanel = new Panel(new GridLayout(1,2,10,10));
				getPanel.add(get);
				getPanel.add(advInfo);
				//get - action listener
				
				Panel amountPanel = new Panel();
				amountPanel.add(new Label("Amount: "));
				amountPanel.add(amountText);
				
				
				Panel buysellPanel = new Panel(new GridLayout(1,2,10,10));
				buysellPanel.add(buyButton);
				buysellPanel.add(sellButton);
				
				
				lowerPanel.add(balancePanel);
				lowerPanel.add(timestampFromPanel);
				lowerPanel.add(timestampToPanel);
				lowerPanel.add(actionNamePanel);
				
				lowerPanel.add(getPanel);
				lowerPanel.add(amountPanel);
				lowerPanel.add(buysellPanel);
				
				eastPanel.add(upperPanel);
				eastPanel.add(lowerPanel);
	}
	
	private void showLoginDialog() {
		TextField username = new TextField(10);
		TextField password = new TextField(10);
		TextField balance = new TextField(10);
		Button login = new Button("Login");
		Button register = new Button("Register");
		
		Dialog lgn = new Dialog(this, ModalityType.APPLICATION_MODAL);
		lgn.setTitle("Login");
		lgn.setBounds(700, 700, 330, 330);
		lgn.setResizable(false);
		
		Panel content = new Panel(new GridLayout(0, 1, 0, 5));
		
		Panel userPanel = new Panel();
		userPanel.add(new Label("Username: "));
		userPanel.add(username);
		
		content.add(userPanel);
		
		
		Panel passPanel = new Panel();
		password.setEchoChar('*');
		passPanel.setBackground(Color.YELLOW);
		passPanel.add(new Label("Password: "));
		passPanel.add(password);
		
		content.add(passPanel);
		
		Panel balancePanel = new Panel();
		balancePanel.add(new Label("Balance: "));
		balancePanel.add(balance);
	
		content.add(balancePanel);
		
		Panel buttonPanel = new Panel(new GridLayout(0, 2, 0, 5));
		
		login.addActionListener((ae) -> {
			String user = username.getText();
			String pass = password.getText();
			User u = new User();
			if (u.Login(user,pass)) {
				lgn.dispose();
			}
			else {
				showLoginDialog();
			}
		});
		
		register.addActionListener((ae) -> {
			String user = username.getText();
			String pass = password.getText();
			double bal = Double.parseDouble(balance.getText());
			
			User.Register(user,pass,bal);
		});
		
		
		buttonPanel.add(login);
		buttonPanel.add(register);
		content.add(buttonPanel);
		
		lgn.add(content, BorderLayout.CENTER);
		
		
		lgn.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				lgn.dispose();
				
			}
		});
		
		
		
		lgn.setVisible(true);
	}
	
	private void showQuitDialog() {
		
		Button yes = new Button("Yes");
		Button no = new Button("No");
		
		Dialog qt = new Dialog(this, ModalityType.APPLICATION_MODAL);
		qt.setTitle("Quit");
		qt.setBounds(300, 400, 700, 600);
		qt.setResizable(false);
		
		Panel quitPanel = new Panel(new GridLayout(2, 1, 0, 5));
		
		Label msg = new Label("Are you sure you want to quit?");
		
		Panel quitbuttonPanel = new Panel(new GridLayout(1, 2, 0, 5));
		quitbuttonPanel.add(yes);

		yes.addActionListener((ae) -> {
			this.dispose();
		});
		
		no.addActionListener((ae) -> {
			qt.dispose();
		});
		
		qt.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				qt.dispose();
				
			}
		});
		
		
		quitbuttonPanel.add(no);
		
		quitPanel.add(msg);
		quitPanel.add(quitbuttonPanel);
		qt.add(quitPanel);
		qt.pack();
		
		qt.setVisible(true);
		
	}

	
	public void updateTable() {
				
		ArrayList<String[]> alltrans = new ArrayList<>();
		
		
		for(ArrayList<Transaction> t : User.portfolio.values()) {
			for( Transaction tt: t) {
				alltrans.add(tt.toTable());
			}
		}
		int i =0;
		String[][] data = new String[alltrans.size()][];
		for(String[] t:alltrans) {
			data[i++] = t;
		}
		
		model.setDataVector(data, columnNames);
		table.setModel(model);
		model.fireTableDataChanged();
		
	}
	
	public void updateLabel() {
		if(User.currentUser!=null)balLabel.setText("Balance: "+ User.currentUser.bal);
	}
}
