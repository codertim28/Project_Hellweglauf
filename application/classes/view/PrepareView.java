package classes.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.xml.internal.ws.util.StringUtils;

import classes.controller.ChipsController;
import classes.model.Chip;
import classes.model.ChipState;
import classes.model.Competition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PrepareView implements Initializable {
	
	@FXML private TextField timeField, lapLengthField, lapCountField;

	@FXML private Button readyBtn;
	@FXML private Button yesBtn, noBtn;
	
	@FXML private TextField chipIdField;
	@FXML private Label studentNameLabel;
	
	private ChipsController chipsController;
	private Competition competition;

	private Chip scannedChip;
	
	public PrepareView(ChipsController chipsController, Competition comp) {
		setChipsController(chipsController);
		setCompetition(comp);
	}
	
	@FXML
	private void chipIdFieldOnKeyPressed(KeyEvent ke) {
		if(ke.getCode().equals(KeyCode.ENTER)) {
			scannedChip = chipsController.getChipById(chipIdField.getText().trim());
			
			if(scannedChip != null) {
				studentNameLabel.setText(scannedChip.getStudentName());
			}
			else {
				studentNameLabel.setText("Chip nicht vorhanden!");
			}
		}
	}
	
	private void clearChipIdField() {
		// Das Feld leeren, um einen erneuten Scan zu ermöglichen.
		chipIdField.setText(new String());

		scannedChip = null;
		// und erneut Fokus auf das Scanfeld legen.
		chipIdField.requestFocus();
	}
	
	@FXML 
	private void yesBtnClick() {
		clearChipIdField();
	}
	
	@FXML 
	private void noBtnClick() {
		// Anmerken, dass dieser Chip / Schüler nicht starten wird.
		scannedChip.setState(ChipState.DNS);
		
		clearChipIdField();
	}
	
	@FXML
	private void readyBtnClick() {
		// TODO: Die (eventuell) neuen Wettkampfparameter übernehmen
		// und die Einstellungen im CompetitionView aktualisieren bzw.
		// anzeigen...
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Alle Werte laden bzw. anzeigen
		timeField.setText("" + competition.getTime() / 60);
		lapLengthField.setText("" + competition.getLapLength());
		lapCountField.setText("" + competition.getLapCount());		
	}


	// GETTER UND SETTER
	public ChipsController getChipsController() {
		return chipsController;
	}


	private void setChipsController(ChipsController chipsController) {
		this.chipsController = chipsController;
	}


	public Competition getCompetition() {
		return competition;
	}


	private void setCompetition(Competition competition) {
		this.competition = competition;
	}
}
