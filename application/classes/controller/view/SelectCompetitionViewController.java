package classes.controller.view;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectCompetitionViewController {

	@FXML private VBox root;
	
	private MainViewController mainViewController;
	
	// Click-Events
	public void timePaneClick(Event event) throws IOException {
		// TODO: IOException in dieser Methode behandeln
		
		// Den Tab erstellen und hinzufügen
		TimeCompetitionViewController tcvc = new TimeCompetitionViewController();
		if(tcvc.checkRequirements()) {
			// wenn die Voraussetzungen geklärt sind, den View einbinden, sonst nicht
			mainViewController.addTab(this.createTab("Wettkampf (Zeit)", "/templates/competition/competitionView.fxml", tcvc));
		}
			
		// zuletzt das Modal (dieses Fenster) schließen
		this.close();
	}
	
	public void distancePaneClick(Event event) throws IOException {
		// TODO: IOException in dieser Methode behandeln
		
		// Den Tab erstellen und hinzufügen
		mainViewController.addTab(this.createTab("Wettkampf (Distanz)", "/templates/competition/competitionView.fxml", null));
		
		// TODO: CompetitionView Controller (Distanz) hinzufügen, um einen Wettkampf
		// zu kontrollieren. Diesem einen Wettkampf mitgeben. So wird entschieden,
		// welcher Wettkampf ausgeführt wird
		
		// zuletzt das Modal (dieses Fenster) schließen
		this.close();
	}
	
	private Tab createTab(String title, String resource, CompetitionViewController controller) throws IOException {
		Tab tab = new Tab(title);
		FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
		loader.setController(controller);
		tab.setContent(loader.load());
		return tab;
	}
	
	private void close() {
		Stage stage = (Stage)root.getScene().getWindow();
		stage.close();
	}
	
	// Setzt den MainViewController, damit diese Klasse
	// bei einem Klick, einen Tab setzen kann.
	void setMainViewController(MainViewController mvc) {
		mainViewController = mvc;
	}
}
