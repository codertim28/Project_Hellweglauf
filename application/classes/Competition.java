package classes;

import java.util.LinkedList;
import java.util.List;

public class Competition {
	
	private String name;
	// Wenn Wettkampf auf Distanz
	private int roundLength; // in Metern
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
	
	public int getRoundLength() {
		return roundLength;
	}
	
	public void setRoundLength(int roundLength) {
		this.roundLength = roundLength;
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

	public LinkedList<Lap> getRounds() {
		return laps;
	}

	public void setRounds(LinkedList<Lap> laps) {
		this.laps = laps;
	}
	
	public void addRound(Lap lap) {
		laps.add(lap);
	}
}
