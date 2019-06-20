package prohell.prohell.classes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import prohell.prohell.classes.model.Chip;
import prohell.prohell.classes.model.Lap;

/**
 * Diese Klasse ist das DataModel f�r das TableView
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
     * Erstellt eine DataRow auch wenn kein vollst�ndige(r) Runde oder Chip
     * zur Verf�gung steht (z.B. beim Laden eines Wettkampfes). 
     * @param id ID des (unvollst�ndigen) Chips
     * @param studentName Sch�ler (Name) des (unvollst�ndigen) Chips
     * @param timestamp Zeitstempel der Runde f�r die DataRow (Format: HH:mm:ss)
     * @param lapNumber Nummer der Runde f�r die DataRow
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
