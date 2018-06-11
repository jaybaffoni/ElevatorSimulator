package com.jaybaffoni;

public class Request {
	
	int end;
	int start;
	boolean goingUp;
	boolean boarded;
	
	public Request(int start, int destination) {
		this.start = start;
		this.end = destination;
		this.goingUp = (this.end > this.start);
		this.boarded = false;
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
	
	public boolean isBoarded() {
		return boarded;
	}
	
	public void setBoarded(boolean boarded) {
		this.boarded = boarded;
	}
	
	public String full() {
		String board = "B";
		if(!boarded) {
			board = "N";
		}
		return(board + "(" + start + "," + end + ")");
	}
	
	public String toString() {
		String board = "B";
		if(!boarded) {
			board = "N";
		}
		if(end < 10) {
			return("(0" + end + ")");
		}
		return("(" + end + ")");
	}

}
