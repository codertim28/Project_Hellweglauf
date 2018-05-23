package classes.controller.view;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectCompetitionViewController {

	@FXML private VBox root;
	
	private MainViewController mainViewController;
	
	// Click-Events
	public void timePaneClick(Event event) throws IOException {
		Stage stage = (Stage)root.getScene().getWindow();
		
		// Den Tab erstellen und hinzufügen
		Tab tab = new Tab("Wettkampf");
		tab.setContent(FXMLLoader.load(getClass().getResource("/templates/competition/competitionView.fxml")));
		mainViewController.getTabPane().getTabs().add(tab);
		
		// TODO: CompetitionView Controller hinzufügen, um einen Wettkampf
		// zu kontrollieren. Diesem einen Wettkampf mitgeben. So wird entschieden,
		// welcher Wettkampf ausgeführt wird
		
		// zuletzt das Modal (dieses Fenster) schließen
		stage.close();
	}
	
	// Setzt den MainViewController, damit diese Klasse
	// bei einem Klick, einen Tab setzen kann.
	void setMainViewController(MainViewController mvc) {
		mainViewController = mvc;
	}
}
