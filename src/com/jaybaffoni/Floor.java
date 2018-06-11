package com.jaybaffoni;

import java.util.ArrayList;

public class Floor {
	
	int floorNumber;
	ArrayList<Employee> employeesOnFloor;
	ArrayList<Employee> employeesInElevatorQueue;
	ArrayList<Request> elevatorQueue;
	ArrayList<Request> requestsOnFloor;
	boolean upFlag = false;
	boolean downFlag = false;
	
	public Floor(int floorNumber) {
		this.floorNumber = floorNumber;
		elevatorQueue = new ArrayList<Request>();
		requestsOnFloor = new ArrayList<Request>();
	}
	
	public void addRequest(Request r) {
		elevatorQueue.add(r);
	}
	
	public void addToFloor(Request r) {
		requestsOnFloor.add(r);
	}
	
	public boolean contains(Request r) {
		return elevatorQueue.contains(r);
	}
	
	public boolean hasPassengers(boolean goingUp) {
		if(isEmpty()) {
			return false;
		}
		for(Request r: elevatorQueue) {
			if(r.isGoingUp() == goingUp) {
				return true;
			}
		}
		return false;
	}
	
	public Request peek() {
		if(isEmpty()) {
			return null;
		}
		return elevatorQueue.get(0);
	}
	
	public Request pop(boolean goingUp) {
		for(Request r: elevatorQueue) {
			if(r.isGoingUp() == goingUp) {
				elevatorQueue.remove(r);
				return r;
			}
		}
		return null;
	}
	
	public void reset() {
		// TODO reset flags if there are still items in queue
	}
	
	public boolean isEmpty() {
		return elevatorQueue.size() == 0;
	}
	
	public String toString() {
		String toReturn = "";
		for(Request r: elevatorQueue) {
			toReturn += r.toString();
		}
		return toReturn;
	}

}
