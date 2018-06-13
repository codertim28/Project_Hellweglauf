package classes.controller.view;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import static java.time.temporal.ChronoUnit.SECONDS;

import classes.model.Chip;
import classes.model.Competition;
import classes.CompetitionViewRowData;
import classes.model.Lap;
import classes.controller.ChipsController;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class CompetitionViewController implements Initializable {

	// Die Steuerelemente, die immer vorhanden sind
	@FXML protected TableView<CompetitionViewRowData> dataTable;
	@FXML protected TableColumn<CompetitionViewRowData, String> idCol;
	@FXML protected TableColumn<CompetitionViewRowData, String> studentNameCol;
	@FXML protected TableColumn<CompetitionViewRowData, Number> roundNumberCol;
	@FXML protected TableColumn<CompetitionViewRowData, String> timestampCol;
	@FXML protected TextArea logTextArea;
	@FXML protected Button startBtn;
	@FXML protected TextField scanTextField;
	
	protected boolean started;
	/*
	 * Anmerkung: Die Chips werden nicht im Wettkampf
	 * gespeichert! Dies liegt daran, dass jeder Chip,
	 * welcher dem System bekannt ist, an jedem Wettkampf
	 * teilnimmt. Somit bilden die Chips eine eigene
	 * Einheit.
	 */
	protected Competition comp;	
	protected ChipsController chipsController;
	
	public CompetitionViewController() {
		started = false;
		chipsController = new ChipsController();
	}
	
	protected void setStartRounds() {
		List<CompetitionViewRowData> dataList = dataTable.getItems();
		// An dieser Stelle muss nicht abgefragt werden, ob alle Chips nur 
		// Runde -1 besitzen, da dies in der initialize(...) sowie in 
		// startBtnClick(...) geregelt wird.
		int size = dataList.size();
		//  ^^^^ dies muss hier so stehen! 
		// dataList.size() darf nicht zum steuern, der Schleife 
		// verwendet werden, da so eine Endlosschleife entsteht.
		for(int i = 0; i < size; i++) {
			String curId = dataList.get(i).getChip().getId();
			Chip curChip = chipsController.getChipById(curId);
			chipsController.addRound(curId);
			dataList.add(new CompetitionViewRowData(curChip, curChip.getLaps().getLast()));
		}
	}

	protected void stopCompetition() {
		started = false;
		scanTextField.setDisable(true);
		scanTextField.setText("");
		log("Zeit abgelaufen!");
		log("Wettkampf beendet");
		// TODO: Nicht nur hier speichern, sondern auch zwischen
		// durch. Am Besten: Bei jeder Runde den jeweiligen Chip speichern.
		chipsController.save();
	}
	
	protected void log(String message) {
		String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		logTextArea.appendText("\n[" + timestamp + "] " + message);
	}
	
	// FXML-METHODEN
	
	@FXML
	protected abstract void startBtnClick(Event event);
	
	@FXML
	private void scanTextFieldOnKeyPressed(KeyEvent ke) {
		if(ke.getCode() == KeyCode.ENTER) {
			String scannedId = scanTextField.getText().trim();
			Chip chip = chipsController.getChipById(scannedId);
			// Wenn ein Chip gefunden wurde, ist alles gut.
			if(chip != null) {
				LocalTime timestampOfLastRound = chip.getLaps().getLast().getTimestamp();
				// Diese Abfrage verhindert einen "Doppelscan"
				// TODO: Doppelscan in der addRound() abfragen ?
				if(SECONDS.between(timestampOfLastRound, LocalTime.now()) >= 10) {
					List<CompetitionViewRowData> dataList = dataTable.getItems();
					chipsController.addRound(scannedId);
					dataList.add(new CompetitionViewRowData(chip, chip.getLaps().getLast()));
					log("Runde (id: " + scannedId + ")");
				}
			} else {
				// Kein Chip gefunden: loggen
				log("FEHLER (1) (id: " + scannedId +")");
			}
			
			scanTextField.setText("");
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(cellData -> cellData.getValue().getChip().idProperty());
		studentNameCol.setCellValueFactory(cellData -> cellData.getValue().getChip().studentNameProperty());
		roundNumberCol.setCellValueFactory(cellData -> cellData.getValue().getRound().numberProperty());
		timestampCol.setCellValueFactory(cellData -> cellData.getValue().getRound().timestampProperty());

		// TODO: Wenn es bereits eine Wettkampf-datei gibt, so soll diese 
		// in die Tabelle geladen werden. Ansonsten sollen alle Chips die Runde -1
		// zugeteilt bekommen. Wenn ein Chip bereits Runden besitzt, muss irgendwie anders
		// verfahren werden. Stichwort: Alert.
		chipsController.load();
		List<Chip> chips = chipsController.getChips();
		List<CompetitionViewRowData> dataList = new LinkedList<CompetitionViewRowData>();
		for(int i = 0; i < chips.size(); i++) {
			Chip curChip = chips.get(i);
			// Dies f�gt die Runde -1 ein.
			chipsController.addRound(curChip.getId());
			dataList.add(new CompetitionViewRowData(curChip, curChip.getLaps().getLast()));
		}
		
		dataTable.setItems(FXCollections.observableList(dataList));
		logTextArea.appendText("Wettkampf initialisiert.");
	}
}
