package classes.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindowController {

	// Click-Events
	public void competitionPaneClick(Event e) {
		System.out.println("competionPaneClick");
	}

	public void settingsBtnClick(ActionEvent e) throws java.io.IOException {

		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(((Button) e.getSource()).getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);

		Parent parent = FXMLLoader.load(getClass().getResource("/templates/settings/settingsView.fxml"));
		Scene scene = new Scene(parent, 500, 400);
		scene.getStylesheets().add(getClass().getResource("/templates/application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
}
