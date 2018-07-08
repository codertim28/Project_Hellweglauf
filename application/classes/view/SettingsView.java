package classes.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SettingsView {

	@FXML
	private Label settingsHeaderLabel;
	@FXML
	private GridPane currentView;

	
	// FÜR DIE NAVIGATION AUF DER LINKEN SEITE
	public void chipsBtnClick(ActionEvent e) {
		GridPane root = (GridPane) ((Button) e.getSource()).getScene().getRoot();
		settingsHeaderLabel.setText("Chips verwalten");
		
		// Die aktuell angezeigten Einstellungen nicht mehr anzeigen,
		// um eine Überlagerung der GridPanes zu verhindern
		root.getChildren().remove(currentView);

		try {
			currentView = (GridPane) FXMLLoader.load(getClass().getResource("/templates/settings/settingsPartialEdit.fxml"));
			// Das GridPane an die richtige Stelle setzen
			GridPane.setColumnIndex(currentView, 1);
			GridPane.setRowIndex(currentView, 1);
			// Das Partial anhängen
			root.getChildren().add(currentView);

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public void settingsBtnClick(ActionEvent e) {
		GridPane root = (GridPane) ((Button) e.getSource()).getScene().getRoot();
		settingsHeaderLabel.setText("Einstellungen");
		
		// Die aktuell angezeigten Einstellungen nicht mehr anzeigen,
		// um eine Überlagerung der GridPanes zu verhindern
		root.getChildren().remove(currentView);

		
		try {
			currentView = (GridPane) FXMLLoader.load(getClass().getResource("/templates/settings/settingsViewPartialCompetition.fxml"));
			// Das GridPane an die richtige Stelle setzen
			GridPane.setColumnIndex(currentView, 1);
			GridPane.setRowIndex(currentView, 1);
			// Das Partial anhängen
			root.getChildren().add(currentView);

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
