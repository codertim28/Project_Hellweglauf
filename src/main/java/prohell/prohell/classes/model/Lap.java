package prohell.prohell.classes.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class Lap {
	
	@NonNull
	private LocalTime timestamp;
	private int number;
	
	public String getTimestampAsString() {
		return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
}
