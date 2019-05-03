package classes.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import classes.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import tp.logging.SimpleLoggingUtil;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SettingsView implements Initializable {

	@FXML
	private Label settingsHeaderLabel;
	@FXML
	private GridPane currentView;
	@FXML 
	private GridPane root;
	@FXML 
	private Pane buttonContainer;
	
	// Das MainView wird hier benötigt, damit es 
	// ggf. geupdatet werden kann (z.B. wenn keine Chips
	// vorhanden sind, werden die Wettkampfbuttons gesperrt usw.)
	@FXML private MainView mainView;
	
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	
	// FÜR DIE NAVIGATION AUF DER LINKEN SEITE
	public void chipsBtnClick(ActionEvent e) {
		showPartial("Chips verwalten", "/templates/settings/settingsPartialEdit.fxml");	
		((Button)e.getSource()).getStyleClass().add("pressed");
	}
	
	public void settingsBtnClick(ActionEvent e) {
		showPartial("Einstellungen", "/templates/settings/settingsViewPartialCompetition.fxml");
		((Button)e.getSource()).getStyleClass().add("pressed");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showPartial("Chips verwalten", "/templates/settings/settingsPartialEdit.fxml");
		buttonContainer.getChildren().get(0).getStyleClass().add("pressed");
	}
	
	private void showPartial(String title, String resource) {
		buttonContainer.getChildren().forEach(btn -> {
			btn.getStyleClass().remove("pressed");
		});
		
		settingsHeaderLabel.setText(title);
		
		// Die aktuell angezeigten Einstellungen nicht mehr anzeigen,
		// um eine Überlagerung der GridPanes zu verhindern
		root.getChildren().remove(currentView);

		
		try {
			currentView = (GridPane) FXMLLoader.load(getClass().getResource(resource));
			// Das GridPane an die richtige Stelle setzen
			GridPane.setColumnIndex(currentView, 1);
			GridPane.setRowIndex(currentView, 1);
			// Das Partial anhängen
			root.getChildren().add(currentView);

		} catch (Exception ex) {
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(ex);
		}
	}
}
