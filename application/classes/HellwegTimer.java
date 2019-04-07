package classes;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class HellwegTimer extends Thread {

	private int seconds;
	private Runnable stopCompetitionCallback;
	// Kommt als Referenz aus dem TimeCompetitionView
	private Label timeLabel;
	private ProgressBar timeProgressBar;
	private double onePercent;

	public HellwegTimer(int seconds, Label timeLabel, ProgressBar timeProgressBar, Runnable stopCompetitionCallback) {
		this.setPriority(Thread.MIN_PRIORITY);
		setSeconds(seconds);
		this.timeLabel = timeLabel;
		this.timeProgressBar = timeProgressBar;
		this.stopCompetitionCallback = stopCompetitionCallback;
		
		// Vor ab die Zeit anzeigen
		showTime();
	}
	
	public void startTimer() {   
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
	
	private void showTime() {
		// entsprechende UI Komponenten updaten
    	timeLabel.setText(String.format(
			"%d:%02d:%02d", 
			(seconds / 3600), 
			(seconds % 3600) / 60, 
			(seconds % 60)
		));
	}
	
	@Override
	public void run() {
		while (!this.isInterrupted() && seconds > 0) {
            setSeconds(seconds - 1);      
            // UI updaten
            Platform.runLater(() -> {
                // entsprechende UI Komponenten updaten
            	showTime();
            	timeProgressBar.setProgress(timeProgressBar.getProgress() + onePercent);      
            });
            // Schlafen / Warten
            try {
				sleep(1000);
			} catch (InterruptedException e) {
				// Wenn der Thread schläft, soll der Interrupt
				// trotzdem bearbeitet werden.
				this.interrupt();
			}
		}
		// Wenn die Zeit abgelaufen ist, muss dennoch überprüft 
		// werden, da ein Interrupt auch hinter die Schleife führt
		if(seconds == 0) {
			stopCompetitionCallback.run();
		}
		
	}
}