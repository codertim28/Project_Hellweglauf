package classes.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

import javax.print.PrintException;

import classes.Data;
import classes.model.Chip;
import classes.model.Competition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebView;
import tp.dialog.StandardAlert;
import tp.dialog.StandardMessageType;

public class PrintView implements Initializable {
	
	@FXML private ChoiceBox<String> printerChoiceBox;
	@FXML private ListView<Chip> studentListView;
	@FXML private WebView previewWebView;
	

	private ObservableSet<Printer> allPrinter;
	private Competition comp;
	
	public PrintView(Competition comp) {
		setCompetition(comp);
	}
	
	@FXML 
	private void printBtnClick() throws PrintException, IOException {
		// Ausgewählten Printer holen
		// (muss leider so kompliziert sein, anders kann man auf die
		//  Elemente des ObservableSet nicht zugreifen...)
		Printer printer = allPrinter.stream().filter(p -> {
			if(p.getName().equals(printerChoiceBox.getSelectionModel().getSelectedItem())) {
				return true;
			}
			return false;
		}).findFirst().get(); // endlich das Element bekommen..
		
		Node node = this.previewWebView;
		// Das WebView transformieren... bisschen unschön an dieser Stelle
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        node.getTransforms().add(new Scale(scaleX, scaleY));
 
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allPrinter = Printer.getAllPrinters();
		// die verfügbaren Printer setzen
		allPrinter.stream().forEach(ps -> printerChoiceBox.getItems().add(ps.getName()));
		
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
		
		// Wenn ein neues Item ausgewählt wurde: Die WebView rendern
		studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chip>() {
		    @Override
		    public void changed(ObservableValue<? extends Chip> observable, Chip oldValue, Chip newValue) {
		        // Rendern mit dem ausgewählen Chip, welcher in newValue steht
		        renderDocument(newValue);
		    }
		});
		
		// Die Preview setzen (den ersten Chip in der Liste rendern)
		renderDocument(comp.getChipsController().getChips().get(0));
		previewWebView.setZoom(0.38);

	}
	
	// Rendert das HTML-Dokument mit einem entsprechendem Chip
	private void renderDocument(Chip c) {
		try {
			// Die Vorlage auslesen
			List<String> list = Files.readAllLines(new File(Data.DIR + "/" + Data.BASIC_DIR + "/urkunde-vorlage.html").toPath());
		
			// Zeile für Zeile in die Datei "letzterDruck.html" schreiben.
			// Dabei werden die entsprechenden Templatevariablen ersetzt:
			//   - <%name%>            Name des Schülers
			//   - <%runden%>          Anzahl der gelaufenden Runden
			//   - <%rundenliste%>     Übersicht über die gelaufenen Runden mit Zeitstempel
			//   - <%wettkampfdetail%> (Optional) Infos über den Wettkampf 
			PrintWriter pw = new PrintWriter(new FileWriter(Data.DIR + "/" + Data.BASIC_DIR + "/letzterDruck.html"));
			
			// Hier darf nicht gefiltert werden, da sonst HTML und CSS verloren geht
			list.stream().map(line -> { 
				return line = line.replaceAll("<%name%>", c.getStudentName())
						.replaceAll("<%runden%>", "" + c.getLapCount());
			}).forEach(pw::println);
			
			pw.close();
			
			// Anzeigen
			previewWebView.getEngine().load(new File(Data.DIR + "/" + Data.BASIC_DIR + "/letzterDruck.html").toURI().toURL().toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new StandardAlert(StandardMessageType.ERROR);
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
