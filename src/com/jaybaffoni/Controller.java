package com.jaybaffoni;

import java.util.ArrayList;
import java.util.Iterator;

public class Controller {
	
	int floorCount;
	Floor[] building;
	ArrayList<Request> queue;
	Elevator elevator;
	
	public Controller(int floorCount) {
		this.floorCount = floorCount;
		queue = new ArrayList<Request>();
		building = new Floor[floorCount];
		elevator = new Elevator(5, building, queue);
		fillFloors();
	}
	
	public void addRequest(Request r) {
		
		building[r.getStart()].addRequest(r);
		
		if(elevator.isReady()) {
			elevator.addExternalStop(r);
		} else {
			queue.add(r);
		}
		
	}
	
	public void tick() {
		//printState();
		elevator.tick();
		//print();
		
	}
	
	public void fillFloors() {
		for(int x = 0; x < floorCount; x++) {
			building[x] = new Floor(x);
		}
	}
	
	public String print() {
		String toReturn = "<html>" + elevator.getState();
		//System.out.println("BUILDING");
		for(int x = floorCount-1; x >= 0; x--) {
			if(x == elevator.getCurrentFloor()) {
				toReturn += "<br>" + elevator.toString();
				toReturn += "X-";
			} else {
				toReturn += "<br>" + "----------------------";
			}
			toReturn += "FLOOR: " + x + "\t";
			toReturn += building[x].toString();
		}
		System.out.println(toReturn);
		return toReturn + "</html>";
	}
	
	public void printState() {
		System.out.println(elevator.getState());
	}
	
	public boolean isComplete() {
		return queue.isEmpty() && elevator.isReady();
	}
	
	/*public boolean isControllerOk() {
		return elevator.isReady() && 
	}*/
	
	public boolean sameDirection(Request r) {
		return elevator.isGoingUp() == r.isGoingUp();
	}
	
	public void printPendingRequests() {
		String toPrint = "\tController Requests: ";
		for(Request r: queue) {
			toPrint += r.toString() + ",";
		}
		System.out.println(toPrint);
	}

}
