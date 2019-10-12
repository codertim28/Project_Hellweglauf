package prohell.prohell.classes.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser.ExtensionFilter;
import prohell.prohell.classes.io.IOFacade;

public class SettingsPartialImport implements Initializable {
	
	@FXML private Label fileNameLabel;
	
	private File selectedFile;
	
	private String NO_FILE_SELECTED = "Keine Datei ausgewählt!";
	
	@Override
	public void initialize(URL url, ResourceBundle rBundle) {
		fileNameLabel.setText(NO_FILE_SELECTED);
	}

	@FXML
	private void onSelectFileBtnClick(ActionEvent ev) {
		File selectedFile = IOFacade.chooseFile("CSV-Datei wählen", new ExtensionFilter("CSV-Dateien", "*.csv"));
		this.selectedFile = selectedFile;
		
		if(selectedFile != null) {
			fileNameLabel.setText(selectedFile.getAbsolutePath());
		}
		else {
			fileNameLabel.setText(NO_FILE_SELECTED);
		}
	}
}
