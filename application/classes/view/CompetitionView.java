package classes.view;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import classes.CompetitionViewRowData;
import classes.Data;
import classes.controller.ChipsController;
import classes.model.Chip;
import classes.model.Competition;
import classes.model.CompetitionState;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public abstract class CompetitionView implements Initializable {

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
	
	public CompetitionView() {
		started = false;
		// Bevor der ChipsController erstellt wird: Das Wettkampfverzeichnis erstellen
		// (falls nicht vorhanden) und Chips + Wettkampf kopieren. 
		Data.createCompetitionDirIfNotExists();
		chipsController = new ChipsController(Data.COMPETITION_DIR);
		// Muss hier geladen werden. Chips werden bereits vor der initialize(...)
		// verwendet. (s. checkRequirements)
		chipsController.load(); 
		// Noch den entsprechenden Wettkampf laden, wenn es einen gibt
		try {
			// Schauen, ob ein Wettkampf bereits vorhanden ist.
			comp = Data.readComp(Data.COMPETITION_DIR);
		} catch (Exception e) {
			// Wenn es hierzu kommt, darf nichts mehr gemacht werden (auch nicht die
			// die checkRequirements) !
			System.err.println("Wettkampf konnte nicht geladen werden.");
			System.err.println(e.getMessage());
			// TODO: Benutzer benarichtigen, dass kein Wettkampf geladen werden konnte.
		}
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
			String curId = dataList.get(i).chipIdProperty().get();
			Chip curChip = chipsController.getChipById(curId);
			chipsController.addLap(curId);
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
				// TODO: Doppelscan in der addLap() abfragen ?
				if(SECONDS.between(timestampOfLastRound, LocalTime.now()) >= 10) {
					List<CompetitionViewRowData> dataList = dataTable.getItems();
					chipsController.addLap(scannedId);
					dataList.add(new CompetitionViewRowData(chip));
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
		idCol.setCellValueFactory(cellData -> cellData.getValue().chipIdProperty());
		studentNameCol.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
		roundNumberCol.setCellValueFactory(cellData -> cellData.getValue().lapNumberProperty());
		timestampCol.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
		
		// Hier muss aufgrund der checkReqirements() nicht auf die Anzahl an vorhandenen Runden 
		// überprüft werden. 
		
		// TODO: Die gesamte Methode ab hier überarbeiten. Was passiert, wenn ein Wettkampf
		// DataRows hat ? -> DataRows anzeigen und Chips in Ruhe lassen.
		List<Chip> chips = chipsController.getChips();
		LinkedList<CompetitionViewRowData> dataList = new LinkedList<CompetitionViewRowData>();
		for(int i = 0; i < chips.size(); i++) {
			Chip curChip = chips.get(i);
			// Dies fügt die Runde -1 ein.
			chipsController.addLap(curChip.getId());
			dataList.add(new CompetitionViewRowData(curChip, curChip.getLaps().getLast()));
		}
		chipsController.save();
		dataTable.setItems(FXCollections.observableList(dataList));
		logTextArea.appendText("Wettkampf initialisiert.");
		
		try {
			if(comp != null) {
				// unter Umständen ist das Wettkampfobjekt nicht vorhanden.
				comp.setData(dataList);
				comp.setState(CompetitionState.ENDED);
				Data.writeComp(Data.COMPETITION_DIR, comp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Prüft die Bedingung und verändert diese, wenn der Benutzer dies fordert 
	 * (per Buttons im Dialog)
	 * @return false, wenn View nicht eingebunden werden kann. true, wenn View 
	 * eingebunden werden kann, z.B. weil der Benutzer einen Wettkampf resetet hat.
	 */
	public boolean checkRequirements() {
		// Falls ein Wettkampf existiert, muss dieser erst 
		// resetet werden.
		if(comp.getState() != CompetitionState.PREPARE) {
			Alert alert = generateAlert("competitionExists");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.isPresent()) {
				if(result.get().getButtonData().equals(ButtonData.YES)) {
					// Wettkampf + Runden zurücksetzen
					comp.setData(new LinkedList<CompetitionViewRowData>()); // Hier darf kein neuer Wettkampf erstellt werden,
					comp.setState(CompetitionState.PREPARE);				// da so die Einstellungen verloren gehen würden.
					chipsController.resetLaps();
					return true;
					
				} else if(result.get().getButtonData().equals(ButtonData.NO)) {
					// vorhandenen Wettkampf anzeigen
					return true;
				} else if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
					return false;
				}
			}
		} else {
			// Falls Runden existieren, aber keine Wettkampf-Datei.
			// Die Runde -1 soll einfach übergangen werden, es interressiert
			// im Endeffekt eh nicht, welcher Timestamp dort hinterlegt ist.
			if(chipsController.getHighestLapCount() == Chip.LAPCOUNT_START + 1) {
				// Runden zurücksetzen -> true zurückgeben.
				chipsController.resetLaps();
				return true;
			}
			// sonst sollte jedoch der Benutzer entscheiden
			else if(chipsController.getHighestLapCount() != Chip.LAPCOUNT_START) {
				// Wenn mind. ein Chip eine Runde enthählt, den Benutzer informieren.
				Alert alert = generateAlert("lapsExist");
				// Der traditionelle Ansatz ist an dieser Stelle schöner und 
				// komfortabler als ein Lamda-Ausdruck
				Optional<ButtonType> result = alert.showAndWait();
				if(result.isPresent()) {
					if(result.get().getButtonData().equals(ButtonData.YES)) {
						// Runden zurücksetzen -> true zurückgeben.
						chipsController.resetLaps();
						return true;
					}
					else if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private Alert generateAlert(String type) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Achtung!");
		alert.setTitle("");
		alert.getButtonTypes().clear();
		
		// Noch ein bisschen schön machen mit css
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
		dialogPane.getStyleClass().add("hellwegDialog");
		
		// Je nach dem, was abgefragt werden soll, einen anderen Text, sowie 
		// Buttons setzen.
		if(type.equals("lapsExist")) {
			alert.setContentText("Es gibt bereits Runden. " + 
					"Um einen neuen Wettkampf zu starten, " +
					"müssen die Runden der Chips zurückgesetzt werden.");
			alert.getButtonTypes().addAll(new ButtonType("Zurücksetzen", ButtonBar.ButtonData.YES),
					new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE));
		}
		else if(type.equals("competitionExists")) {
			alert.setContentText("Es ist bereits ein Wettkampf vorhanden. " + 
					"Um einen neuen Wettkampf zu starten, muss ein neuer Wettkampf erstellt werden.");
			alert.getButtonTypes().addAll(new ButtonType("Neu", ButtonBar.ButtonData.YES),
					new ButtonType("Anzeigen", ButtonBar.ButtonData.NO),
					new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE));
		}
		
		return alert;
					
	}
}
