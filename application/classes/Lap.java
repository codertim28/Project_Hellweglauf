package classes;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Lap {
	
	private LocalTime timestamp;
	// Diese Property wird in dem TableView im
	// CompetitonView angezeigt. Sie ist synchronisiert
	// mit timestamp.
	private StringProperty timestampProp;
	
	private IntegerProperty number;
	
	public Lap(LocalTime timestamp, int number) {
		this.timestampProp = new SimpleStringProperty(this, "timestampProp");
		this.number = new SimpleIntegerProperty(this, "number");
		setTimestamp(timestamp);
		setNumber(number);
	}
	
	// PROPERTIES
	public IntegerProperty numberProperty() {
		return number;
	}
	
	public StringProperty timestampProperty() {
		return timestampProp;
	}
	
	// GETTER AND SETTER
	public int getNumber() {
		return numberProperty().get();
	}

	private void setNumber(int number) {
		numberProperty().set(number);
	}
	
	public LocalTime getTimestamp() {
		return timestamp;
	}

	private void setTimestamp(LocalTime timestamp) {
		this.timestamp = timestamp;
		timestampProperty().set(timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	}
}
