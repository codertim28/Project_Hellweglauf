package classes.model;

import java.util.LinkedList;

import classes.CompetitionViewRowData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Competition {
	
	// "name"-Attribut wird momentan nicht verwendet
	private String name;
	// Wenn Wettkampf auf Distanz
	private int lapLength; // in Metern
	private double lapCount;  // Anzahl der Runden
	// Wenn Wettkampf auf Zeit
	private int time; // in Sekunden
	
	private CompetitionState state;
	
	// Die Daten (gelistet nach Runden; wie in der 
	// ursprünglichen Software)
	private ObservableList<CompetitionViewRowData> data;
	
	public Competition() {
		init();
	}
	
	public void init() {
		setState(CompetitionState.PREPARE);
		setData(FXCollections.observableList(new LinkedList<CompetitionViewRowData>()));
	}
	
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

	public ObservableList<CompetitionViewRowData> getData() {
		return data;
	}

	public void setData(ObservableList<CompetitionViewRowData> data) {
		this.data = data;
	}

	public CompetitionState getState() {
		return state;
	}

	public void setState(CompetitionState state) {
		this.state = state;
	}
}
