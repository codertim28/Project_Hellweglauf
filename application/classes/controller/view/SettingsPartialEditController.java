package classes.controller.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import classes.Chip;
import classes.controller.ChipsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class SettingsPartialEditController implements Initializable {
	
	@FXML private TableView<Chip> dataTable;
	@FXML private TableColumn<Chip,String> idCol;
	@FXML private TableColumn<Chip,String> nameCol;
	
	@FXML private TextField chipField;
	@FXML private TextField nameField;
	
	// Der ChipsController verwaltet die Chips. 
	// Die Chips werden von der Tablle in diesen geschrieben und 
	// anschließend weiterverarbeitet (z.B. gespeichert)
	private ChipsController chipsController;

	// Zur Pflege der Chips
	public void addChips(ActionEvent e) {
		// TODO: Eine Idiotensicherung einbauen, 
		// damit keine leeren Chips erstellt werden können.
		
		Chip c = new Chip(chipField.getText().trim(), nameField.getText().trim());
		dataTable.getItems().add(c);
		
		// Die Textfelder leeren
		chipField.setText("");
		nameField.setText("");
		
		updateChipsController();
	}
	
	// Damit der Controller geupdatet wird, falls ein 
	// Chip über die Tabelle geändert wurde.
	public void nameColContentChanged(Event e) {
		// TODO: Chip löschen, wenn name auf "" gesetzt wird
		// FIXME: Chip wird über dieses Event nicht verändert.
		updateChipsController();
	}
	
	private void updateChipsController() {
		chipsController.setChips(dataTable.getItems());		
		chipsController.save();
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory("studentName"));		
		// NameCol soll editierbar sein, damit man z.B. den Namen ändern kann, 
		// falls man sich vertipt hat.
		nameCol.setCellFactory(TextFieldTableCell.<Chip>forTableColumn());
		
		chipsController = new ChipsController();
		chipsController.load();
		List<Chip> chips = chipsController.getChips();
		if(chips.size() > 0) {
			dataTable.setItems(FXCollections.observableList(chips));	
		}		
	}
}
