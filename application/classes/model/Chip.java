package classes.model;

import java.util.LinkedList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// TODO: Auch die Klasse speichern (als Attribut)? So könnte man wählen,
// welche Klasse an einem Wettkampf teilnehmen. Momentan nehmen alle Chips
// automatisch an einem WK teil.

public class Chip {
	
	/**
	 * Gibt an, wo mit dem "Rundenzählen" begonnen wird.
	 * -2, weil -1 die "Registrierungsrunde" ist.
	 */
	public final static int LAPCOUNT_START = -2;
	
	private StringProperty id;
	private StringProperty studentName;
	private LinkedList<Lap> laps;

	/**
	 * Wird von "HellwegBufferedReader" benutzt.
	 * Sollte sonst nicht (!) verwendet werden.
	 */
	public Chip() {
		this("sg-1_default1234", "Daniel Jackson");
	}
	
	public Chip(String id, String studentName) {	
		this.id = new SimpleStringProperty(this, "id");
		this.studentName = new SimpleStringProperty(this, "studentName");
		
		setId(id);
		setStudentName(studentName);
		setLaps(new LinkedList<Lap>());
	}
	
	// PROPERTIES
	public StringProperty idProperty() {
		return id;
	}
	
	public StringProperty studentNameProperty() {
		return studentName;
	}
	
	// GETTER AND SETTER
	public String getId() {
		return idProperty().get();
	}
	
	public void setId(String id) {
		idProperty().set(id);
	}
	
	public String getStudentName() {
		return studentNameProperty().get();
	}
	
	public void setStudentName(String studentName) {
		studentNameProperty().set(studentName);
	}
	
	public LinkedList<Lap> getLaps() {
		return laps;
	}

	public void setLaps(LinkedList<Lap> laps) {
		this.laps = laps;
	}

	public int getLapCount() {
		// Hier kann nicht mit rounds.size()
		// gearbeitet werden, da jeder Chip
		// die Runde -1 bekommt, um für einen 
		// Wettkampf "registeriert zu werden.
		if(laps.size() == 0) {
			// Die erste Runde ist die "Registrierungsrunde"
			// Bei hinzufügen einer Runde wird +1 gerechnet.
			return LAPCOUNT_START;
		}
		return laps.getLast().getNumber();
	}
}
