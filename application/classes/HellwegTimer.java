package classes;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class HellwegTimer extends Thread {

	private int seconds;
	
	// Kommt als Referenz aus dem TimeCompetitionViewController
	private Label timeLabel;

	public HellwegTimer(Label timeLabel) {
		this.setPriority(Thread.MIN_PRIORITY);
		this.timeLabel = timeLabel;
	}
	
	public void startTimer(int seconds) {
	    setSeconds(seconds);    
	    this.start();
	}
	
	public void stopTimer() {
		this.interrupt();
	}
	
	public void setSeconds(int seconds) {
	    this.seconds = seconds;
	}
	
	public long getSeconds() {
	    return seconds;
	}
	
	@Override
	public void run() {
		try {
			while (!this.isInterrupted()) {
	            setSeconds(seconds - 1);      
	            // UI updaten
	            Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                    // entsprechende UI Komponente updaten
	                	timeLabel.setText("" + seconds);
	                }
	            });
	            // Schlafen / Warten
	            sleep(1000);
			}
		} catch(Exception e) {
			// Das soll gar nicht behandelt werden..
		}
		
	}
}