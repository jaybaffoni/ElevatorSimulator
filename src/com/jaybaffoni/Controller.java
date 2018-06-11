package com.jaybaffoni;

import java.util.ArrayList;
import java.util.Iterator;

public class Controller {
	
	int floorCount;
	Floor[] building;
	ArrayList<Request> queue;
	//Elevator elevator;
	Elevator[] elevators;
	
	public Controller(int floorCount, int elevatorCount) {
		this.floorCount = floorCount;
		queue = new ArrayList<Request>();
		building = new Floor[floorCount];
		elevators = new Elevator[elevatorCount];
		for(int i = 0; i < elevatorCount; i++) {
			elevators[i] = new Elevator(i, 5, building, queue);
		}
		fillFloors();
	}
	
	public void addRequest(Request r) {
		
		building[r.getStart()].addRequest(r);
		
		for(Elevator e: elevators) {
			if(e.isReady()) {
				e.addExternalStop(r);
			}
		}
		queue.add(r);
		
	}
	
	public void tick() {
		//printState();
		for(Elevator e: elevators) {
			e.tick();
		}
		//print();
		
	}
	
	public void fillFloors() {
		for(int x = 0; x < floorCount; x++) {
			building[x] = new Floor(x);
		}
	}
	
	public String print() {
		String toReturn = "<html>";
		for(Elevator e: elevators) {
			String ministring = e.getState();
			while(ministring.length() < (e.capacity * 4) + 2) {
				ministring = "." + ministring;
			}
			toReturn += ministring;
		}
		
		for(int x = floorCount-1; x >= 0; x--) {
			toReturn += "<br>";
			for(Elevator e: elevators) {
				if(x == e.getCurrentFloor()) {
					toReturn += e.toString();
					toReturn += "X-";
				} else {
					toReturn += "----------------------";
				}
			}
			
			toReturn += "FLOOR: " + x + "\t";
			toReturn += building[x].toString();
		}
		//System.out.println(toReturn);
		return toReturn + "</html>";
	}
	
	public String printQueue() {
		String toReturn = "QUEUE: ";
		for(Request r: queue) {
			toReturn += r.toString() + ",";
		}
		return toReturn;
	}
	
	public void printState(Elevator elevator) {
		System.out.println(elevator.getState());
	}
	
	public boolean isComplete() {
		return queue.isEmpty() && allElevatorsReady();
	}
	
	public boolean allElevatorsReady() {
		for(Elevator e: elevators) {
			if(!e.isReady()) {
				return false;
			}
		}
		return true;
	}
	
	/*public boolean isControllerOk() {
		return elevator.isReady() && 
	}*/
	
	/*public boolean sameDirection(Request r) {
		return elevator.isGoingUp() == r.isGoingUp();
	}*/
	
	public void printPendingRequests() {
		String toPrint = "\tController Requests: ";
		for(Request r: queue) {
			toPrint += r.toString() + ",";
		}
		System.out.println(toPrint);
	}

}
