package classes.controller.view;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import classes.Chip;
import classes.CompetitionViewRowData;
import classes.HellwegTimer;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TimeCompetitionViewController extends CompetitionViewController {
	
	@FXML private Label timeLabel;
	@FXML private ProgressBar timeProgressBar;
	private HellwegTimer hellwegTimer;
	
	public TimeCompetitionViewController() {
		super();
		// Im Konstruktor sind die JavaFx-Komponenten scheinbar
		// noch nicht vorhanden.
		// hellwegTimer = new HellwegTimer(timeLabel);
		// Somit muss diese Zeile (s.o.) in die initialize-Methode.
	}
	
	@FXML
	protected void startBtnClick(Event event) {
		if(!started) {
			// TODO: Wettkampf starten
			hellwegTimer.startTimer(30);
			logTextArea.appendText("Wettkampf gestartet!\n");
			started = true;
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		
		hellwegTimer = new HellwegTimer(timeLabel);
	}
	
}
