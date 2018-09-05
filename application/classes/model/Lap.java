package classes.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Lap {
	
	private LocalTime timestamp;
	private int number;
	
	public Lap(LocalTime timestamp, int number) {
		setTimestamp(timestamp);
		setNumber(number);
	}
	
	
	// GETTER AND SETTER
	public int getNumber() {
		return number;
	}

	private void setNumber(int number) {
		this.number = number;
	}
	
	public String getTimestampAsString() {
		return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
	
	public LocalTime getTimestamp() {
		return timestamp;
	}

	private void setTimestamp(LocalTime timestamp) {
		this.timestamp = timestamp;
	}
}
