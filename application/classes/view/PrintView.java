package classes.view;

import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

import classes.Data;
import classes.model.Chip;
import classes.model.Competition;
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
		// Ausgew�hlten Printer holen
		// (muss leider so kompliziert sein, anders kann man auf die
		//  Elemente des ObservableSet nicht zugreifen...)
		Printer printer = allPrinter.stream().filter(p -> {
			if(p.getName().equals(printerChoiceBox.getSelectionModel().getSelectedItem())) {
				return true;
			}
			return false;
		}).findFirst().get(); // endlich das Element bekommen..
		
		Node node = this.previewWebView;
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
		
		// Die Preview setzen
		try {
			previewWebView.getEngine().load(new File(Data.DIR + "/" + Data.BASIC_DIR + "/urkunde-vorlage.html").toURI().toURL().toString());
			previewWebView.setZoom(0.38);
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
