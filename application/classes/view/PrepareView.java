package classes.view;

import java.net.URL;
import java.util.ResourceBundle;

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
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
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
