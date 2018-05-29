package classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Chip {
	
	private StringProperty id;
	private StringProperty studentName;
	private LinkedList<Round> rounds;

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
		setRounds(new LinkedList<Round>());
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
	
	public LinkedList<Round> getRounds() {
		return rounds;
	}

	public void setRounds(LinkedList<Round> rounds) {
		this.rounds = rounds;
	}

	public void getRoundCount() {
		rounds.size();
	}
}
