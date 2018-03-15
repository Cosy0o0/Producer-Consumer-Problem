/* 
 *  Problem producenta i konsumenta
 *
 *  Autor: Kamil Kozak
 *   Data: 09.12.2016 r.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

class Producent extends Thread
{
	static char item = 'A';
	
	Bufor buf;
	int number;
	
	public Producent(Bufor c, int number){ 
		buf = c;
		this.number = number;
	}
	
	public void run(){ 
		char c;
		while(true){
			c = item++;
			buf.put(number, c);
			try {
				sleep((int)(Math.random() * 1000));
				} catch (InterruptedException e) { }
		}
	}
}

class Konsument extends Thread
{
	Bufor buf;
    int number;

	public Konsument(Bufor c, int number){ 
		buf = c;
		this.number = number;
	}
	
	public void run(){ 
		while(true){ 
			buf.get(number);
			try {
				sleep((int)(Math.random() * 1000));
				} catch (InterruptedException e) { }
		}
	}
}

class Bufor
{
	private char contents;
	private int available = 0;
	private int capacity;
	boolean stopped=false;
	JTextArea console;
	
	Bufor (JTextArea cons, int capa) {
		console=cons;
		capacity=capa;
	}

	public synchronized int get(int kons){
		while(stopped)
 		{
 			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
		console.append("Konsument #" + kons + " chce zabrac\n");
		while (available == 0){
			try { console.append("Konsument #" + kons + "   bufor pusty - czekam\n");
				  wait();
				} catch (InterruptedException e) { }
		}
		available--;
		console.append("Konsument #" + kons + "      zabral: " + contents+"\n");
		notifyAll();
		return contents;
	}

	public synchronized void put(int prod, char value){
		while(stopped)
 		{
 			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
		console.append("Producent #" + prod + "  chce oddac: " + value+"\n");
		while (available == capacity){
			try { console.append("Producent #" + prod + "   bufor zajety - czekam\n");
				  wait();
				} catch (InterruptedException e) { }
		}
		contents = value;
		available++;
		console.append("Producent #" + prod + "       oddal: " + value+"\n");
		notifyAll();
	}
}


public class ProducentKonsument extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	static int iloscBuforow=1, iloscKonsumentow=1, iloscProducentow=1;
	Bufor bufor;
	boolean firstTime=true;
	
	private final String DESCRIPTION = "Program symuluje współdziałanie"
			+ "producentów i konsumentów.";
	
	private final String AUTOR = "ProducentKonsument\n"
			+ "Kamil Kozak";
	
	private JMenu[] menu = {
			new JMenu("Plik"),
			new JMenu("Pomoc")
	};
	
	private JMenuItem[] items = {
		new JMenuItem("Zakończ"),
		new JMenuItem("Informacje"),
		new JMenuItem("O programie")
	};
	
	
	private JLabel etykieta_bufora = new JLabel("Rozmiar bufora:");
	private JLabel etykieta_producenci = new JLabel("Ilość producentów:");
	private JLabel etykieta_konsumenci = new JLabel("Ilosc konsumentów:");
	
	private JComboBox<String> box_buforow = new JComboBox<String>();
	private JComboBox<String> box_producentow = new JComboBox<String>();
	private JComboBox<String> box_konsumentow = new JComboBox<String>();
	
	private JTextArea konsola = new JTextArea(20,30);
	private JScrollPane scroll;
	
	private JButton buttonStart = new JButton("Start");
	private JButton buttonStop = new JButton("Wstrzymaj symulację");
	
	
	public ProducentKonsument()
	{
		super ("ProducentKonsument - Aplikacja");
		setSize(500,500);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	     for (int i = 0; i < items.length; i++)
	       	items[i].addActionListener(this);
	     
	     menu[0].add(items[0]);
	     
	     menu[1].add(items[1]);
	     menu[1].add(items[2]);
	     
	     JMenuBar menubar = new JMenuBar();
	      for (int i = 0; i < menu.length; i++)
	      	menubar.add(menu[i]);
	      setJMenuBar(menubar);
	      
	      box_buforow.addItem("1");
	      box_buforow.addItem("2");
	      box_buforow.addItem("3");
	      box_buforow.addItem("4");
	      box_buforow.addItem("5");
	      box_buforow.addItem("6");
	      box_buforow.addItem("7");
	      box_buforow.addItem("8");
	      
	      box_producentow.addItem("1");
	      box_producentow.addItem("2");
	      box_producentow.addItem("3");
	      box_producentow.addItem("4");
	      box_producentow.addItem("5");
	      box_producentow.addItem("6");
	      box_producentow.addItem("7");
	      
	      box_konsumentow.addItem("1");
	      box_konsumentow.addItem("2");
	      box_konsumentow.addItem("3");
	      box_konsumentow.addItem("4");
	      box_konsumentow.addItem("5");
	      box_konsumentow.addItem("6");
	      box_konsumentow.addItem("7");
	      
	      JPanel panel = new JPanel();
	      panel.add(etykieta_bufora);
	      box_buforow.addActionListener(this);
	      panel.add(box_buforow);
	      panel.add(etykieta_producenci);
	      box_producentow.addActionListener(this);
	      panel.add(box_producentow);
	      panel.add(etykieta_konsumenci);
	      box_konsumentow.addActionListener(this);
	      panel.add(box_konsumentow);
	      
	      konsola.setEditable(false);
	      DefaultCaret caret = (DefaultCaret)konsola.getCaret();
	      caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	      konsola.setBounds(50,50,200,200);
	      panel.add(konsola);
	      
	      buttonStart.addActionListener(this);
	      panel.add(buttonStart);
	      buttonStop.addActionListener(this);
	      panel.add(buttonStop);
	      
	      buttonStop.setEnabled(false);
	      
	      scroll = new JScrollPane(konsola);
	      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      scroll.setBounds(30, 25, 450, 250);
	      panel.add(scroll);
	      
	      setContentPane(panel);
	      setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object zrodlo = evt.getSource();
		
		if (zrodlo == items[0])
			System.exit(0);
		if (zrodlo == items[1])
			JOptionPane.showMessageDialog(null, DESCRIPTION);
		if (zrodlo == items[2])
			JOptionPane.showMessageDialog(null, AUTOR);
		
		if (zrodlo == box_buforow) {
			String ilosc = box_buforow.getSelectedItem().toString();
			if (ilosc.equals("1"))
				iloscBuforow=1;
			if (ilosc.equals("2"))
				iloscBuforow=2;
			if (ilosc.equals("3"))
				iloscBuforow=3;
			if (ilosc.equals("4"))
				iloscBuforow=4;
			if (ilosc.equals("5"))
				iloscBuforow=5;
			if (ilosc.equals("6"))
				iloscBuforow=6;
			if (ilosc.equals("7"))
				iloscBuforow=7;
			if (ilosc.equals("8"))
				iloscBuforow=8;
		}
		
		if (zrodlo == box_producentow) {
			String ilosc = box_producentow.getSelectedItem().toString();
			if (ilosc.equals("1"))
				iloscProducentow=1;
			if (ilosc.equals("2"))
				iloscProducentow=2;
			if (ilosc.equals("3"))
				iloscProducentow=3;
			if (ilosc.equals("4"))
				iloscProducentow=4;
			if (ilosc.equals("5"))
				iloscProducentow=5;
			if (ilosc.equals("6"))
				iloscProducentow=6;
			if (ilosc.equals("7"))
				iloscProducentow=7;
		}
		
		if (zrodlo == box_konsumentow) {
			String ilosc = box_konsumentow.getSelectedItem().toString();
			if (ilosc.equals("1"))
				iloscKonsumentow=1;
			if (ilosc.equals("2"))
				iloscKonsumentow=2;
			if (ilosc.equals("3"))
				iloscKonsumentow=3;
			if (ilosc.equals("4"))
				iloscKonsumentow=4;
			if (ilosc.equals("5"))
				iloscKonsumentow=5;
			if (ilosc.equals("6"))
				iloscKonsumentow=6;
			if (ilosc.equals("7"))
				iloscKonsumentow=7;
		}
		
		if (zrodlo == buttonStart) { 
			if (firstTime) {
			LinkedList<Producent> producents=new LinkedList<Producent>();
			LinkedList<Konsument> consuments=new LinkedList<Konsument>();
			
			bufor = new Bufor(this.konsola,iloscBuforow);
			
			for(int i=0;i<iloscProducentow;i++) {
				producents.add(new Producent(this.bufor, i));
			}
			
			for(int i=0;i<iloscKonsumentow;i++) {
				consuments.add(new Konsument(this.bufor, i));
			}
			
			for(Konsument c : consuments)
			{	
				c.start();
			}
			
			for(Producent p : producents)
			{	
				p.start();
			}
			
			box_konsumentow.setEnabled(false);
			box_producentow.setEnabled(false);
			box_buforow.setEnabled(false);
			this.buttonStart.setEnabled(false);
			this.buttonStop.setEnabled(true);
			
			firstTime=false;
			} else {
				synchronized(this.bufor)
				{
					bufor.stopped=false;
					bufor.notifyAll();
				}
				this.buttonStart.setEnabled(false);
				this.buttonStop.setEnabled(true);
			}
		}
		
		if (zrodlo == buttonStop) {
			synchronized(this.bufor)
			{
				bufor.stopped=true;
			}
			this.buttonStart.setEnabled(true);
			this.buttonStop.setEnabled(false);
		}
	}

	public static void main(String[] args){
		new ProducentKonsument();
	}
}
