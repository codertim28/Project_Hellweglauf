package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.HellwegTimer;
import classes.model.Competition;
import classes.model.CompetitionState;
import classes.repository.CompetitionRepository;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TimeCompetitionView extends CompetitionView {
	
	@FXML private Label timeLabel;
	@FXML private ProgressBar timeProgressBar;
	private HellwegTimer hellwegTimer;
	
	public TimeCompetitionView() throws IOException {
		super(0);
		// Im Konstruktor sind die JavaFx-Komponenten scheinbar
		// noch nicht vorhanden.
		// hellwegTimer = new HellwegTimer(timeLabel);
		// Somit muss diese Zeile (s.o.) in die initialize-Methode.
	}
	
	public TimeCompetitionView(Competition comp, CompetitionRepository compRepo) {
		super(comp, compRepo);
	}

	@FXML
	protected void startBtnClick(Event event) {	
		comp.setState(CompetitionState.READY); // NUR ZUM TEST
		if(comp.getState() == CompetitionState.READY) {
			setStartRounds();
			hellwegTimer.startTimer();
			scanTextField.setDisable(false);
			scanTextField.requestFocus();
			super.log("Wettkampf gestartet!");
			comp.setState(CompetitionState.RUNNING);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		
		// Den Timer-Thread erstellen.
		hellwegTimer = new HellwegTimer(comp.getTime(), timeLabel, timeProgressBar, new Runnable() {
			@Override
			public void run() {
				stopCompetition();
			}
		});
	}
	
}
