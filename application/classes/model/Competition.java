package classes.model;

import java.util.LinkedList;

import classes.CompetitionViewRowData;
import classes.HellwegTimer;
import classes.controller.ChipsController;
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
	private int type; // 0: Auf Zeit, 1: Auf Distanz
	
	// Die Daten (gelistet nach Runden; wie in der 
	// ursprünglichen Software)
	private ObservableList<CompetitionViewRowData> data;
	
	// Muss hier (leider) liegen, denn der Timer muss zerstört werden,
	// wenn ein Tab geschlossen wird (anders ist der Thread nicht durch
	// den MainView erreichbar...)
	private HellwegTimer timer;
	
	public Competition() {
		setState(CompetitionState.PREPARE);
		setType(0); // Standard: Auf Zeit
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		// nur 0 und 1 erlauben.
		if(type >= 0 && type < 2) {
			this.type = type;
		}
	}

	public HellwegTimer getTimer() {
		return timer;
	}

	public void setTimer(HellwegTimer timer) {
		this.timer = timer;
	}
}
