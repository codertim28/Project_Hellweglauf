package classes.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import classes.Data;
import classes.controller.ChipsController;
import classes.model.Chip;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class SettingsPartialEdit implements Initializable {
	
	@FXML private TableView<Chip> dataTable;
	@FXML private TableColumn<Chip,String> idCol;
	@FXML private TableColumn<Chip,String> nameCol;
	@FXML private TableColumn<Chip,String> formCol;
	
	@FXML private TextField chipField;
	@FXML private TextField nameField;
	@FXML private ChoiceBox<String> formChoiceBox;
	
	// Der ChipsController verwaltet die Chips. 
	// Die Chips werden von der Tablle in diesen geschrieben und 
	// anschließend weiterverarbeitet (z.B. gespeichert)
	private ChipsController chipsController;

	// Zur Pflege der Chips
	@FXML
	public void addChip(ActionEvent e) {
		
		if(nameField.getText().equals("createTestChips")) {
			List<Chip> testChips = IntStream.range(1000, 1050)
					.mapToObj(i -> new Chip("" + i, "Testperson " + (i + 1), "None"))
					.collect(Collectors.toList());
			chipsController.getChips().addAll(testChips);
		}
		
		String id = chipField.getText().trim();
		String name = nameField.getText().trim();
		
		if(!id.equals("") && !name.equals("")) {
			Chip c = new Chip(chipField.getText().trim(), nameField.getText().trim(), formChoiceBox.getSelectionModel().getSelectedItem());
			// Wenn ein Chip mit der gleichen Id bereits vorhanden ist, muss gefragt werden, ob
			// der vorhandene Chip überschrieben werden soll.
			// Die Entscheidung wird über writeChip gesteuert.
			boolean writeChip = true;
			if(chipsController.getChipById(c.getId()) != null) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Ein Chip mit der ID " + c.getId() + " ist bereits im System vorhanden. " +
						"Soll dieser Überschrieben werden ?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.isPresent()) {
					// Wenn CANCEL_CLOSE gewählt wird, soll der Chip nicht 
					// überschrieben werden
					if(result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
						writeChip = false;
					}
					else if(result.get().getButtonData().equals(ButtonData.OK_DONE)) {
						// Falls gewüscht, löschen. Erzielt das überschreiben.
						chipsController.getChips().remove(chipsController.getChipById(c.getId()));
					}
				}
			}
			// Den Chip hinzufügen, wenn writeChip true ist.
			// Dies ist der Fall wenn...
			// - der Chip noch nicht vorhanden ist.
			// - der Benutzer zugestimmt hat, einen bestehenden 
			//   Chip zu überschreiben
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
		formCol.setCellValueFactory(new PropertyValueFactory("form"));
		// NameCol soll editierbar sein, damit man z.B. den Namen ändern kann, 
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
		
		// Eine Button-Spalte hinzufügen, worüber die Chips gelöscht werden können
		TableColumn<Chip, String> btnColumn = new TableColumn<Chip, String>();
		btnColumn.setCellValueFactory(new PropertyValueFactory("id"));
		dataTable.getColumns().add(btnColumn);
		btnColumn.setCellFactory(column -> {
		    return new TableCell<Chip, String>() {
		    	private Button btn = new Button("Löschen");		
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
		
		// Die Klassen laden und in die ChoiceBox einfügen
		try {
			formChoiceBox.setItems(FXCollections.observableList((ArrayList<String>)Data.readObject(Data.BASIC_DIR + "/" + Data.FORMS_FILE)));
			// Damit immer das erste Item ausgewählt ist und keine Fehler entstehen.
			formChoiceBox.getSelectionModel().select(0); 
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Die Chips laden und anzeigen
		chipsController = new ChipsController();
		chipsController.load();
		List<Chip> chips = chipsController.getChips();
		if(chips.size() > 0) {
			dataTable.setItems(FXCollections.observableList(chips));	
		}		
	}
}

