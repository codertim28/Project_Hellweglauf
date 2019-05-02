package classes.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import classes.CompetitionViewRowData;
import classes.Constants;
import classes.Data;
import classes.controller.ChipsController;
import classes.model.Chip;
import classes.model.Competition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.dialog.StandardAlert;
import tp.dialog.StandardMessageType;
import tp.logging.SimpleLoggingUtil;

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
		
		try {
			competition = Data.readComp(Data.BASIC_DIR);
			chipsController = new ChipsController();
			chipsController.setChips(Data.readChips(Data.BASIC_DIR));
			
			// Runde -1 einfügen
			for(final Chip c : chipsController.getChips()) {
				chipsController.addLap(c.getId());
				competition.getData().add(new CompetitionViewRowData(c));
			}
		} catch (IOException e) {
			log("Trainingsdaten konnten nicht geladen werden!");
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(e);
		}
		// Die Daten aus dem Wettkampf mit der Tabelle verknüpfen.
		dataTable.setItems(competition.getData());	
		scanTextField.requestFocus();
	}
}
