package classes.model;

import java.util.LinkedList;
import java.util.List;

public class Competition {
	
	private String name;
	// Wenn Wettkampf auf Distanz
	private int lapLength; // in Metern
	private int distance; // Gesamtdistanz in Metern
	// Wenn Wettkampf auf Zeit
	private int time; // in Sekunden
	
	private LinkedList<Lap> laps;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLapLength() {
		return lapLength;
	}
	
	public void setLapLength(int lapLength) {
		this.lapLength = lapLength;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public LinkedList<Lap> getLaps() {
		return laps;
	}

	public void setLaps(LinkedList<Lap> laps) {
		this.laps = laps;
	}
	
	public void addLap(Lap lap) {
		laps.add(lap);
	}
}
