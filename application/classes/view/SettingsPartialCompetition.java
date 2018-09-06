package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.Data;
import classes.model.Competition;
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
	
	@FXML
	private void saveBtnClick() {
		// errorLabel vorher löschen, um den Benutzer nicht zu verwirren.
		errorLabel.setText(new String());
		
		// Alles auslesen und speichern oder einen Fehler anzeigen
		Competition comp = new Competition();
		comp.setName("Wettkampf"); // Attribut wird derzeit nicht verwendet
		// TODO: Parse-Fehler abfangen während der Benutzer die Daten einträgt
		try {
			comp.setLapLength(Integer.parseInt(lapLengthField.getText().replace(',', '.')));
			comp.setLapCount(Double.parseDouble(lapCountField.getText().replace(',', '.')));
			comp.setTime(Integer.parseInt(timeField.getText().replace(',', '.')) * 60);
		} catch(NumberFormatException nfe) {
			errorLabel.setText("Bitte beachte die korrekte Formatierung (Rundenlänge, Rundenanzahl & Zeit).");
			// Aussteigen, damit fehlerhafte Werte nicht geschrieben werden.
			return;
		}
		
		try {
			Data.writeComp(Data.BASIC_DIR, comp);
			// Allgemeine Erfolgsnachricht
			StandardAlert standardAlert = new StandardAlert(StandardMessageType.SUCCESS);
			standardAlert.showAndWait();
		} catch (IOException e) {
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
		defaultComp.setTime(30);
		
		try {
			Data.writeComp(Data.BASIC_DIR, defaultComp);
			// Allgemeine Erfolgsnachricht
			new StandardAlert(StandardMessageType.SUCCESS).showAndWait();
		} catch (IOException e) {
			// Allgemeine Fehlernachricht
			new StandardAlert(StandardMessageType.ERROR).showAndWait();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			// Das Wettkampfobjekt aus dem basic_dir lesen und casten
			Competition comp = Data.readComp(Data.BASIC_DIR);
			// Die Attribute in Textfelder schreiben, damit diese vom Benutzer
			// bearbeitet werden können.
			lapLengthField.setText("" + comp.getLapLength());
			lapCountField.setText("" + comp.getLapCount());
			timeField.setText("" + comp.getTime());
		} catch (IOException e) {
			// Allgemeine Fehlernachricht
			new StandardAlert(StandardMessageType.ERROR).showAndWait();
		}
	}
}
