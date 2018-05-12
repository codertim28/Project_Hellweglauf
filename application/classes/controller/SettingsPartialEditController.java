package classes.controller;

import java.net.URL;
import java.util.ResourceBundle;

import classes.Chip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class SettingsPartialEditController implements Initializable {
	
	@FXML
	private TableView<Chip> dataTable;
	@FXML
	private TableColumn<Chip,String> idCol;
	@FXML
	private TableColumn<Chip,String> nameCol;
	
	@FXML
	private TextField chipField;
	@FXML
	private TextField nameField;

	// Zur Pflege der Chips
	public void addChips(ActionEvent e) {
		Chip c = new Chip(chipField.getText().trim(), nameField.getText().trim());
		System.out.println("Chip: " + c.getId() + " / " + c.getStudentName());
		
		dataTable.getItems().add(c);
		dataTable.setEditable(true);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory("studentName"));		
		// NameCol soll editierbar sein, damit man z.B. den Namen ändern kann, 
		// falls man sich vertipt hat.
		nameCol.setCellFactory(TextFieldTableCell.<Chip>forTableColumn());
	}
}
