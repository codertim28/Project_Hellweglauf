package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.controller.CompetitionController;
import classes.model.Chip;
import classes.model.Competition;
import classes.model.CompetitionState;
import classes.repository.CompetitionRepository;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class DistanceCompetitionView extends CompetitionView {
	
	// Anmerkung: Der Benutzer muss nicht angeben, wann die halbe Stunde stattfindet,
	// da es für das Programm keine Rolle spielt.

	@FXML private Label leadingStudentLabel;
	@FXML private ProgressBar leadingStudentProgressBar;
	private double onePercent;
	private double leadingLapCount;

	public DistanceCompetitionView() throws IOException {
		super(1);
	}
	
	public DistanceCompetitionView(CompetitionController competitionController) {
		super(competitionController);
	}
	
	// TODO: DRY Code erzeugen, indem diese Methode aufgesplittet wird, damit
	// diese besser überschrieben werden kann.
	@Override 
	protected void scanTextFieldOnKeyPressed(KeyEvent ke) {
		Competition comp = competitionController.getCompetition();
		if(ke.getCode() == KeyCode.ENTER) {
			try {
				Chip c = chipsController.getChipById(scanTextField.getText().trim());
				// Nur wenn de gelaufene Distanz kleiner als die Zieldistanz ist, darf gescant werden.
				if(c.getLapCount() <= comp.getLapCount()) {
					super.scanTextFieldOnKeyPressed(ke);
					
					// Den Führenden anzeigen und den Fortschritt anzeigen
					if(c.getLapCount() > leadingLapCount) {
						leadingStudentLabel.setText(c.getStudentName());
						leadingStudentProgressBar.setProgress(leadingStudentProgressBar.getProgress() + onePercent);
						
						leadingLapCount = c.getLapCount();
					}
				}
				if(c.getLapCount() >= comp.getLapCount()) {
					// In diesem Fall ist der Schüler im Ziel. (>= wegen halben Runden)
					log("Wettkampf beendet (id: " + c.getId() + ")");
				}
			}
			catch(NullPointerException npe) {
				// Kein Chip gefunden: loggen
				log("FEHLER (1) (id: " + scanTextField.getText().trim() +")");
			}
			scanTextField.setText("");
		}
	}

	@Override
	protected void startBtnClick(Event event) {
		Competition comp = competitionController.getCompetition();
		if(comp.getState() == CompetitionState.READY || comp.getState() == CompetitionState.PREPARE) {
			setStartRounds();
			scanTextField.setDisable(false);
			scanTextField.requestFocus();
			super.log("Wettkampf gestartet!");
			comp.setState(CompetitionState.RUNNING);
		}
	}
	
	@Override
	protected void updateUI() {

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		
		// Schritt für die Progressbar definieren.
		onePercent = 1.0 / competitionController.getCompetition().getLapCount();
		// Einen leadingLapCount setzen.
		leadingLapCount = 0;
		
	}

}
