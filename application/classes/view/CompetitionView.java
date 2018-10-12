package classes.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import classes.CompetitionViewRowData;
import classes.Data;
import classes.SetupUtils;
import classes.controller.ChipsController;
import classes.model.Chip;
import classes.model.ChipState;
import classes.model.Competition;
import classes.model.CompetitionState;
import classes.repository.CompetitionRepository;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.dialog.StandardAlert;
import tp.dialog.StandardMessageType;


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
	
	// FIXME: TimerThread zerstören, wenn Wettkampf-Tab geschlossen wird.
	
	/*
	 * Anmerkung: Die Chips sind Teil des Wettkampfes! Die Referenz auf den Controller
	 * Wird nur direkt in den View gezogen, um Aktionen mit dem Controller zu vereinfachen.
	 */
	protected CompetitionRepository compRepo; // w/r eines Wettkampfes
	protected Competition comp;	
	protected ChipsController chipsController;
	
	// Wird verwendet vom MainView. Also wenn der Benutzer einen Wettkampf 
	// direkt ins Programm lädt.
	public CompetitionView(Competition comp, CompetitionRepository compRepo) {
		this.compRepo = compRepo;
		this.comp = comp;
		chipsController = comp.getChipsController();
	}
	
	public CompetitionView() throws IOException {
		// Bevor der ChipsController erstellt wird: Das Wettkampfverzeichnis erstellen
		// (falls nicht vorhanden) und Chips + Wettkampf kopieren. 
		SetupUtils.createCompetitionDirIfNotExists();
		// Noch den entsprechenden Wettkampf laden, wenn es einen gibt
		// Schauen, ob ein Wettkampf bereits vorhanden ist.
		compRepo = new CompetitionRepository(Data.DIR + "/" + Data.COMPETITION_DIR + "/" + Data.COMPETITION_FILE);
		comp = compRepo.read();
		//              ^^^^^^
		// Das ist der Teil, welche die Exception auslösen kann
		// Den ChipsController aus dem Wettkampf holen.
		chipsController = comp.getChipsController();
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
			// Die Startrunde soll nur eingefügt werden, wenn der Schüler
			// auch startet. Ist der Status eines Chips == DNS wird dieser nicht 
			// starten.
			if(curChip.getState() != ChipState.DNS) {
				chipsController.addLap(curId);
				dataList.add(new CompetitionViewRowData(curChip, curChip.getLaps().getLast()));
			}
		}
	}

	protected void stopCompetition() {
		comp.setState(CompetitionState.ENDED);
		scanTextField.setDisable(true);
		scanTextField.setText("");
		log("Zeit abgelaufen!");
		log("Wettkampf beendet");
		// TODO: Nicht nur hier speichern, sondern auch zwischen
		// durch. Am Besten: Bei jeder Runde den jeweiligen Chip speichern.
		chipsController.save();
		boolean success = compRepo.write(comp);
		if(!success) {
			log("Wettkampf konnte NICHT gespeichert werden!");
		}
	}
	
	protected void log(String message) {
		String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		logTextArea.appendText("\n[" + timestamp + "] " + message);
	}
	
	// FXML-METHODEN
	
	@FXML
	protected abstract void startBtnClick(Event event);
	
	@FXML
	protected void scanTextFieldOnKeyPressed(KeyEvent ke) {
		if(ke.getCode() == KeyCode.ENTER) {
			
			String scannedId = scanTextField.getText().trim();
			int addLapResult = chipsController.addLap(scannedId);
			
			if(addLapResult == 0) {
				// Eine Null-Pointer-exception muss hier nicht mehr abgefangen werden,
				// da diese bei einem Rückgabewert von -2 auftritt. Somit ist der Chip 
				// bei einem Rückgabewert von 0 vorhanden.
				comp.getData().add(new CompetitionViewRowData(chipsController.getChipById(scannedId)));
				log("Runde (id: " + scannedId + ")");
				
			} else if(addLapResult == -1) {
				log("Doppelscan (id: " + scannedId + ")");
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
		
		// Hier muss aufgrund der checkRequirements() nicht auf die Anzahl an vorhandenen Runden 
		// überprüft werden. 
		
		
		// Je nach dem in welchem Zustand sich der Wettkampf befindet, muss anders 
		// verfahren werden: 
		// (1) - State == PREPARE || READY
		//       -> Alle Runden löschen und neu eintragen (es ist nur Runde -1 vorhanden)
		// (2) - State == RUNNUNG || ENDED
		//       -> Die vorhandenen Wettkampfdaten anzeigen. Dabei RUNNING genau wie 
		//          ENDED behandeln.
		
		
		// Fall (1)
		if(comp.getState() == CompetitionState.PREPARE || comp.getState() == CompetitionState.READY) {
			// Bevor die Runde -1 eingefügt werden kann, müssen vorhandene Runden entfernt werden.
			// Dies ist nur ein Schönheitsaspekt, denn der Zeitstempel in Runde -1 ist irrelevant.
			chipsController.resetLaps();
			comp.getData().clear();
			// Jetzt in Ruhe zurücksetzen
			for(final Chip c : chipsController.getChips()) {
				// Dies fügt Runde -1 ein.
				chipsController.addLap(c.getId());
				comp.getData().add(new CompetitionViewRowData(c));
			}
			
			logTextArea.appendText("Wettkampf initialisiert.");
		}
		// Fall (2)
		else {
			// Damit nicht gestartet werden kann.
			startBtn.setDisable(true);
			logTextArea.appendText("Wettkampf im \"Anzeigemodus\". ");
		}
		
		// Die Daten aus dem Wettkampf mit der Tabelle verknüpfen.
		dataTable.setItems(comp.getData());
		
		

		boolean success = compRepo.write(comp);
		if(!success) {
			log("Warnung: Wettkampf kann nicht gespeichert werden!");
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
		// resetet werden. Allerdings nur, wenn Status == RUNNING || ENDED.
		if(comp.getState() != CompetitionState.PREPARE && comp.getState() != CompetitionState.READY) {
			Alert alert = generateAlert("competitionExists");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.isPresent()) {
				if(result.get().getButtonData().equals(ButtonData.YES)) {
					return reload();
				} else if(result.get().getButtonData().equals(ButtonData.NO)) {
					// vorhandenen Wettkampf anzeigen
					return true;
				} else if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
					return false;
				}
			}
		}
		else if(comp.getState() == CompetitionState.READY) {
			// Gibt es einen bereits vorbereiteten Wettkampf, so soll der Benutzer
			// gefragt werden, was geschen soll.
			Alert alert = generateAlert("competitionReady");
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.isPresent()) {
				if(result.get().getButtonData().equals(ButtonData.YES)) {
					// vorhandenen Wettkampf anzeigen
					return true;
				} else if(result.get().getButtonData().equals(ButtonData.NO)) {
					return reload();
				} else if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
					return false;
				}
			}
		} else {
			// Falls Runden existieren, aber keine Wettkampf-Datei (oder Wettkampf
			// im Status PREPARE).
			// Die Runde -1 soll einfach übergangen werden, es interressiert
			// im Endeffekt eh nicht, welcher Timestamp dort hinterlegt ist.
			if(chipsController.getHighestLapCount() == Chip.LAPCOUNT_START + 1) {
				// Chips neu laden -> true zurückgeben.
				Data.copyChips(Data.BASIC_DIR, Data.COMPETITION_DIR);
				chipsController.load();
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
						// Chips neu laden -> true zurückgeben.
						Data.copyChips(Data.BASIC_DIR, Data.COMPETITION_DIR);
						chipsController.load();
						return true;
					}
					else if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
						return false;
					}
				}
			}
			else {
				// in jedem anderen Fall:
				// Chips neu laden -> true zurückgeben.
				Data.copyChips(Data.BASIC_DIR, Data.COMPETITION_DIR);
				chipsController.load();
			}
		}
		
		return true;
	}
	
	private boolean reload() {
		// Wettkampf + Runden zurücksetzen
		try {
			// Einen frischen Wettkampf laden, damit eventuelle
			// neue Einstellungen übernommen werden.
			CompetitionRepository tempRepo = new CompetitionRepository(Data.DIR + "/" + Data.BASIC_DIR + "/" + Data.COMPETITION_FILE);
			compRepo.write(tempRepo.read());
			comp = compRepo.read();
		} catch (IOException e) {
			return false;
		}
		// Chips neu laden, damit neu eingetragene oder gelöschte auch 
		// angezeigt werden ode eben nicht.
		Data.copyChips(Data.BASIC_DIR, Data.COMPETITION_DIR);
		// Referenz updaten
		chipsController = comp.getChipsController();
		chipsController.load();
		return true;
	}
	
	/**
	 * Sorgt dafür, das der "Vorbereitungsdialog" geöffnet wird.
	 */
	@FXML
	private void prepareBtnClick(Event e) {
		// Das Checklisten-Fenster erstellen
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(((ImageView) e.getSource()).getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);
		
		
		// Das Laden vorbereiten...
		FXMLLoader templateLoader = new FXMLLoader(getClass().getResource("/templates/competition/prepareView.fxml"));
		templateLoader.setController(new PrepareView(chipsController, comp));
		// und laden
		Parent parent;
		try {
			// Laden und anzeigen
			parent = (Parent)templateLoader.load();
			Scene scene = new Scene(parent, 400, 400);
			stage.setScene(scene);
			stage.show();
		} catch(IOException e1) {
			new StandardAlert(StandardMessageType.ERROR);
		}
	}
	
	// GETTER UND SETTER
	// wird vom MainView verwendet, um den Wetkampf
	// vom Benutzer speichern zu lassen
	public Competition getCompetition() {
		return comp;
	}
	// wird vom MainView verwendet, um den Wetkampf
	// vom Benutzer speichern zu lassen
	public CompetitionRepository getCompetitionRepository() {
		return compRepo;
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
		else if(type.equals("competitionReady")) {
			alert.setContentText("Es ist bereits ein Wettkampf vorbereitet. " + 
					"Es kann mit diesem weiter gearbeitet werden oder ein neuer erstellt werden.");
			alert.getButtonTypes().addAll(new ButtonType("Weiter", ButtonBar.ButtonData.YES),
					new ButtonType("Neu", ButtonBar.ButtonData.NO),
					new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE));
		}
		
		return alert;
					
	}
}
