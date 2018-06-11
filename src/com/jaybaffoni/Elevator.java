package com.jaybaffoni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class Elevator {
	
	enum State {
		READY, MOVING, OPENING_DOORS, UNLOADING, LOADING, CLOSING_DOORS;
	}
	
	int elevatorNumber;
	State state;
	int currentFloor = 0;
	ArrayList<Integer> stops;
	ArrayList<Request> requests;
	boolean goingUp;
	int capacity, vacancy;
	Floor[] building;
	Request workingRequest = null;
	ArrayList<Request> controllerQueue;
	
	public Elevator(int elevatorNumber, int capacity, Floor[] building, ArrayList<Request> queue) {
		stops = new ArrayList<Integer>(4);
		requests = new ArrayList<Request>();
		goingUp = false;
		this.capacity = this.vacancy = capacity;
		this.state = State.READY;
		this.building = building;
		this.controllerQueue = queue;
		this.elevatorNumber = elevatorNumber;
		currentFloor = ThreadLocalRandom.current().nextInt(0, building.length);
	}
	
	
	public void tick() {
		
		//System.out.println(controllerQueue.size() + " " + workingRequest);
		printStops();
		
		if(workingRequest != null) {
			System.out.println(workingRequest.full());
			if(workingRequest.getBoard() != elevatorNumber && workingRequest.getBoard() != -1) {
				workingRequest = null;
			}
		}
		
		switch(state){
			case READY:
				break;
			case MOVING:
				move();
				break;
			case OPENING_DOORS:
				if(!stops.isEmpty() && currentFloor == stops.get(nextStopIndex())) {
					stops.remove(nextStopIndex());
				}
				if(controllerQueue.isEmpty()) {
					state = State.CLOSING_DOORS;
				} else {
					//if there are no more stops and no working request, get a new request
					if(stops.isEmpty()) {
						
						if(workingRequest == null) {
							System.out.println("got wr from opening doors");
							Request temp = controllerQueue.get(0);
							workingRequest = temp;
							controllerQueue.remove(0);
							//get new direction
							addExternalStop(temp);
						} else {
							goingUp = workingRequest.getEnd() > currentFloor;
						}
						
					}
					
				}
				if(hasPassengersDeparting()) {
					state = State.UNLOADING;
				} else {
					state = State.LOADING;
				}
				break;
			case UNLOADING:
				unload();
				break;
			case LOADING:
				load();
				state = State.CLOSING_DOORS;
				break;
			case CLOSING_DOORS:
				if(stops.isEmpty()) {
					
					if(workingRequest == null) {
						state = State.READY;
						if(!controllerQueue.isEmpty()) {
							Request temp = controllerQueue.get(0);
							workingRequest = temp;
							controllerQueue.remove(0);
							//get new direction
							addExternalStop(temp);
							System.out.println("got wr from closing doors");
						}
					} else {
						state = State.MOVING;
						
						if(workingRequest.getBoard() == elevatorNumber) {
							addStop(workingRequest.getEnd());
							goingUp = workingRequest.getEnd() > currentFloor;
						} else {
							addStop(workingRequest.getStart());
							goingUp = workingRequest.getStart() > currentFloor;
						}
					}
					
				} else {
					state = State.MOVING;
				}
				/*if(stops.isEmpty()) {
					System.out.println("NO STOPS");
				} else {
					System.out.println(stops.get(nextStopIndex()));
					if(workingRequest != null) {
						System.out.println(workingRequest.toString());
					}
				}*/
				break;
		}
		
	}

	
	public void addExternalStop(Request r) {
		
		int stop = r.getStart();
		
		workingRequest = r;
		
		
		if(stop > currentFloor) {
			addStop(stop);
			goingUp = true;
			if(state == State.READY) {
				state = State.MOVING;
			}
		} else if(stop < currentFloor) {
			addStop(stop);
			goingUp = false;
			if(state == State.READY) {
				state = State.MOVING;
			}
		} else {
			goingUp = r.getEnd() > currentFloor;
			if(state == State.READY) {
				state = State.OPENING_DOORS;
			} else if(state == State.UNLOADING) {
				if(building[currentFloor].hasPassengers(goingUp)) {
					state = State.LOADING;
				} else {
					state = State.CLOSING_DOORS;
				}
			} else if(state == State.OPENING_DOORS) {
				
			}
		}
		
		//System.out.println(r.getStart() + "," + r.getEnd() + "," + goingUp);
		
	}
	
	public boolean isReady() {
		return state == State.READY;
	}
	
	public boolean hasPassengersDeparting() {
		if(requests.isEmpty()) {
			return false;
		}
		for(Request r: requests) {
			if(r.getEnd() == currentFloor) {
				return true;
			}
		}
		return false;
	}
	
	public void move() {
		//System.out.println("Move called");
		if(goingUp) {
			currentFloor++;
		} else {
			currentFloor--;
		}
		//System.out.println(stops.size());
		if(currentFloor == stops.get(nextStopIndex()) || building[currentFloor].hasPassengers(goingUp)) {
			state = State.OPENING_DOORS;
		}
		//System.out.println("Move finished");
	}
	
	public void unload() {
		Iterator<Request> iterator = requests.iterator();
		while (iterator.hasNext()) {
		   Request r = iterator.next();
		   if(r.getEnd() == currentFloor) {
			   building[currentFloor].addToFloor(r);
			   iterator.remove();
			   vacancy++;
			   if(r == workingRequest) {
				   workingRequest = null;
				   //getWorkingRequest();
			   }
		   }
		}
		
		if(stops.isEmpty() && workingRequest == null) {
			if(getWorkingRequest()) {
				System.out.println("got wr from unloading");
			} else {
				state = State.CLOSING_DOORS;
			}
			
		} else {
			if(building[currentFloor].hasPassengers(goingUp)) {
				state = State.LOADING;
			} else {
				state = State.CLOSING_DOORS;
			}
		}
	
		
		

		
		
	}
	
	public boolean getWorkingRequest() {
		if(!controllerQueue.isEmpty()) {
			Request temp = controllerQueue.get(0);
			workingRequest = temp;
			controllerQueue.remove(0);
			//get new direction
			addExternalStop(temp);
			return true;
		}
		return false;
	}
	
	public void load() {
		boolean loadingWR = building[currentFloor].contains(workingRequest);
		
		while(vacancy > 0 && building[currentFloor].hasPassengers(goingUp)) {
			Request temp = building[currentFloor].pop(goingUp);
			requests.add(temp);
			temp.setBoarded(elevatorNumber);
			loadingWR = false;
			controllerQueue.remove(temp);
			addStop(temp.getEnd());
			//System.out.println("ADDED STOP: " + temp.getEnd());
			vacancy--;
		}
		Collections.sort(stops);
		
		if(loadingWR) {
			//The working request did not get on board bc elevator was at capacity
			controllerQueue.add(0, workingRequest);
			workingRequest = null;
		}
	}
	
	public void addStop(int i) {
		if(!stops.contains(i)){
			stops.add(i);
		}
		Collections.sort(stops);
	}
	
	/*public void process() {
		for(Request r: requests) {
			if(r.getStart() == currentFloor) {
			   //addStop(r.getEnd());
			   r.setBoarded(true);
			}
		}
		Iterator<Request> iterator = requests.iterator();
		while (iterator.hasNext()) {
		   Request r = iterator.next();
		   if(r.getEnd() == currentFloor && r.isBoarded()) {
			   iterator.remove();
		   }
		}
		
		if(noMoreStops()) {
			goingUp = false;
		} else {
			goingUp = stops.get(nextStopIndex()) > currentFloor;
			Collections.sort(stops);
		}
		
	}*/
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public int nextStopIndex() {
		if(goingUp) {
			return 0;
		} else {
			return stops.size() - 1;
		}
	}
	
	public int getFinalStop() {
		if(stops.size() == 0) {
			return currentFloor;
		} else {
			return stops.get(stops.size() - 1);
		}
	}
	
	public Boolean isGoingUp() {
		return goingUp;
	}
	
	public boolean noMoreStops() {
		return stops.size() == 0;
	}
	
	public int getVacancy() {
		return vacancy;
	}
	
	public String getState() {
		return state.toString();
	}
	
	/*public void setWorkingRequest(Request r) {
		this.workingRequest = r;
	}*/
	
	public void printStops() {
		String toPrint = "Elevator Stops: ";
		for(Integer i: stops) {
			toPrint += i.toString() + ",";
		}
		System.out.println(toPrint);
	}
	
	public String toString() {
		String toReturn = "";
		for(Request i: requests) {
			toReturn += i.toString();
		}
		while(toReturn.length() < capacity * 4) {
			toReturn = "-" + toReturn;
		}
		return toReturn;
	}

}
