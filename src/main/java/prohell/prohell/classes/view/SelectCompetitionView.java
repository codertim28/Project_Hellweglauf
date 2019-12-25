package prohell.prohell.classes.view;


import java.io.File;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prohell.prohell.classes.Constants;
import prohell.prohell.utils.logging.SimpleLoggingUtil;

interface SelectRunnable {
	void run() throws Exception;
}

public class SelectCompetitionView {

	@FXML
	private VBox root;

	private MainView mainView;

	// Dies ist ein experimenteller Ansatz. Der eigentliche Klick wird in einem
	// Runnable�bergeben. Somit kann alles in eine Methode ausgelagert werden und der
	// "Inhalt" des Klicks wird an der entsprechenden Stelle ausgef�hrt. Somit spart man sich
	// doppelten Quelltext und wiederholt sich nicht selbst.
	// -> Fazit: Ein Versuch war es wert. Dieses Prinzip l�sst sich bei gr��eren Auswahlverfahren
	// eventuell ganz gut anwenden. Bei denen die einzelnen Klicks unterschiedlicher sind als
	// in diesem Fall.
	private void runnableClick(SelectRunnable sr) {

		try {
			// Den "Inhalt" des Klicks ausf�hren
			sr.run();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Ein Fehler ist aufgetreten. Die Wettkampfoberfl�che konnte nicht geladen werden.");
			alert.show();
			
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
				// wenn die Voraussetzungen gekl�rt sind, den View einbinden, sonst nicht
				mainView.addTab(mainView.createTab("Wettkampf (Zeit)", "/templates/competition/competitionViewTime.fxml", tcv));
				mainView.setCurrentCompetitionController(tcv.getCompetitionController());
			}
		});
	}

	public void distancePaneClick(Event event) {

		runnableClick(() -> {
			// Den Tab erstellen und hinzuf�gen
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

	// Setzt den MainView, damit diese Klasse
	// bei einem Klick, einen Tab setzen kann.
	void setMainViewController(MainView mvc) {
		mainView = mvc;
	}
}
