package classes.view;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import classes.Data;
import classes.model.Chip;
import classes.model.Competition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;

public class PrintView implements Initializable {
	
	@FXML private ChoiceBox<String> printerChoiceBox;
	@FXML private ListView<Chip> studentListView;
	@FXML private WebView previewWebView;
	
	private PrintService[] printServices;
	private Competition comp;
	
	public PrintView(Competition comp) {
		setCompetition(comp);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		printServices = PrintServiceLookup.lookupPrintServices(null, null); // Mit DocFlavor html geht's nicht :/
		
		// die verfügbaren Printer setzen
		Arrays.stream(printServices).forEach(ps -> printerChoiceBox.getItems().add(ps.getName()));
		
		// Alle Schüler anzeigen, die mind. eine Runde gelaufen sind
		// zuerst cellfactory setzen
		studentListView.setCellFactory(l -> new ListCell<Chip>() {
	        @Override
	        protected void updateItem(Chip chip, boolean empty) {
	            super.updateItem(chip, empty);
	            if (empty || chip == null) {
	                setText(null);
	            } else {
	                setText(chip.getStudentName());
	            }
	        }
		});
		// Schüler filtern und setzen
		comp.getChipsController().getChips().stream()
			.filter(c -> c.getLapCount() > 0)
			.forEach(c -> studentListView.getItems().add(c));
		
		// Die Preview setzen
		try {
			previewWebView.getEngine().load(new File(Data.DIR + "/" + Data.BASIC_DIR + "/urkunde-vorlage.html").toURI().toURL().toString());
		} catch(MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// GETTER UND SETTER
	public Competition getCompetition() {
		return comp;
	}

	public void setCompetition(Competition comp) {
		this.comp = comp;
	}

}
