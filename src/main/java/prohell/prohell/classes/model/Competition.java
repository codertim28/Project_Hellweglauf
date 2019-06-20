package prohell.prohell.classes.model;

import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import prohell.prohell.classes.CompetitionViewRowData;
import prohell.prohell.classes.HellwegTimer;

@Getter
@Setter
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
	
	public void setType(int type) {
		// nur 0 und 1 erlauben.
		if(type >= 0 && type < 2) {
			this.type = type;
		}
	}
}
