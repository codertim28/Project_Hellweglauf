package prohell.prohell.classes.view;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import prohell.prohell.classes.io.IOFacade;
import prohell.prohell.utils.dialog.StandardAlert;
import prohell.prohell.utils.dialog.StandardMessageType;


public class SettingsPartialCompetition implements Initializable {
	
	@FXML private TextField lapLengthField;
	@FXML private TextField lapCountField;
	@FXML private TextField timeField;
	
	@FXML private Label errorLabel;
	
	private Properties properties;
	
	@FXML
	private void saveBtnClick() {
		// errorLabel vorher löschen, um den Benutzer nicht zu verwirren.
		errorLabel.setText(new String());
		
		try {
			int lapLength = Integer.parseInt(lapLengthField.getText().replace(',', '.'));
			double lapCount = Double.parseDouble(lapCountField.getText().replace(',', '.'));
			int time = Integer.parseInt(timeField.getText()) * 60;
			properties.setProperty("competition.lapLength", String.valueOf(lapLength));
			properties.setProperty("competition.lapCount", "" + lapCount);
			properties.setProperty("competition.time", String.valueOf(time));
		} catch(NumberFormatException nfe) {
			errorLabel.setText("Bitte beachte die korrekte Formatierung (Rundenlänge, Rundenanzahl & Zeit).");
			// Aussteigen, damit fehlerhafte Werte nicht geschrieben werden.
			return;
		}
		
		boolean success = IOFacade.storeProperties(properties);
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
		properties.setProperty("competition.lapLength", String.valueOf(400));
		properties.setProperty("competition.lapCount", "" + 2.5);
		properties.setProperty("competition.time", String.valueOf(30 * 60));
		
		boolean success = IOFacade.storeProperties(properties);
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
		
		properties = IOFacade.loadProperties();
		
		try {
			// Die Attribute in Textfelder schreiben, damit diese vom Benutzer
			// bearbeitet werden können.
			lapLengthField.setText(properties.getProperty("competition.lapLength", String.valueOf(400)));
			lapCountField.setText(properties.getProperty("competition.lapCount", "" + 2.5));
			timeField.setText("" + (Integer.parseInt(properties.getProperty("competition.time", String.valueOf(30 * 60))) / 60));
		} catch (Exception e) {
			// Allgemeine Fehlernachricht
			new StandardAlert(StandardMessageType.ERROR).showAndWait();
		}
	}
}
