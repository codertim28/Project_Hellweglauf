package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.Data;
import classes.model.Competition;
import classes.repository.CompetitionRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tp.dialog.StandardAlert;
import tp.dialog.StandardMessageType;

public class SettingsPartialCompetition implements Initializable {
	
	@FXML private TextField lapLengthField;
	@FXML private TextField lapCountField;
	@FXML private TextField timeField;
	
	@FXML private Label errorLabel;
	
	private CompetitionRepository repository;
	
	@FXML
	private void saveBtnClick() {
		// errorLabel vorher löschen, um den Benutzer nicht zu verwirren.
		errorLabel.setText(new String());
		
		// Alles auslesen und speichern oder einen Fehler anzeigen
		Competition comp = new Competition();
		comp.getChipsController().load(); // laden, damit die Chips nicht überschrieben werden
		comp.setName("Wettkampf"); // Attribut wird derzeit nicht verwendet
		// TODO: Parse-Fehler abfangen während der Benutzer die Daten einträgt
		try {
			comp.setLapLength(Integer.parseInt(lapLengthField.getText().replace(',', '.')));
			comp.setLapCount(Double.parseDouble(lapCountField.getText().replace(',', '.')));
			comp.setTime(Integer.parseInt(timeField.getText()) * 60);
		} catch(NumberFormatException nfe) {
			errorLabel.setText("Bitte beachte die korrekte Formatierung (Rundenlänge, Rundenanzahl & Zeit).");
			// Aussteigen, damit fehlerhafte Werte nicht geschrieben werden.
			return;
		}
		
		boolean success = repository.write(comp);
		if(success) {
			// Allgemeine Erfolgsnachricht
			StandardAlert standardAlert = new StandardAlert(StandardMessageType.SUCCESS);
			standardAlert.showAndWait();
		} else {
			// Allgemeine Fehlernachricht
			StandardAlert standardAlert = new StandardAlert(StandardMessageType.ERROR);
			standardAlert.showAndWait();
		}
		
	}
	
	@FXML
	private void resetBtnClick() {
		// errorLabel vorher löschen, um den Benutzer nicht zu verwirren.
		errorLabel.setText(new String());
		
		// Die Standardwerte wiederherstellen
		Competition defaultComp = new Competition();
		defaultComp.setName("Wettkampf");
		defaultComp.setLapCount(2.5);
		defaultComp.setLapLength(400);
		defaultComp.setTime(30 * 60);
		
		boolean success = repository.write(defaultComp);
		if(success) {
			// Allgemeine Erfolgsnachricht
			new StandardAlert(StandardMessageType.SUCCESS).showAndWait();
		} else {
			// Allgemeine Fehlernachricht
			new StandardAlert(StandardMessageType.ERROR).showAndWait();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		repository = new CompetitionRepository(Data.DIR + "/" + Data.BASIC_DIR + "/" + Data.COMPETITION_FILE);
		
		try {
			// Das Wettkampfobjekt aus dem basic_dir lesen
			Competition comp = repository.read();
			// Die Attribute in Textfelder schreiben, damit diese vom Benutzer
			// bearbeitet werden können.
			lapLengthField.setText("" + comp.getLapLength());
			lapCountField.setText("" + comp.getLapCount());
			timeField.setText("" + (comp.getTime() / 60));
		} catch (IOException e) {
			// Allgemeine Fehlernachricht
			new StandardAlert(StandardMessageType.ERROR).showAndWait();
		}
	}
}
