package classes;

import classes.model.Chip;
import classes.model.Lap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Diese Klasse ist das DataModel für das TableView
 * in dem CompetitionView.
 *
 */
public class CompetitionViewRowData {

    private StringProperty chipId;
    private StringProperty studentName;
	private IntegerProperty number;
	private StringProperty timestamp;
    
	// Dieser Konstruktor erstellt eine DataRow mit Chip und 
	// einer beliebigen, gegebenen Runde.
    public CompetitionViewRowData(Chip chip, Lap lap) {
    	init();
    	chipId.set(chip.getId());
    	studentName.set(chip.getStudentName());
        number.set(lap.getNumber());
        timestamp.set(lap.getTimestampAsString());
    }
    
    // Dieser Konstruktor erstellt eine DataRow mit einem Chip und 
    // der letzten Runde dessen.
    public CompetitionViewRowData(Chip chip) {
        this(chip, chip.getLaps().getLast());
    }
    
    /**
     * Erstellt eine DataRow auch wenn kein vollständige(r) Runde oder Chip
     * zur Verfügung steht (z.B. beim Laden eines Wettkampfes). 
     * @param id ID des (unvollständigen) Chips
     * @param studentName Schüler (Name) des (unvollständigen) Chips
     * @param timestamp Zeitstempel der Runde für die DataRow (Format: HH:mm:ss)
     * @param lapNumber Nummer der Runde für die DataRow
     */
    public CompetitionViewRowData(String id, String studentName, String timestamp, int lapNumber) {
        init();
        this.chipId.set(id);
    	this.studentName.set(studentName);
    	this.number.set(lapNumber);
    	this.timestamp.set(timestamp);
    }
    
    private void init() {
    	this.chipId = new SimpleStringProperty();
    	this.studentName = new SimpleStringProperty();
        this.number = new SimpleIntegerProperty();
        this.timestamp = new SimpleStringProperty();
    }

    
    // PROPERTIES
    public StringProperty chipIdProperty() {
 		return chipId;
 	}
 	
 	public StringProperty studentNameProperty() {
 		return studentName;
 	}
    
 	public IntegerProperty lapNumberProperty() {
 		return number;
 	}
 	
 	public StringProperty timestampProperty() {
 		return timestamp;
 	}
 	
}
