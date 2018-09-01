package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.HellwegTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TimeCompetitionView extends CompetitionView {
	
	@FXML private Label timeLabel;
	@FXML private ProgressBar timeProgressBar;
	private HellwegTimer hellwegTimer;
	
	public TimeCompetitionView() throws IOException {
		super();
		// Im Konstruktor sind die JavaFx-Komponenten scheinbar
		// noch nicht vorhanden.
		// hellwegTimer = new HellwegTimer(timeLabel);
		// Somit muss diese Zeile (s.o.) in die initialize-Methode.
	}
	
	@FXML
	protected void startBtnClick(Event event) {
		if(!started) {
			setStartRounds();
			hellwegTimer = new HellwegTimer(timeLabel, timeProgressBar, new Runnable() {
				@Override
				public void run() {
					stopCompetition();
				}
			});
			hellwegTimer.startTimer(30);
			scanTextField.setDisable(false);
			scanTextField.requestFocus();
			super.log("Wettkampf gestartet!");
			started = true;
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		
		//hellwegTimer = new HellwegTimer(timeLabel, new Runnable() {
		//	@Override
		//	public void run() {
		//		stopCompetition();
		//	}
		//});
	}
	
}
