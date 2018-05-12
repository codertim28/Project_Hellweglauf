package classes;

import java.util.Date;

public class Round {
	
	private Date timestamp;
	
	public Round(Date timestamp) {
		setTimestamp(timestamp);
	}
	
	// GETTER AND SETTER
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
