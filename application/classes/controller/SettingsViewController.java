package classes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SettingsViewController {

	@FXML
	private Label settingsHeaderLabel;

	// FÜR DIE NAVIGATION AUF DER LINKEN SEITE
	public void chipsBtnClick(ActionEvent e) {
		GridPane root = (GridPane) ((Button) e.getSource()).getScene().getRoot();
		settingsHeaderLabel.setText("Chips verwalten");

		try {
			GridPane gp = (GridPane) FXMLLoader.load(getClass().getResource("/templates/settings/settingsPartialEdit.fxml"));
			// Das GridPane an die richtige Stelle setzen
			GridPane.setColumnIndex(gp, 1);
			GridPane.setRowIndex(gp, 1);
			// Das Partial anhängen
			root.getChildren().add(gp);

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
