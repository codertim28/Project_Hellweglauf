package classes.model;

import java.util.LinkedList;

import classes.CompetitionViewRowData;
import classes.controller.ChipsController;
import classes.repository.CompetitionRepository;
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
	
	// Der Wettkampf hält einen ChipsController, indem alle
	// Chips gespeichert / verwaltet werden, die an diesem Wettkampf 
	// teilnehmen.
	private ChipsController chipsController;
	
	// Die Daten (gelistet nach Runden; wie in der 
	// ursprünglichen Software)
	private ObservableList<CompetitionViewRowData> data;
	
	public Competition() {
		this(new ChipsController());
	}
	
	public Competition(ChipsController cc) {
		setState(CompetitionState.PREPARE);
		setType(0); // Standard: Auf Zeit
		setData(FXCollections.observableList(new LinkedList<CompetitionViewRowData>()));
		setChipsController(cc);
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

	public ChipsController getChipsController() {
		return chipsController;
	}

	public void setChipsController(ChipsController chipsController) {
		this.chipsController = chipsController;
	}
}
