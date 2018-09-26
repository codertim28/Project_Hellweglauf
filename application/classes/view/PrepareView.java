package classes.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.xml.internal.ws.util.StringUtils;

import classes.controller.ChipsController;
import classes.model.Competition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrepareView implements Initializable {
	
	@FXML private TextField timeField, lapLengthField, lapCountField;

	@FXML private Button readyBtn;
	@FXML private Button yesBtn, noBtn;
	
	@FXML private TextField chipIdField;
	@FXML private Label studentNameLabel;
	
	private ChipsController chipsController;
	private Competition competition;
	
	public PrepareView(ChipsController chipsController, Competition comp) {
		setChipsController(chipsController);
		setCompetition(comp);
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
