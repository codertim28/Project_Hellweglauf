package prohell.prohell.classes.view;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser.ExtensionFilter;
import prohell.prohell.classes.io.IOFacade;

public class SettingsPartialImport implements Initializable {

	@FXML
	private Label fileNameLabel, statusLabel;
	@FXML
	private ListView<String> columnPreview;
	@FXML
	private WebView informationWebView;
	@FXML
	private Button importBtn;

	private File selectedFile;

	private String NO_FILE_SELECTED = "Keine Datei ausgew�hlt!";

	@Override
	public void initialize(URL url, ResourceBundle rBundle) {
		fileNameLabel.setText(NO_FILE_SELECTED);
		informationWebView.getEngine().load(getClass().getResource("/html/importText.html").toExternalForm());

		// wird erst aktiviert, wenn eine gültige Datei ausgew�hlt wurde
		importBtn.setDisable(true);
	}

	@FXML
	private void onSelectFileBtnClick(ActionEvent ev) {
		File selectedFile = IOFacade.chooseFile("CSV-Datei w�hlen", new ExtensionFilter("CSV-Dateien", "*.csv"));
		this.selectedFile = selectedFile;
		columnPreview.getItems().clear();

		if (selectedFile != null) {
			fileNameLabel.setText(selectedFile.getAbsolutePath());
			showColumns();
			checkFile();
		} else {
			fileNameLabel.setText(NO_FILE_SELECTED);
			statusLabel.setText("");
			importBtn.setDisable(true);
		}
	}

	@FXML
	private void onImportBtnClick(ActionEvent ev) {
		IOFacade.importChipsFromCSV(selectedFile);
	}

	private void showColumns() {
		final CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(true).withSeparator(';').build();

		try {
			CSVReader reader = new CSVReaderBuilder(new FileReader(selectedFile)).withCSVParser(parser).build();
			String[] tableHead = reader.readNext();
			reader.close();

			columnPreview.getItems().addAll(tableHead);
		} catch (IOException | CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkFile() {
		// Hier wird eine Exception geworfen, wenn nicht 
		// alle der erforderlichen Spalten vorhanden sind.
		try {
			columnPreview.getItems().stream().filter(c -> c.toLowerCase().equals("chip")).findFirst().orElseThrow(Exception::new);
			columnPreview.getItems().stream().filter(c -> c.toLowerCase().equals("nachname")).findFirst().orElseThrow(Exception::new);
			columnPreview.getItems().stream().filter(c -> c.toLowerCase().equals("vorname")).findFirst().orElseThrow(Exception::new);
		
			statusLabel.setText("Daten k�nnen importiert werden.");
			statusLabel.setStyle("-fx-text-fill: green;");
			importBtn.setDisable(false);
		} catch (Exception e) {
			statusLabel.setText("Datei entspricht nicht der erforderlichen Struktur!");
			statusLabel.setStyle("-fx-text-fill: red;");
			importBtn.setDisable(true);
		}
	}
}
