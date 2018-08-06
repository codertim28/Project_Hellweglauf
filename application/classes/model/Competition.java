package classes.model;

import java.util.LinkedList;

import classes.CompetitionViewRowData;

public class Competition {
	
	// "name"-Attribut wird momentan nicht verwendet
	private String name;
	// Wenn Wettkampf auf Distanz
	private int lapLength; // in Metern
	private double lapCount;  // Anzahl der Runden
	// Wenn Wettkampf auf Zeit
	private int time; // in Sekunden
	
	// Die Daten (gelistet nach Runden; wie in der 
	// ursprünglichen Software
	// TODO: ObservableList draus machen und dann als
	// Content der DataTable im CompetitionView setzen
	private LinkedList<CompetitionViewRowData> data = new LinkedList();
	
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
	
	public double getLapCount() {
		return lapCount;
	}
	
	public void setLapCount(double lapCount) {
		this.lapCount = lapCount;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public LinkedList<CompetitionViewRowData> getData() {
		return data;
	}

	public void setData(LinkedList<CompetitionViewRowData> data) {
		this.data = data;
	}
}
