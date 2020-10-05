package prohell.prohell.classes.model;

import java.util.LinkedList;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import prohell.prohell.classes.io.TextToLap;


public class Chip {
	
	/**
	 * Gibt an, wo mit dem "Rundenzählen" begonnen wird.
	 * -2, weil -1 die "Registrierungsrunde" ist.
	 */
	public final static int LAPCOUNT_START = -2;
	
	@CsvBindByName
	private String id;
	private StringProperty idProp;
	@CsvBindByName
	private String studentName;
	private StringProperty studentNameProp;
	@CsvBindAndSplitByName(elementType=Lap.class, splitOn = "|", converter=TextToLap.class)
	private LinkedList<Lap> laps;
	@CsvBindByName
	private ChipState state;
	
	/**
	 * Wird von "HellwegBufferedReader" benutzt.
	 * Sollte sonst nicht (!) verwendet werden.
	 */
	public Chip() {
		this("SG-1", "Daniel Jackson");
	}
	
	public Chip(String id, String studentName) {
		this.idProp = new SimpleStringProperty(this, "id");
		this.studentNameProp = new SimpleStringProperty(this, "studentName");
		
		setId(id);
		setStudentName(studentName);
		setLaps(new LinkedList<Lap>());
		setState(ChipState.OK);
	}
	
	// PROPERTIES
	public StringProperty idProperty() {
		return idProp;
	}
	
	public StringProperty studentNameProperty() {
		return studentNameProp;
	}
	
	// GETTER AND SETTER
	public String getId() {
		return idProperty().get();
	}
	
	public void setId(String id) {
		this.id = id;
		idProperty().set(id);
	}
	
	public String getStudentName() {
		return studentNameProperty().get();
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
		studentNameProperty().set(studentName);
	}
	
	public LinkedList<Lap> getLaps() {
		return laps;
	}

	public void setLaps(LinkedList<Lap> laps) {
		this.laps = laps;
	}

	public ChipState getState() {
		return state;
	}

	public void setState(ChipState state) {
		this.state = state;
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
