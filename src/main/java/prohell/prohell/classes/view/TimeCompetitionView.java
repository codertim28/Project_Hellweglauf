package prohell.prohell.classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import prohell.prohell.classes.HellwegTimer;
import prohell.prohell.classes.controller.CompetitionController;
import prohell.prohell.classes.model.Competition;
import prohell.prohell.classes.model.CompetitionState;

public class TimeCompetitionView extends CompetitionView {
	
	@FXML private Label timeLabel;
	@FXML private ProgressBar timeProgressBar;
	
	private Competition comp;
	
	public TimeCompetitionView() throws IOException {
		super(0);
		comp = super.competitionController.getCompetition();
	}
	
	public TimeCompetitionView(CompetitionController compCon) {
		super(compCon);	
		comp = super.competitionController.getCompetition();
	}

	
	@FXML
	protected void startBtnClick(Event event) {
		comp.setState(CompetitionState.READY); // NUR ZUM TEST
		if(comp.getState() == CompetitionState.READY) {
			setStartRounds();
			comp.getTimer().startTimer();
			scanTextField.setDisable(false);
			scanTextField.requestFocus();
			super.log("Wettkampf gestartet!");
			comp.setState(CompetitionState.RUNNING);
			startBtn.setDisable(true);
		}
	}
	
	protected void updateUI() {
		// Den Timer-Thread erstellen.
		comp.setTimer(new HellwegTimer(comp.getTime(), timeLabel, 
				timeProgressBar, () -> stopCompetition()));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		updateUI();
	}
	
}
