package com.jaybaffoni;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainTest implements Runnable{
	
	static Controller controller;
	static int speed = 50;
	static int floorCount = 20;
	static int elevatorCount = 4;
	static JLabel l1;
	static Thread simulate;
	
	public static void main(String[] args) {

		controller = new Controller(floorCount, elevatorCount);
		simulate = new Thread(new MainTest());
		
		System.out.println("Program Started");
		JFrame f= new JFrame("ELEVATOR SIMULATOR");  
		
	    l1=new JLabel("First Label.");  
	    l1.setBounds(100,50, 1400,500);  
	    l1.setFont(new Font("monospaced", Font.PLAIN, 14));
		JButton b=new JButton("RESTART");  
		b.setBounds(0,0,100,50);  
		b.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				if(!simulate.isAlive()) {
					simulate = new Thread(new MainTest());
					simulate.start();
				}
	        }  
		});  
		
	    f.add(b);
	    f.add(l1);  
	    f.setSize(1400,500);  
	    f.setLayout(null);  
	    f.setVisible(true);  
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//while(true) {
	    
	    simulate.start();
			//getAction();
		//}
		
		
	}
	
	public void run() {
        System.out.println("Hello from a thread!");
        start();
    }
	
	public static void start() {
		makeRequests();
		l1.setText(controller.print());
		while(!controller.isComplete()) {
			//getAction();
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			controller.tick();
			l1.setText(controller.print());
			//controllerOk = controller.isControllerOk();
		}
		System.out.println(controller.printQueue());
		//getAction();
	}
	
	public static void makeRequests() {
		for(int count = 0; count < 100; count++) {
			int start = ThreadLocalRandom.current().nextInt(0, floorCount);
			int end = ThreadLocalRandom.current().nextInt(0, floorCount);
			while(start == end) {
				end = ThreadLocalRandom.current().nextInt(0, floorCount);
			}
			controller.addRequest(new Request(start, end));
			//controller.addRequest(new Request(4, 2));
			//controller.addRequest(new Request(5, 3));
		}
	}
	
	public static void getAction() {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Action: ");
		keyboard.nextLine();
	}

}
