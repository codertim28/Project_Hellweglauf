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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.dialog.StandardAlert;
import tp.dialog.StandardMessageType;

public class MainView implements Initializable {

	@FXML private GridPane root;
	@FXML private TabPane tabPane;
	@FXML private Pane competitionPane, trainingPane;
	@FXML private Label errorLabel;
	// Unterpunkte des Datei-Men�
	@FXML private MenuItem saveMenu, openMenu;
	
	// Der MainView bekommt den ge�ffneten Wettkampf (und Repository), damit dieser
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
		// �bergeben, damit ein neuer Tab hinzugef�gt werden kann.
		// (sch�n ist das nicht, aber selten)
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
		// werden k�nnen...
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
		
		// Die HTML-Datei f�r den �ber-Tab
		File f = new File("about.html");
		// Den Tab erzeugen
		Tab tab = new Tab("�ber die Software");
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(20));
		
		WebView aboutWebView = new WebView();
		bp.setCenter(aboutWebView);
		
		tab.setContent(bp);
		
		try {
			aboutWebView.getEngine().load(f.toURI().toURL().toString());
			// Tab wird hier hinzugef�gt, damit dieser nicht angezeigt wird,
			// wenn die Datei(about.html) nicht geladen werden konnte.
			addTab(tab);
		} catch(MalformedURLException e) {
			// Dem Benutzer anzeigen, dass etwas nicht 
			// geklappt hat.
			errorLabel.setText("Die \"�ber\"-Seite konnte nicht geladen werden.");
		}
	}
	
	@FXML 
	private void saveMenuClick(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
        // Extensionfilter setzen 
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-Dateien (*.xml)", "*.xml"));
        // Speichern-Dialog anzeigen
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        
        if(file != null){
            currentCompetitionRepository.setPath(file.getAbsolutePath());
            currentCompetitionRepository.write(currentCompetition, true); // true: auf Thread warten
        }
	}
	
	@FXML 
	private void openMenuClick(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
        // Extensionfilter setzen 
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-Dateien (*.xml)", "*.xml"));
        // Speichern-Dialog anzeigen
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        
        if(file != null){
            currentCompetitionRepository = new CompetitionRepository(file.getAbsolutePath());
            try {
				currentCompetition = currentCompetitionRepository.read(true); // true: Pfad beachten
			
				// Den Tab erstellen und hinzuf�gen
				CompetitionView cv;
				if(currentCompetition.getType() == 0) {
					// FIXME: richtigen View verwenden...
					cv = new TimeCompetitionView(currentCompetition, currentCompetitionRepository);
					// Hier m�ssen keine Vorrausetzungen gekl�rt werden, da der Benutzer
					// lediglich einen vorhandenen Wettkampf l�dt und keinen neuen erstellen
					// m�chte...
					addTab(createTab("Wettkampf (Zeit)", "/templates/competition/competitionViewTime.fxml", cv));
				}
				else {
					cv = new TimeCompetitionView(currentCompetition, currentCompetitionRepository);
					// s.o.
					addTab(createTab("Wettkampf (Distanz)", "/templates/competition/competitionViewDistance.fxml", cv));
				}
				
				// Ui updaten
				setCurrentCompetitionAndRepository(cv.getCompetition(), cv.getCompetitionRepository());
            } catch (IOException ioe) {
            	ioe.printStackTrace();
				new StandardAlert(StandardMessageType.ERROR).showAndWait();
			} 
        }
	}
	
	// GETTER UND SETTER
	public void setCurrentCompetitionAndRepository(Competition c, CompetitionRepository cr) {
		currentCompetition = c;
		currentCompetitionRepository = cr;
		
		toggleCompetitionRelevantUIComponents();
	}
	
	// So muss es keinen Getter geben, der das 
	// ganze TabPane nach au�en gibt.
	public void addTab(Tab tab) {
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);	
		
		// Ein Event-Handler f�r das Schlie�en eines Wettkampfes einh�ngen
		tab.setOnClosed(e -> {
			// TODO: Benutzer fragen, ob Tab wirklich geschlossen werden soll
			setCurrentCompetitionAndRepository(null, null);
		});
	}
	
	public Tab createTab(String title, String resource, CompetitionView view) throws IOException {
		Tab tab = new Tab(title);
		// vorbereiten...
		FXMLLoader templateLoader = new FXMLLoader(getClass().getResource(resource));
		templateLoader.setController(view);
		// und laden
		tab.setContent(templateLoader.load());
		
		return tab;
	}
	
	private void check() {
		errorLabel.setText(new String());
		// Testen, ob es Chips gibt. Falls es keine Chips gibt, m�ssen der Wettkampf- und 
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
	
	private void toggleCompetitionRelevantUIComponents() {
		if(currentCompetition != null && currentCompetitionRepository != null) {
			// Nur ein Wettkampf darf ge�ffnet sein
			saveMenu.setDisable(false);
			openMenu.setDisable(true);
			competitionPane.setDisable(true);
			trainingPane.setDisable(true);
		}
		else {
			saveMenu.setDisable(true);
			openMenu.setDisable(false);
			competitionPane.setDisable(false);
			trainingPane.setDisable(false);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		check();	
		toggleCompetitionRelevantUIComponents();
	}
}
