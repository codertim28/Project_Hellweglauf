package classes.model;

import java.util.LinkedList;
import java.util.List;

import classes.CompetitionViewRowData;

public class Competition {
	
	private String name;
	// Wenn Wettkampf auf Distanz
	private int lapLength; // in Metern
	private int distance; // Gesamtdistanz in Metern
	// Wenn Wettkampf auf Zeit
	private int time; // in Sekunden
	
	// Die Daten (gelistet nach Runden; wie in der 
	// ursprünglichen Software
	// TODO: ObservableList draus machen und dann als
	// Content der DataTable im CompetitionView setzen
	private LinkedList<CompetitionViewRowData> data;
	
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
}
