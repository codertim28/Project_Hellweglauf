package classes;

import java.time.Duration;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class HellwegTimer extends Thread {

	private int seconds;
	private Runnable stopCompetitionCallback;
	// Kommt als Referenz aus dem TimeCompetitionViewController
	private Label timeLabel;
	private ProgressBar timeProgressBar;
	private double onePercent;

	public HellwegTimer(Label timeLabel, ProgressBar timeProgressBar, Runnable stopCompetitionCallback) {
		this.setPriority(Thread.MIN_PRIORITY);
		this.timeLabel = timeLabel;
		this.timeProgressBar = timeProgressBar;
		this.stopCompetitionCallback = stopCompetitionCallback;
	}
	
	public void startTimer(int seconds) {
	    setSeconds(seconds);    
	    this.onePercent = 1.0 / (double)seconds;
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
			while (!this.isInterrupted() && seconds > 0) {
	            setSeconds(seconds - 1);      
	            // UI updaten
	            Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                    // entsprechende UI Komponenten updaten
	                	timeLabel.setText(String.format(
                			"%d:%02d:%02d", 
                			(seconds / 3600), 
        					(seconds % 3600) / 60, 
        					(seconds % 60)
    					));
	                	timeProgressBar.setProgress(timeProgressBar.getProgress() + onePercent);
	                }
	            });
	            // Schlafen / Warten
	            sleep(1000);
			}
			// Wenn die Zeit abgelaufen ist, muss dennoch überprüft 
			// werden, da ein Interrupt auch hinter die Schleife führt
			if(seconds == 0) {
				stopCompetitionCallback.run();
			}
		} catch(Exception e) {
			// Das soll gar nicht behandelt werden..
		}
		
	}
}