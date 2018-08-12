package classes.view;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import classes.controller.ChipsController;
import classes.model.Chip;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class SettingsPartialEdit implements Initializable {
	
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
			// Wenn ein Chip mit der gleichen Id bereits vorhanden ist, muss gefragt werden, ob
			// der vorhandene Chip �berschrieben werden soll.
			// Die Entscheidung wird �ber writeChip gesteuert.
			boolean writeChip = true;
			if(chipsController.getChipById(c.getId()) != null) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Ein Chip mit der ID " + c.getId() + " ist bereits im System vorhanden. " +
						"Soll dieser �berschrieben werden ?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.isPresent()) {
					// Wenn CANCEL_CLOSE gew�hlt wird, soll der Chip nicht 
					// �berschrieben werden
					if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
						writeChip = false;
					}
					else if(result.get().getButtonData().equals(ButtonData.OK_DONE)) {
						// Falls gew�scht, l�schen. Erzielt das �berschreiben.
						chipsController.getChips().remove(chipsController.getChipById(c.getId()));
					}
				}
			}
			// Den Chip hinzuf�gen, wenn writeChip true ist.
			// Dies ist der Fall wenn...
			// - der Chip noch nicht vorhanden ist.
			// - der Benutzer zugestimmt hat, einen bestehenden 
			//   Chip zu �berschreiben
			if(writeChip) {
				chipsController.getChips().add(c);
			}
		}
		
		// Die Textfelder leeren
		chipField.setText("");
		nameField.setText("");
		
		update();
	}
	
	private void removeChip(String id) {
		chipsController.getChips().remove(chipsController.getChipById(id));
		update();
	}
	
	private void update() {
		chipsController.save();
		dataTable.setItems(FXCollections.observableList(chipsController.getChips()));
		dataTable.refresh();
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory("studentName"));
		// NameCol soll editierbar sein, damit man z.B. den Namen �ndern kann, 
		// falls man sich vertipt hat.
		nameCol.setCellFactory(TextFieldTableCell.<Chip>forTableColumn());
		nameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Chip, String>>() {
                @Override
                public void handle(CellEditEvent<Chip, String> cee) {
                    Chip c = (Chip)cee.getTableView().getItems().get(cee.getTablePosition().getRow());
                    // Den neuen Wert setzen
                    c.setStudentName(cee.getNewValue());
                    
                    // update zum Speichern und so
                    update();
                }
            }
	    );
		
		// Eine Button-Spalte hinzuf�gen, wor�ber die Chips gel�scht werden k�nnen
		TableColumn<Chip, String> btnColumn = new TableColumn<Chip, String>();
		btnColumn.setCellValueFactory(new PropertyValueFactory("id"));
		dataTable.getColumns().add(btnColumn);
		btnColumn.setCellFactory(column -> {
		    return new TableCell<Chip, String>() {
		    	private Button btn = new Button("L�schen");		
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	super.updateItem(item, empty);
		        	if(empty) {
		        		setGraphic(null);
		        	}
		        	else {
		        		btn.setOnAction(e -> {
		        			removeChip(item);
		        		});
		                this.setGraphic(btn);
		        	}
		        }
		    };
	    });
		
		
		chipsController = new ChipsController();
		chipsController.load();
		List<Chip> chips = chipsController.getChips();
		if(chips.size() > 0) {
			dataTable.setItems(FXCollections.observableList(chips));	
		}		
	}
}
