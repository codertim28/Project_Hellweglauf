package classes.model;

import java.util.LinkedList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Chip {
	
	/**
	 * Gibt an, wo mit dem "Rundenzählen" begonnen wird.
	 * -2, weil -1 die "Registrierungsrunde" ist.
	 */
	public final static int LAPCOUNT_START = -2;
	
	private StringProperty id;
	private StringProperty studentName;
	private StringProperty form; // Die Klasse des Schülers
	private LinkedList<Lap> laps;

	/**
	 * Wird von "HellwegBufferedReader" benutzt.
	 * Sollte sonst nicht (!) verwendet werden.
	 */
	public Chip() {
		this("default1234", "Daniel Jackson", "SG-1");
	}
	
	public Chip(String id, String studentName, String form) {	
		this.id = new SimpleStringProperty(this, "id");
		this.studentName = new SimpleStringProperty(this, "studentName");
		this.form = new SimpleStringProperty(this, "form");
		
		setId(id);
		setStudentName(studentName);
		setForm(form);
		setLaps(new LinkedList<Lap>());
	}
	
	// TODO: Diesen Konstruktor entfernen! Ist nur vorhanden, damit der
	// BufferedReader keinen Fehler wirft.
	public Chip(String id, String studentName) {	
		this(id, studentName, "None");
	}
	
	// PROPERTIES
	public StringProperty idProperty() {
		return id;
	}
	
	public StringProperty studentNameProperty() {
		return studentName;
	}
	
	public StringProperty formProperty() {
		return form;
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
	
	public String getForm() {
		return formProperty().get();
	}
	
	public void setForm(String form) {
		formProperty().set(form);
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
