package prohell.prohell.classes.view;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import prohell.prohell.classes.CompetitionViewRowData;
import prohell.prohell.classes.controller.ChipsController;
import prohell.prohell.classes.model.Chip;
import prohell.prohell.classes.model.Competition;

// Quasi ein Mini-CompetitionView
public class TrainingView implements Initializable {

	// Die Steuerelemente, die immer vorhanden sind
	@FXML
	protected TableView<CompetitionViewRowData> dataTable;
	@FXML protected TableColumn<CompetitionViewRowData, String> idCol;
	@FXML protected TableColumn<CompetitionViewRowData, String> studentNameCol;
	@FXML protected TableColumn<CompetitionViewRowData, Number> roundNumberCol;
	@FXML protected TableColumn<CompetitionViewRowData, String> timestampCol;
	@FXML protected TextArea logTextArea;
	@FXML protected Button startBtn;
	@FXML protected TextField scanTextField;

	protected Competition competition;
	protected ChipsController chipsController;

	public TrainingView() {
	
	}

	protected void log(String message) {
		String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		logTextArea.appendText("\n[" + timestamp + "] " + message);
	}

	@FXML
	protected void scanTextFieldOnKeyPressed(KeyEvent ke) {
		if (ke.getCode() == KeyCode.ENTER) {

			String scannedId = scanTextField.getText().trim();
			int addLapResult = chipsController.addLap(scannedId);

			if (addLapResult == 0) {
				competition.getData().add(new CompetitionViewRowData(chipsController.getChipById(scannedId)));
				log("Runde (id: " + scannedId + ")");

			} else if (addLapResult == -1) {
				log("Doppelscan (id: " + scannedId + ")");
			} else {
				// Kein Chip gefunden: loggen
				log("FEHLER (1) (id: " + scannedId + ")");
			}

			scanTextField.setText("");
			Platform.runLater(() -> dataTable.scrollTo(dataTable.getItems().size() - 1));
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(cellData -> cellData.getValue().chipIdProperty());
		studentNameCol.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
		roundNumberCol.setCellValueFactory(cellData -> cellData.getValue().lapNumberProperty());
		timestampCol.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
		
		logTextArea.appendText("Training");
		
		competition = Competition.fromProperties();
		chipsController = new ChipsController();
		chipsController.load();
		chipsController.resetLaps();

		// Runde -1 einfügen
		for(final Chip c : chipsController.getChips()) {
			chipsController.addLap(c.getId());
			competition.getData().add(new CompetitionViewRowData(c));
		}

		// Die Daten aus dem Wettkampf mit der Tabelle verknüpfen.
		dataTable.setItems(competition.getData());	
		scanTextField.requestFocus();
	}
}
