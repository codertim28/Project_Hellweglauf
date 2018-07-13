package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.Data;
import classes.model.Competition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class SettingsPartialCompetition implements Initializable {
	
	@FXML
	private TextField lapLengthField;
	@FXML
	private TextField lapCountField;
	@FXML
	private TextField timeField;
	
	@FXML
	private void saveBtnClick() {
		Competition comp = new Competition();
		comp.setName("bla"); // Attribut wird derzeit nicht verwendet
		// TODO: Parse-Fehler abfangen und den Benutzer darauf hinweisen,
		// das mind. eine Eingabe falsch ist.
		comp.setLapLength(Integer.parseInt(lapLengthField.getText()));
		comp.setLapCount(Double.parseDouble(lapCountField.getText()));
		comp.setTime(Integer.parseInt(timeField.getText()));
		
		try {
			Data.writeObject(Data.BASIC_DIR, comp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Benuter benachrichtigen, ob alles gelappt hat.
	}
	
	@FXML
	private void resetBtnClick() {
		Competition defaultComp = new Competition();
		defaultComp.setName("Wettkampf");
		defaultComp.setLapCount(2.5);
		defaultComp.setLapLength(400);
		defaultComp.setTime(30);
		
		try {
			Data.writeObject(Data.BASIC_DIR, defaultComp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Neu laden und Benuter benachrichtigen, ob alles gelappt hat.
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			// Das Wettkampfobjekt aus dem basic_dir lesen und casten
			Competition comp = (Competition)Data.readObject(Data.BASIC_DIR);;
			// Die Attribute in Textfelder schreiben, damit diese vom Benutzer
			// bearbeitet werden können.
			lapLengthField.setText("" + comp.getLapLength());
			lapCountField.setText("" + comp.getLapCount());
			timeField.setText("" + comp.getTime());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
