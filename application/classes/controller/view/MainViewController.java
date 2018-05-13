package classes.controller.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController {

	@FXML private TabPane tabPane;
	
	// Click-Events
	public void competitionPaneClick(Event e) throws IOException {
		Tab tab = new Tab("Wettkampf");
		tab.setContent(FXMLLoader.load(getClass().getResource("/templates/competition/competitionView.fxml")));
		tabPane.getTabs().add(tab);
	}

	public void settingsBtnClick(ActionEvent e) throws IOException {

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
