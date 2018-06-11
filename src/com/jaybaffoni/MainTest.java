package com.jaybaffoni;

import java.awt.Font;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainTest {
	
	static Controller controller;
	static int speed = 100;
	static int floorCount = 5;

	public static void main(String[] args) {

		System.out.println("Program Started");
		JFrame f= new JFrame("ELEVATOR SIMULATOR");  
	    JLabel l1;
	    l1=new JLabel("First Label.");  
	    l1.setBounds(0,0, 1200,500);  
	    l1.setFont(new Font("monospaced", Font.PLAIN, 14));
	    f.add(l1);  
	    f.setSize(1200,500);  
	    f.setLayout(null);  
	    f.setVisible(true);  
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		controller = new Controller(floorCount);
		boolean controllerOk = true;
		
		while(controllerOk) {
			
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
			//getAction();
		}
		
		
		
		
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
