package classes.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import classes.controller.ChipsController;
import classes.model.Chip;
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

public class SettingsPartialEdit implements Initializable {
	
	@FXML private TableView<Chip> dataTable;
	@FXML private TableColumn<Chip,String> idCol;
	@FXML private TableColumn<Chip,String> nameCol;
	
	@FXML private TextField chipField;
	@FXML private TextField nameField;
	
	// Der ChipsController verwaltet die Chips. 
	// Die Chips werden von der Tablle in diesen geschrieben und 
	// anschlieﬂend weiterverarbeitet (z.B. gespeichert)
	private ChipsController chipsController;

	// Zur Pflege der Chips
	@FXML
	public void addChip(ActionEvent e) {
		
		if(nameField.getText().equals("createTestChips")) {
			for(int i = 0; i < 50; i++) {
				Chip c = new Chip("" + (1000 + i), "Testperson " + (i + 1));
				chipsController.getChips().add(c);
			}
		}
		
		String id = chipField.getText().trim();
		String name = nameField.getText().trim();
		
		if(!id.equals("") && !name.equals("")) {
			Chip c = new Chip(chipField.getText().trim(), nameField.getText().trim());
			// FIXME: Hier den Inhalt der Tabelle neu laden.
			// Ansonsten werden Chips dopelt angezeigt, obwohl
			// diese im Hintergrund ¸berschreiben werden
			chipsController.getChips().add(c);
		}
		
		// Die Textfelder leeren
		chipField.setText("");
		nameField.setText("");
		
		update();
	}
	
	// Damit der Controller geupdatet wird, falls ein 
	// Chip ¸ber die Tabelle ge‰ndert wurde.
	public void nameColContentChanged(Event e) {
		// TODO: Chip lˆschen, wenn name auf "" gesetzt wird oder
		// ‰ndern...
		update();
	}
	
	private void update() {
		chipsController.save();
		dataTable.setItems(FXCollections.observableList(chipsController.getChips()));
		// TODO: Tabelleninhalt updaten
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory("studentName"));		
		// NameCol soll editierbar sein, damit man z.B. den Namen ‰ndern kann, 
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
