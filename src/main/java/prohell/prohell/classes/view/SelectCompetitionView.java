package prohell.prohell.classes.view;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prohell.prohell.classes.Constants;
import prohell.prohell.utils.logging.SimpleLoggingUtil;

interface SelectRunnable {
	void run() throws Exception;
}

public class SelectCompetitionView implements Initializable {

	@FXML
	private VBox root;

	private MainView mainView;
	
	// Dies ist ein experimenteller Ansatz. Der eigentliche Klick wird in einem
	// Runnable übergeben. Somit kann alles in eine Methode ausgelagert werden und der
	// "Inhalt" des Klicks wird an der entsprechenden Stelle ausgeführt. Somit spart man sich
	// doppelten Quelltext und wiederholt sich nicht selbst.
	// -> Fazit: Ein Versuch war es wert. Dieses Prinzip lässt sich bei größeren Auswahlverfahren
	// eventuell ganz gut anwenden. Bei denen die einzelnen Klicks unterschiedlicher sind als
	// in diesem Fall.
	private void runnableClick(SelectRunnable sr) {

		try {
			// Den "Inhalt" des Klicks ausführen
			sr.run();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Ein Fehler ist aufgetreten. Die Wettkampfoberfl�che konnte nicht geladen werden.");
			alert.show();
			
			e.printStackTrace();
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(e);
		}

		// zuletzt das Modal (dieses Fenster) schlie�en
		this.close();
	}

	// Click-Events
	public void timePaneClick(Event event) {

		runnableClick(() -> {	
			// Den Tab erstellen und hinzuf�gen
			TimeCompetitionView tcv = new TimeCompetitionView();
			if (tcv.checkRequirements()) {
				// wenn die Voraussetzungen geklärt sind, den View einbinden, sonst nicht
				mainView.addTab(mainView.createTab("Wettkampf (Zeit)", "/templates/competition/competitionViewTime.fxml", tcv));
				mainView.setCurrentCompetitionController(tcv.getCompetitionController());
			}
		});
	}

	public void distancePaneClick(Event event) {

		runnableClick(() -> {
			// Den Tab erstellen und hinzufügen
			DistanceCompetitionView dcv = new DistanceCompetitionView();
			if (dcv.checkRequirements()) {
				mainView.addTab(mainView.createTab("Wettkampf (Distanz)", "/templates/competition/competitionViewDistance.fxml", dcv));
				mainView.setCurrentCompetitionController(dcv.getCompetitionController());
			}
		});
	}

	private void close() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainView = MainView.get();
	}
}
