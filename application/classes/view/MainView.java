package classes.view;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.Data;
import classes.SetupUtils;
import classes.model.Competition;
import classes.repository.CompetitionRepository;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainView implements Initializable {

	@FXML private TabPane tabPane;
	@FXML private Pane competitionPane, trainingPane;
	@FXML private Label errorLabel;
	@FXML private Menu saveMenu;
	
	// Der MainView bekommt den geöffneten Wettkampf (und Repository), damit dieser
	// so gespeichert werden kann vom Benutzer...
	private Competition currentCompetition;
	private CompetitionRepository currentCompetitionRepository;
	
	// Click-Events
	public void competitionPaneClick(Event e) throws IOException {
		// Das Auswahlfenster erstellen
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(((Parent) e.getSource()).getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);

		// Das Template laden und dem Controller diesen Controller
		// übergeben, damit ein neuer Tab hinzugefügt werden kann.
		// (schön ist das nicht, aber selten)
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/competition/selectCompetitionView.fxml"));
		Parent parent = (Parent)loader.load();
		SelectCompetitionView scvc = (SelectCompetitionView)loader.getController();
		scvc.setMainViewController(this); 
		
		Scene scene = new Scene(parent, 300, 400);
		//scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void settingsBtnClick(ActionEvent e) throws IOException {

		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initOwner(((Button) e.getSource()).getScene().getWindow());
		stage.initModality(Modality.WINDOW_MODAL);
		
		// Wenn das Einstellungsfenster geschlossen wird, 
		// updaten, damit die Fehlernachrichten neu geschrieben
		// werden können...
		stage.setOnCloseRequest(EventHandler -> {
			check();
		});

		Parent parent = FXMLLoader.load(getClass().getResource("/templates/settings/settingsView.fxml"));
		Scene scene = new Scene(parent, 540, 400);
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML 
	private void aboutMenuClick() {
		
		// Die HTML-Datei für den Über-Tab
		File f = new File("about.html");
		// Den Tab erzeugen
		Tab tab = new Tab("Über die Software");
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(20));
		
		WebView aboutWebView = new WebView();
		bp.setCenter(aboutWebView);
		
		tab.setContent(bp);
		
		try {
			aboutWebView.getEngine().load(f.toURI().toURL().toString());
			// Tab wird hier hinzugefügt, damit dieser nicht angezeigt wird,
			// wenn die Datei(about.html) nicht geladen werden konnte.
			addTab(tab);
		} catch(MalformedURLException e) {
			// Dem Benutzer anzeigen, dass etwas nicht 
			// geklappt hat.
			errorLabel.setText("Die \"Über\"-Seite konnte nicht geladen werden.");
		}
	}
	
	@FXML 
	private void saveMenuClick() {
		// TODO: Speichern
	}
	
	// GETTER UND SETTER
	public void setCurrentCompetitionAndRepository(Competition c, CompetitionRepository cr) {
		currentCompetition = c;
		currentCompetitionRepository = cr;
		
		// Den Menüpunkt aktualisieren
		if(c != null && cr != null) {
			saveMenu.setDisable(false);
		}
		else {
			saveMenu.setDisable(true);
		}
	}
	
	// So muss es keinen Getter geben, der das 
	// ganze TabPane nach außen gibt.
	public void addTab(Tab tab) {
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);		
	}
	
	private void check() {
		errorLabel.setText(new String());
		// Testen, ob es Chips gibt. Falls es keine Chips gibt, müssen der Wettkampf- und 
		// Trainingsbutton deaktiviert werden. 
		int basicChipsFile = SetupUtils.testForFile(Data.BASIC_DIR + "/" + Data.CHIPS_FILE);
		
		// Die Chipsdatei im BASIC_DIR ist die Wichtigste. Ohne diese kann das Programm kaum richtig arbeiten,
		// da bei jedem neuen Wettkampf (und auch Training) die Chips von dort aus kopiert werden.
		if(basicChipsFile == 0) {
			errorLabel.setText("Anmerkung: Es sind keine Chips vorhanden. (Einstellungen -> Chips verwalten)");
		}
		else if(basicChipsFile < 0) {
			errorLabel.setText("Fehler: Die Datei \"data/basic/chips.xml\" ist nicht vorhanden.");
			errorLabel.setTooltip(new Tooltip("Dieser Fehler kann behoben werden, indem Chips eingetragen werden. "
					+ "(Einstellungen -> Chips verwalten)"));
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		check();		
	}
}
