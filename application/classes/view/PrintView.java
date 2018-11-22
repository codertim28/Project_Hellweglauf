package classes.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.print.PrintException;

import classes.Data;
import classes.model.Chip;
import classes.model.Competition;
import classes.model.Lap;
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
import javafx.stage.Stage;
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
	private void listOfResultsPrintBtnClick() {
		// FIXME: Beim Drucken gehen die Style angaben verloren
		// Die Liste rendern
		renderListOfResults();
		
        previewWebView.setZoom(1); // Damit die volle Breite des Blattes ausgenutzt wird
		PrinterJob job = PrinterJob.createPrinterJob(getSelectedPrinter());
		job.showPrintDialog(previewWebView.getScene().getWindow());
	    if (job != null) {
	    	previewWebView.getEngine().print(job);
	        job.endJob();
	    }
	    previewWebView.setZoom(0.38); // Zur�ck setzten, damit es f�r den Benutzer sch�n ist
	}
	
	@FXML 
	private void printBtnClick() throws PrintException, IOException {
		Printer printer = getSelectedPrinter();
		
		Node node = this.previewWebView;
		// Das WebView transformieren... bisschen unsch�n an dieser Stelle
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
		// die verf�gbaren Printer setzen
		allPrinter.stream().forEach(ps -> printerChoiceBox.getItems().add(ps.getName()));
		// TODO: ersten Printer ausw�hlen um Exception zu verhindern
		
		// Alle Sch�ler anzeigen, die mind. eine Runde gelaufen sind
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
		// Sch�ler filtern und setzen
		comp.getChipsController().getChips().stream()
			.filter(c -> c.getLapCount() > 0)
			.forEach(c -> studentListView.getItems().add(c));
		
		// Wenn ein neues Item ausgew�hlt wurde: Die WebView rendern
		studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chip>() {
		    @Override
		    public void changed(ObservableValue<? extends Chip> observable, Chip oldValue, Chip newValue) {
		        // Rendern mit dem ausgew�hlen Chip, welcher in newValue steht
		        renderDocument(newValue);
		    }
		});
		
		// Die Preview setzen (den ersten Chip in der Liste rendern)
		renderDocument(comp.getChipsController().getChips().get(0));
		previewWebView.setZoom(0.38);

	}
	
	// Rendert das HTML-Dokument mit einem entsprechendem Chip
	private void renderDocument(Chip c) {
		
		// Die Rundenliste in Form einer HTML-Tabelle vorbereiten
		LinkedList<Lap> laps = c.getLaps();
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder.append("<table>");
		// Start (0 ist Runde -1 -> somit ist 1 die 0. Runde)
		tableBuilder.append("<tr><td>Start</td><td>" + laps.get(1).getTimestampAsString() + "</td></tr>");
		for(int i = 2; i < laps.size(); i++) {
			Lap lap = laps.get(i);
			tableBuilder.append("<tr><td>Runde " + lap.getNumber() + "</td><td>" + lap.getTimestampAsString() + "</td></tr>");
		}	
		tableBuilder.append("</table>");
		// final, damit die Tabelle im Lambda verf�gbar ist.
		final String table = tableBuilder.toString();
			
		try {
			// Die Vorlage auslesen
			List<String> list = Files.readAllLines(new File(Data.DIR + "/" + Data.BASIC_DIR + "/urkunde-vorlage.html").toPath());
		
			// Zeile f�r Zeile in die Datei "letzterDruck.html" schreiben.
			// Dabei werden die entsprechenden Templatevariablen ersetzt:
			//   - <%name%>            Name des Sch�lers
			//   - <%runden%>          Anzahl der gelaufenden Runden
			//   - <%rundenliste%>     �bersicht �ber die gelaufenen Runden mit Zeitstempel
			//   - <%wettkampfdetail%> (Optional) Infos �ber den Wettkampf 
			PrintWriter pw = new PrintWriter(new FileWriter(Data.DIR + "/" + Data.BASIC_DIR + "/letzterDruck.html"));
			
			// Hier darf nicht gefiltert werden, da sonst HTML und CSS verloren geht
			list.stream().map(line -> { 
				return line = line.replaceAll("<%name%>", c.getStudentName())
						.replaceAll("<%runden%>", "" + c.getLapCount())
						.replaceAll("<%rundenliste%>", "" + table);
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
	
	private void renderListOfResults() {
		
		// Die Erbenisliste (lor -> list of results) erstellen
		StringBuilder lorBuilder = new StringBuilder();
		lorBuilder.append("<table><tr><th>Name<th><th>Runden<th></tr>");
		
		for(Chip chip : comp.getChipsController().getChips()) {
			lorBuilder.append("<tr><td>" + chip.getStudentName() + "<td>"
					+ "<td>" + chip.getLapCount() + "</td></tr>");
		}
		
		lorBuilder.append("</table>");
		final String lor = lorBuilder.toString();
		
		try {
			// Die Vorlage auslesen
			List<String> list = Files.readAllLines(new File(Data.DIR + "/" + Data.BASIC_DIR + "/ergebnisliste-vorlage.html").toPath());
		
			// Zeile f�r Zeile in die Datei "letzterDruck.html" schreiben.
			// Dabei werden die entsprechenden Templatevariablen ersetzt:
			//   - <%list%>            Dort wird die Tabelle reingeschrieben
			PrintWriter pw = new PrintWriter(new FileWriter(Data.DIR + "/" + Data.BASIC_DIR + "/letzterDruck.html"));
			
			// Hier darf nicht gefiltert werden, da sonst HTML und CSS verloren geht
			list.stream().map(line -> { 
				return line = line.replaceAll("<%list%>", lor);
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
	
	private Printer getSelectedPrinter() {
		// Ausgew�hlten Printer holen
		// (muss leider so kompliziert sein, anders kann man auf die
		//  Elemente des ObservableSet nicht zugreifen...)
		return allPrinter.stream().filter(p -> {
			if(p.getName().equals(printerChoiceBox.getSelectionModel().getSelectedItem())) {
				return true;
			}
			return false;
		}).findFirst().get(); // endlich das Element bekommen..
	}

}
