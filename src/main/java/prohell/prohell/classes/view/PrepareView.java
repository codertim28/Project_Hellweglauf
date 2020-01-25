package prohell.prohell.classes.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import prohell.prohell.classes.Constants;
import prohell.prohell.classes.controller.ChipsController;
import prohell.prohell.classes.model.Chip;
import prohell.prohell.classes.model.ChipState;
import prohell.prohell.classes.model.Competition;
import prohell.prohell.classes.model.CompetitionState;
import prohell.prohell.utils.dialog.StandardAlert;
import prohell.prohell.utils.dialog.StandardMessageType;
import prohell.prohell.utils.logging.SimpleLoggingUtil;

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
		if(scannedChip != null) {
			scannedChip.setState(ChipState.DNS);
		}
		
		clearChipIdField();
	}
	
	@FXML
	private void readyBtnClick() {
		// Aktualisierung des Wettkampf-Interfaces wird im entsprechenden
		// View durchgeführt...
		// Den Wettkampf als "vorbereitet" vermerken und schlie�en 
		try {
			competition.setTime(Integer.parseInt(timeField.getText()) * 60);
			competition.setLapLength(Integer.parseInt(lapLengthField.getText()));
			competition.setLapCount(Double.parseDouble(lapCountField.getText().replace(',','.')));
			// Wenn keine Exception aufgetreten ist, den Wettkampf als
			// vorbereitet markieren und das Fenster schließen
			competition.setState(CompetitionState.READY);
			readyBtn.getScene().getWindow().hide();
		}
		catch(Exception e) {
			new StandardAlert(StandardMessageType.ERROR).showAndWait();
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Alle Werte laden bzw. anzeigen
		timeField.setText("" + competition.getTime() / 60);
		lapLengthField.setText("" + competition.getLapLength());
		lapCountField.setText((competition.getLapCount() + "").replace(".",","));		
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
