package classes;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Chip {
	
	private StringProperty id;
	private StringProperty studentName;
	private List<Round> rounds;
	
	public Chip(String id, String studentName) {	
		this.id = new SimpleStringProperty(this, "id");
		this.studentName = new SimpleStringProperty(this, "studentName");
		
		setId(id);
		setStudentName(studentName);
		
		rounds = new ArrayList<Round>();
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
	
	public void getRoundCount() {
		rounds.size();
	}
	
	
}
