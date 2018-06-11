package com.jaybaffoni;

import java.util.ArrayList;

public class Building {
	
	int floorCount = 0;
	ArrayList<Floor> floors;
	
	public Building(int floorCount) {
		this.floorCount = floorCount;
		floors = new ArrayList<Floor>();
		for(int x = 0; x < floorCount; x++) {
			floors.add(new Floor(x));
		}
	}
	
	public int getFloorCount() {
		return floorCount;
	}

}
