package classes.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController {

	@FXML private TabPane tabPane;
	
	// Click-Events
	public void competitionPaneClick(Event e) throws IOException {
		// Das Auswahlfenster erstellen
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(((Parent) e.getSource()).getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);

		// Das Template laden und dem Controller diesen Controller
		// übergeben, damit ein neuer Tab hinzugefügt werden kann.
		// (schön ist das nicht, aber selten)
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/competition/selectCompetitionView.fxml"));
		Parent parent = (Parent)loader.load();
		SelectCompetitionViewController scvc = (SelectCompetitionViewController)loader.getController();
		scvc.setMainViewController(this); 
		
		Scene scene = new Scene(parent, 300, 400);
		//scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void settingsBtnClick(ActionEvent e) throws IOException {

		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(((Button) e.getSource()).getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);

		Parent parent = FXMLLoader.load(getClass().getResource("/templates/settings/settingsView.fxml"));
		Scene scene = new Scene(parent, 500, 400);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
	
	// GETTER UND SETTER
	// So muss es keinen Getter geben, der das 
	// ganze TabPane nach außen gibt.
	public void addTab(Tab tab) {
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
	}
}
