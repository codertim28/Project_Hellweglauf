package prohell.prohell.classes.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import prohell.prohell.classes.Constants;
import prohell.prohell.tp.logging.SimpleLoggingUtil;

public class SettingsView implements Initializable {

	@FXML 
	private GridPane root;
	
	@FXML private Tab manageChipsTab, importTab, settingsTab;
	
	// Das MainView wird hier ben�tigt, damit es 
	// ggf. geupdatet werden kann (z.B. wenn keine Chips
	// vorhanden sind, werden die Wettkampfbuttons gesperrt usw.)
	@FXML private MainView mainView;
	
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		try {
			manageChipsTab.setContent(FXMLLoader.load(getClass().getResource("/templates/settings/settingsPartialEdit.fxml")));
			// TODO: import-Funktionalit�t
			settingsTab.setContent(FXMLLoader.load(getClass().getResource("/templates/settings/settingsViewPartialCompetition.fxml")));
		} catch (Exception ex) {
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(ex);
		}
	}
}
