package classes.controller.view;

import java.net.URL;
import java.util.ResourceBundle;

import classes.Chip;
import classes.controller.ChipsController;
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
	// anschlie�end weiterverarbeitet (z.B. gespeichert)
	private ChipsController chipsController;

	// Zur Pflege der Chips
	private void addChips(ActionEvent e) {
		// TODO: Eine Idiotensicherung einbauen, 
		//damit keine leeren Chips erstellt werden k�nnen.
		
		Chip c = new Chip(chipField.getText().trim(), nameField.getText().trim());
		dataTable.getItems().add(c);
		
		// Die Textfelder leeren
		chipField.setText("");
		nameField.setText("");
		
		updateChipsController();
	}
	
	// Damit der Controller geupdatet wird, falls ein 
	// Chip �ber die Tabelle ge�ndert wurde.
	private void nameColContentChanged(Event e) {
		// TODO: Chip l�schen, wenn name auf "" gesetzt wird
		updateChipsController();
	}
	
	private void updateChipsController() {
		chipsController.setChips(dataTable.getItems());		
		//chipsController.save();
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory("studentName"));		
		// NameCol soll editierbar sein, damit man z.B. den Namen �ndern kann, 
		// falls man sich vertipt hat.
		nameCol.setCellFactory(TextFieldTableCell.<Chip>forTableColumn());
		
		chipsController = new ChipsController();
	}
}
