package classes.controller.view;

import java.net.URL;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import classes.Chip;
import classes.Competition;
import classes.CompetitionViewRowData;
import classes.Round;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public abstract class CompetitionViewController implements Initializable {

	// Die Steuerelemente, die immer vorhanden sind
	@FXML protected TableView<CompetitionViewRowData> dataTable;
	@FXML protected TableColumn<CompetitionViewRowData, String> idCol;
	@FXML protected TableColumn<CompetitionViewRowData, String> studentNameCol;
	@FXML protected TableColumn<CompetitionViewRowData, Number> roundNumberCol;
	@FXML protected TableColumn<CompetitionViewRowData, String> timestampCol;
	@FXML protected TextArea logTextArea;
	@FXML protected Button startBtn;
	
	protected boolean started;
	/*
	 * Anmerkung: Die Chips werden nicht im Wettkamp 
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
	
	@FXML
	protected abstract void startBtnClick(Event event);
	
	@FXML
	private void scanTextFieldOnKeyReleased(Event event) {
		// TODO: Chip scannen
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
		// verfahren werden.
		chipsController.load();
		List<Chip> chips = chipsController.getChips();
		List<CompetitionViewRowData> dataList = new LinkedList<CompetitionViewRowData>();
		for(int i = 0; i < chips.size(); i++) {
			dataList.add(new CompetitionViewRowData(chips.get(i), new Round(LocalTime.now())));
		}
		
		dataTable.setItems(FXCollections.observableList(dataList));		
	}
}
