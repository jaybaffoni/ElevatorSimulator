package com.jaybaffoni;

public class Request {
	
	int end;
	int start;
	boolean goingUp;
	int boarded;
	
	public Request(int start, int destination) {
		this.start = start;
		this.end = destination;
		this.goingUp = (this.end > this.start);
		this.boarded = -1;
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getStart() {
		return start;
	}
	
	public boolean isGoingUp() {
		return goingUp;
	}
	
	public int getBoard() {
		return boarded;
	}
	
	public void setBoarded(int boarded) {
		this.boarded = boarded;
	}
	
	public String full() {
		return(boarded + "(" + start + "," + end + ")");
	}
	
	public String toString() {
		if(end < 10) {
			return("(0" + end + ")");
		}
		return("(" + end + ")");
	}

}
